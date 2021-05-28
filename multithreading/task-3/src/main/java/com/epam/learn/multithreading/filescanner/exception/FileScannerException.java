package com.epam.learn.multithreading.filescanner.exception;

import java.io.IOException;
import java.io.UncheckedIOException;

public class FileScannerException extends UncheckedIOException {

    public FileScannerException(String message, IOException cause) {
        super(message, cause);
    }

    public FileScannerException(IOException cause) {
        super(cause);
    }
}
