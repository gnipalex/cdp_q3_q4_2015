package com.epam.cdp.hnyp.storage.exception;

import java.io.IOException;

public class NoSuchBlockException extends IOException{

    public NoSuchBlockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchBlockException(String message) {
        super(message);
    }
    
}
