package com.epam.learn.multithreading.filescanner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static com.epam.learn.multithreading.filescanner.util.FilesUtil.wrappedList;
import static com.epam.learn.multithreading.filescanner.util.FilesUtil.wrappedSize;

public class PathSizeScanner extends RecursiveTask<Long> {

    private final Path path;
    private final AtomicLong fileCounter;

    private PathSizeScanner(Path path, AtomicLong fileCounter) {
        this.path = path;
        this.fileCounter = fileCounter;
    }

    public static PathSizeScanner pathSize(Path path, AtomicLong fileCounter) {
        return new PathSizeScanner(path, fileCounter);
    }

    @Override
    protected Long compute() {
        if (!Files.isDirectory(path)) {
            this.fileCounter.getAndIncrement();
            return wrappedSize(path);
        }

        final List<PathSizeScanner> scanners = wrappedList(path)
                .map(p -> new PathSizeScanner(p, this.fileCounter))
                .collect(Collectors.toList());

        invokeAll(scanners);

        Collections.reverse(scanners);

        return scanners.stream()
                .mapToLong(ForkJoinTask::join)
                .sum();
    }
}
