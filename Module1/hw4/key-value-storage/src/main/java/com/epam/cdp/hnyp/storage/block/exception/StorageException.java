package com.epam.cdp.hnyp.storage.block.exception;

public class StorageException extends Exception {

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageException(String message) {
        super(message);
    }

}
