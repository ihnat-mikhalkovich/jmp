package com.epam.learn.multithreading.filescanner;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicLong;

public class FileScannerImpl implements FileScanner {

    @Override
    public PathStatistic scan(Path path) {
        final PathStatistic pathStatistic = this.countDirectoriesAndFiles(path);
        pathStatistic.setSize(this.size(path, new AtomicLong(0)));

        return pathStatistic;
    }

    @Override
    public PathStatistic countDirectoriesAndFiles(Path path) {
        final FilesAndDirectoriesCounter counter = FilesAndDirectoriesCounter.pathCounter(path);
        return counter.invoke();
    }

    @Override
    public long size(Path path, AtomicLong counter) {
        final PathSizeScanner sizeScanner = PathSizeScanner.pathSize(path, counter);
        return sizeScanner.invoke();
    }
}
