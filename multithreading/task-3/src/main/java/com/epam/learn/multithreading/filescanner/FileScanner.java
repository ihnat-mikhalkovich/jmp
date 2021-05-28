package com.epam.learn.multithreading.filescanner;

import lombok.NonNull;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicLong;

public interface FileScanner {
    @NonNull
    PathStatistic scan(@NonNull Path path);

    @NonNull
    PathStatistic countDirectoriesAndFiles(@NonNull Path path);

    @NonNull
    long size(@NonNull Path path, @NonNull AtomicLong counter);
}
