package com.epam.cdp.hnyp.storage.block.impl;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.apache.commons.lang3.ArrayUtils;

import com.epam.cdp.hnyp.storage.block.BlockStorage;
import com.epam.cdp.hnyp.storage.exception.NoSuchBlockException;
import com.epam.cdp.hnyp.storage.exception.StorageException;
import com.epam.cdp.hnyp.storage.exception.StructureCorruptedException;

public class FileMappingBlockStorage implements BlockStorage {
    private int blockSize;
    private RandomAccessFile raFile;
    
    public FileMappingBlockStorage(File file, int blockSize) throws FileNotFoundException {
        if (blockSize <= 0) {
            throw new IllegalArgumentException("block size should be positive");
        }
        this.raFile = new RandomAccessFile(file, "rwd");
        this.blockSize = blockSize;
    }

    @Override
    public byte[] readBlock(int block) throws StorageException {
        long position = block * blockSize;
        try {
            long fileSize = raFile.length();
            checkBlockIsCorrectForReading(fileSize, position);
            
            raFile.seek(position);  
            byte[] readedBytes = new byte[blockSize];
            int actualReadedCount = raFile.read(readedBytes);
            checkExactlyBlockSizeReaded(actualReadedCount);
            
            return readedBytes;
        } catch (IOException e) {
            throw new StorageException("i/o error", e);
        }
    }
    
    private void checkExactlyBlockSizeReaded(int actualReadedCount) throws StorageException {
        if (actualReadedCount < blockSize) {
            throw new StorageException("failed to read whole block");
        }
    }
    
    private void checkBlockIsCorrectForReading(long fileSize, long position) throws StorageException {
        if (position < 0) {
            throw new StorageException("can't read block", 
                    new NoSuchBlockException("negative position"));
        }
        if (fileSize - position < blockSize) {
            throw new StorageException("can't read block", 
                    new NoSuchBlockException("cannot read block, file size is less than position + block size"));
        }
    }

    @Override
    public void writeBlock(byte[] source, int offset) throws StorageException {
        long position = offset * blockSize;
        try {
            long fileSize = raFile.length();
            checkBlockIsCorrectForWriting(fileSize, position);
            
            raFile.seek(position);
            byte[] arrayToWrite = getArrayOfBlockSize(source);
            raFile.write(arrayToWrite);
        } catch (IOException e) {
            throw new StorageException("i/o error", e);
        }
    }
    
    private byte[] getArrayOfBlockSize(byte[] source) {
        byte[] arrayToWrite = source;
        if (source.length > blockSize) {
            arrayToWrite = ArrayUtils.subarray(source, 0, blockSize);
        } else if (source.length < blockSize) {
            arrayToWrite = new byte[blockSize];
            System.arraycopy(source, 0, arrayToWrite, 0, source.length);
        }
        return arrayToWrite;
    }
    
    private void checkBlockIsCorrectForWriting(long fileSize, long position) throws StorageException {
        if (position < 0) {
            throw new StorageException("can't write block", 
                    new NoSuchBlockException("negative position"));
        }
        if (fileSize < position) {
            throw new StorageException("can't write block", 
                    new StructureCorruptedException("specified offset breaks files structure"));
        }
    }

    @Override
    public void close() throws IOException {
        raFile.close();
    }
     
}
