package com.epam.cdp.hnyp.storage.key.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.epam.cdp.hnyp.storage.exception.InvalidDescriptorException;
import com.epam.cdp.hnyp.storage.exception.StorageException;
import com.epam.cdp.hnyp.storage.key.KeyDescriptor;
import com.epam.cdp.hnyp.storage.key.KeyDescriptorValidator;
import com.epam.cdp.hnyp.storage.key.KeyStorage;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class FileToMemoryKeyStorage implements KeyStorage {

//    private static final String DEFAULT_ENCODING = "cp1251";
//    
    private Gson gson = new Gson();
        
    private Charset charset;
    
    private File keysFile;
    
    private int valueBlockSize;
    
    private Map<String, KeyDescriptor> descriptorsMap = new HashMap<>();
    
    public FileToMemoryKeyStorage(File file, String encoding, int valueBlockSize) throws IOException, StorageException {
        this.keysFile = file;
        this.valueBlockSize = valueBlockSize;
        this.charset = Charset.forName(encoding);
        readAllKeysToMap();
    }
    
    private void readAllKeysToMap() throws IOException, StorageException {
        if (keysFile.exists() && keysFile.length() > 0) {
            try (BufferedReader br = openReader(keysFile)) {
                String line = null;
                while(StringUtils.isNotEmpty(line = br.readLine())) {
                    parseAndLoadKeyDescriptorToMap(line);
                }
            }
        }
    }
    
    private BufferedReader openReader(File file) throws FileNotFoundException, UnsupportedEncodingException {
        Reader reader = new InputStreamReader(new FileInputStream(file), charset); 
        return new BufferedReader(reader);
    }
    
    private void parseAndLoadKeyDescriptorToMap(String line) throws StorageException {
        try {
            KeyDescriptor descriptor = gson.fromJson(line, KeyDescriptor.class);
            // the goal is to allow duplication - last one will overwrite others
            // this situation only possible when file was not overwritten after work is done
            checkDescriptorFormat(descriptor);
            descriptorsMap.put(descriptor.getKey(), descriptor);
        } catch (JsonSyntaxException jse) {
            throw new StorageException("bad format of json : " + line, jse);
        }
    }
    
    private void checkDescriptorFormat(KeyDescriptor descriptor) throws StorageException {
       List<String> validationErrors = KeyDescriptorValidator.validate(descriptor);
       if (!validationErrors.isEmpty()) {
           String error = validationErrors.stream().collect(Collectors.joining(", "));
           throw new StorageException("bad descriptor", 
                   new InvalidDescriptorException(MessageFormat.format("{0} - {1}", descriptor, error)));
       }
    }
    
    @Override
    public KeyDescriptor create(String key, int valueLength, Class<?> clazz) throws StorageException {
        KeyDescriptor newDescriptor = null;
        
        KeyDescriptor existingDescriptor = descriptorsMap.get(key);
        if (existingDescriptor != null) {
            newDescriptor = createOverridingExistingDeleted(existingDescriptor, valueLength, clazz);
        } else  {
            newDescriptor = createNew(key, valueLength, clazz);
        }
        
        return newDescriptor;
    }
    
    private KeyDescriptor createOverridingExistingDeleted(KeyDescriptor deletedDescriptor, int valueLength, 
            Class<?> clazz) throws StorageException {
        if (!deletedDescriptor.isDeleted()) {
            return null;
        }
        KeyDescriptor resizedDescriptor = KeyDescriptor.resize(deletedDescriptor, valueLength, valueBlockSize);
        resizedDescriptor.setValueClass(clazz);
        resizedDescriptor.resetDeleted();
        if (!update(resizedDescriptor)) {
            throw new StorageException("internal key error, could not update deleted key descriptor");
        }
        return resizedDescriptor;
    }
    
    private KeyDescriptor createNew(String key, int valueLength, Class<?> clazz) throws StorageException {
        KeyDescriptor newDescriptor = null;
        KeyDescriptor lastDescriptor = getLastDescriptorByStartBlock();
        if (lastDescriptor != null) {
            newDescriptor = KeyDescriptor.createAfterLast(lastDescriptor, key, valueLength, valueBlockSize, clazz);            
        } else {
            newDescriptor = KeyDescriptor.createFromStart(key, valueLength, valueBlockSize, clazz);
        }
        serializeAndWrite(newDescriptor);
        putCopyToMap(newDescriptor);
        return newDescriptor;
    }
    
    private void serializeAndWrite(KeyDescriptor descriptor) throws StorageException {
        String json = gson.toJson(descriptor);
        try (BufferedWriter bw = openAppendWriter(keysFile)) {
            writeLine(bw, json);
        } catch (IOException | IOExceptionRuntimeWrapper e) {
            throw new StorageException("can't save key to key storage", e);
        }
    }
    
    private KeyDescriptor getLastDescriptorByStartBlock() {
        return descriptorsMap.entrySet().stream()
            .map(e -> e.getValue())
            .sorted((v1, v2) -> v2.getStartBlock() - v1.getStartBlock())
            .findFirst().orElse(null);
    }

    @Override
    public KeyDescriptor read(String key) {
        KeyDescriptor keyDescriptor = descriptorsMap.get(key);
        if (keyDescriptor != null) {
            return new KeyDescriptor(keyDescriptor);
        }
        return null;
    }

    @Override
    public boolean update(KeyDescriptor updatedDescriptor) throws StorageException {
        checkDescriptorValidForUpdate(updatedDescriptor);
        KeyDescriptor oldDescriptor = descriptorsMap.get(updatedDescriptor.getKey());
        if (oldDescriptor != null) {  
            if (needsNewBlockAfterUpdate(oldDescriptor, updatedDescriptor)) {
                // we need to store deleted desriptors to not to loose deleted space in storage
                // but map doesnt allow duplicate keys, so we need to genarate new key for deleted descriptor
                // also in file it is required too
                oldDescriptor.markDeleted();
                oldDescriptor.setKey(generateNewKeyForDeletedDescriptor());
                serializeAndWrite(oldDescriptor);
            } 
            serializeAndWrite(updatedDescriptor);
            putCopyToMap(updatedDescriptor); 
            return true;
        }
        return false;
    }
    
    private void putCopyToMap(KeyDescriptor original) {
        KeyDescriptor copyDescriptor = new KeyDescriptor(original);
        descriptorsMap.put(copyDescriptor.getKey(), copyDescriptor);
    }
    
    private String generateNewKeyForDeletedDescriptor() {
        return UUID.randomUUID().toString();
    }
    
    private void checkDescriptorValidForUpdate(KeyDescriptor descriptor) throws StorageException {
        int blockCount = descriptor.getBlocksCount();
        int requiredBlockCount = KeyDescriptor.requiredBlocksCount(descriptor.getValueLength(), valueBlockSize);
        if (blockCount < requiredBlockCount) {
            throw new StorageException("bad descriptor for update", 
                    new InvalidDescriptorException(
                            MessageFormat.format("blocksCount {0} while required {1}", blockCount, requiredBlockCount)));
        }
    }
    
    private boolean needsNewBlockAfterUpdate(KeyDescriptor old, KeyDescriptor updated) {
        return updated.getBlocksCount() > old.getBlocksCount();
    }

    @Override
    public boolean delete(String key) throws StorageException {
        KeyDescriptor descriptorToDel = descriptorsMap.get(key);
        if (descriptorToDel != null) {
            //modifying copy 'cause saving may fail 
            KeyDescriptor copyOfDelDescriptor = new KeyDescriptor(descriptorToDel);
            copyOfDelDescriptor.markDeleted();
            serializeAndWrite(copyOfDelDescriptor);
            descriptorToDel.markDeleted();
            return true;
        }
        return false;
    }

    @Override
    public void close() throws IOException {
        writeAllKeysDescriptorsToFile();
    }
    
    private void writeAllKeysDescriptorsToFile() throws IOException {
        try (BufferedWriter bw = openOverwriteWriter(keysFile)) {
            descriptorsMap.entrySet().stream()
                .map(e -> e.getValue())
                .map(gson::toJson)
                .forEach(json -> writeLine(bw, json));
        } catch (IOExceptionRuntimeWrapper e) {
            throw new IOException(e);
        }
    }
    
    private void writeLine(BufferedWriter bw, String line) {
        try {
            bw.write(line);
            bw.write('\n');
        } catch (IOException ioe) {
            throw new IOExceptionRuntimeWrapper(ioe);
        }
    }
    
    private BufferedWriter openAppendWriter(File file) throws IOException {
        Writer writer = new OutputStreamWriter(new FileOutputStream(file, true), charset);
        return new BufferedWriter(writer);
    }
    
    private BufferedWriter openOverwriteWriter(File file) throws IOException {
        Writer writer = new OutputStreamWriter(new FileOutputStream(file, false), charset);
        return new BufferedWriter(writer);
    }
    
    private static class IOExceptionRuntimeWrapper extends RuntimeException {
        public IOExceptionRuntimeWrapper(Throwable cause) {
            super(cause);
        }  
    }

}
