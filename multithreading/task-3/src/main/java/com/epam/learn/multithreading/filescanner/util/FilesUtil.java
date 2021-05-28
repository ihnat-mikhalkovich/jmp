package com.epam.learn.multithreading.filescanner.util;

import com.epam.learn.multithreading.filescanner.exception.FileNotFoundFileScannerException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FilesUtil {

    public static Stream<Path> wrappedList(Path path) {
        try {
            return Files.list(path);
        } catch (IOException e) {
            throw new FileNotFoundFileScannerException(e);
        }
    }

    public static long wrappedSize(Path path) {
        try {
            return Files.size(path);
        } catch (IOException e) {
            throw new FileNotFoundFileScannerException(e);
        }
    }
}
