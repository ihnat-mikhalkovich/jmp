package com.epam.learn.multithreading.filescanner.exception;

import java.io.IOException;

public class FileNotFoundFileScannerException extends FileScannerException {
    public FileNotFoundFileScannerException(String message, IOException cause) {
        super(message, cause);
    }

    public FileNotFoundFileScannerException(IOException cause) {
        super(cause);
    }
}
