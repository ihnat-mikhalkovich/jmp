package com.epam.learn.multithreading.filescanner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

import static com.epam.learn.multithreading.filescanner.util.FilesUtil.wrappedList;

public class FilesAndDirectoriesCounter extends RecursiveTask<PathStatistic> {

    private final Path path;

    private FilesAndDirectoriesCounter(Path path) {
        this.path = path;
    }

    public static FilesAndDirectoriesCounter pathCounter(Path path) {
        return new FilesAndDirectoriesCounter(path);
    }

    @Override
    protected PathStatistic compute() {
        if (!Files.isDirectory(path)) {
            return new PathStatistic(1, 0, 0);
        } else {
            final List<FilesAndDirectoriesCounter> counters = wrappedList(path)
                    .map(FilesAndDirectoriesCounter::new)
                    .collect(Collectors.toList());

            invokeAll(counters);

            Collections.reverse(counters);

            final PathStatistic pathStatistic = counters.stream()
                    .map(ForkJoinTask::join)
                    .reduce((stat1, stat2) -> {
                        final long f1 = stat1.getFilesCount();
                        final long f2 = stat2.getFilesCount();

                        final long d1 = stat1.getDirectoriesCount();
                        final long d2 = stat2.getDirectoriesCount();

                        stat1.setFilesCount(f1 + f2);
                        stat1.setDirectoriesCount(d1 + d2);
                        return stat1;
                    }).orElse(new PathStatistic());

            long directoriesCount = pathStatistic.getDirectoriesCount();
            pathStatistic.setDirectoriesCount(++directoriesCount);

            return pathStatistic;
        }
    }
}
