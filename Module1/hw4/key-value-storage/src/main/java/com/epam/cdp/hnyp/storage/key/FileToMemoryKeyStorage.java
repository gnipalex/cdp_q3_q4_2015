package com.epam.cdp.hnyp.storage.key;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.epam.cdp.hnyp.storage.exception.InvalidDescriptorException;
import com.epam.cdp.hnyp.storage.exception.StorageException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class FileToMemoryKeyStorage implements KeyStorage {

    private Gson gson = new Gson();
    
    private File keysFile;
    
    private int valueBlockSize;
    
    private Map<String, KeyDescriptor> descriptorsMap = new HashMap<>();
    
    public FileToMemoryKeyStorage(File file, int valueBlockSize) throws IOException, StorageException {
        try (BufferedReader br = openReader(file)) {
            String line = null;
            while((line = br.readLine()) != null) {
                parseAndLoadKeyDescriptorToMap(line);
            }
        }
        this.keysFile = file;
        this.valueBlockSize = valueBlockSize;
    }
    
    private BufferedReader openReader(File file) throws FileNotFoundException {
        return new BufferedReader(new FileReader(file));
    }
    
    private void parseAndLoadKeyDescriptorToMap(String line) throws StorageException {
        try {
            KeyDescriptor descriptor = gson.fromJson(line, KeyDescriptor.class);
            //checkDescriptorIsUnique(descriptor);
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
    
//    private void checkDescriptorIsUnique(KeyDescriptor descriptor) throws StorageException {
//        if (read(descriptor.getKey()) != null) {
//           throw new StorageException("bad descriptor", 
//                   new StructureCorruptedException("key is not unique, " + descriptor));
//        }
//    }
    
    @Override
    public KeyDescriptor create(String key, int valueLength) throws StorageException {
        if (read(key) != null) {
            return null;
        }        
        KeyDescriptor newDescriptor = null;
        KeyDescriptor lastDescriptor = getLastDescriptorByStartBlock();
        if (lastDescriptor != null) {
            newDescriptor = KeyDescriptor.createAfterLast(lastDescriptor, key, valueLength, valueBlockSize);            
        } else {
            newDescriptor = KeyDescriptor.createFromStart(key, valueLength, valueBlockSize);
        }
        
        serializeAndWrite(newDescriptor);
        descriptorsMap.put(key, newDescriptor);
        
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
                // generate new key for deleted KeyDescriptor ???
                // we need to store deleted desriptors to not to loose deleted space in storage
                // but map doesnt allow to duplicate keys
                // or rewise deleted places management
            }
            //KeyDescriptor copyOfUpdateKeyDescriptor = new KeyDescriptor(updatedDescriptor);
            serializeAndWrite(updatedDescriptor);
            descriptorsMap.put(updatedDescriptor.getKey(), updatedDescriptor);
            return true;
        }
        return false;
    }
    
    private void checkDescriptorValidForUpdate(KeyDescriptor descriptor) throws StorageException {
        int blockCount = descriptor.getBlocksCount();
        int requiredBlockCount = descriptor.getValueLength() / valueBlockSize + 1;
        if ( blockCount < requiredBlockCount) {
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
    
    private void writeAllKeysDescriptorsToFile() {
        try (BufferedWriter bw = openOverwriteWriter(keysFile)) {
            descriptorsMap.entrySet().stream()
                .map(e -> e.getValue())
                .map(gson::toJson)
                .forEach(json -> writeLine(bw, json));
        } catch (IOException | IOExceptionRuntimeWrapper e) {
            // log
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
        return new BufferedWriter(new FileWriter(keysFile, true));
    }
    
    private BufferedWriter openOverwriteWriter(File file) throws IOException {
        return new BufferedWriter(new FileWriter(file));
    }
    
    private static class IOExceptionRuntimeWrapper extends RuntimeException {

        public IOExceptionRuntimeWrapper(Throwable cause) {
            super(cause);
        }
        
    }

}
