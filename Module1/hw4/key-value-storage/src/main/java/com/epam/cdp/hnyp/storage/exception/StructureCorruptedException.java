package com.epam.cdp.hnyp.storage.exception;

import java.io.IOException;

public class StructureCorruptedException extends IOException {

    public StructureCorruptedException(String message, Throwable cause) {
        super(message, cause);
    }

    public StructureCorruptedException(String message) {
        super(message);
    }
    
}
