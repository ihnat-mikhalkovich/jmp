package com.epam.learn.multithreading.filescanner;

import lombok.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        if (args == null || args.length == 0) {
            System.out.println("Directory path did not provided.");
            return;
        }
        System.out.println(Arrays.asList(args));

        final FileScanner fileScanner = new FileScannerImpl();

        final Path path = Paths.get(args[0]);
        final PathStatistic statistic = fileScanner.countDirectoriesAndFiles(path);

        final AtomicLong passedFiles = new AtomicLong(0);

        System.out.println("Directories: " + statistic.getDirectoriesCount());
        System.out.println("Files: " + statistic.getFilesCount());

        final ExecutorService executorService = Executors.newFixedThreadPool(3);

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        executorService.submit(() -> {
            final StatusBarCreator creator = new StatusBarCreator(new StatusBarMeta());
            while (statistic.getFilesCount() > passedFiles.get()) {
                final double currentPercent = (double) passedFiles.get() / statistic.getFilesCount() * 100;
                final String message = creator.create((int) currentPercent);
                System.out.print(message + '\r');
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            countDownLatch.countDown();
        });

        executorService.submit(() -> {
            final Scanner scanner = new Scanner(System.in);

            try {
                final String s = scanner.nextLine();
            } finally {
                scanner.close();
                executorService.shutdown();
                System.exit(1);
            }
        });


        executorService.submit(() -> {
            final long size = fileScanner.size(path, passedFiles);
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Size: " + size);
            executorService.shutdown();
            System.exit(1);
        });
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class StatusBarMeta {
        private String leftBorder = "(";
        private String rightBorder = ")";
        private String slider = ">";
        private String filledSpace = "-";
        private String notFiledSpace = "_";
        private int length = 100;
    }

    @RequiredArgsConstructor
    public static class StatusBarCreator {
        private final StatusBarMeta statusBarMeta;

        public String create(int percents) {
            final double current = Math.floor((double) percents * statusBarMeta.length / 100);

            StringBuilder afterSlider = new StringBuilder();
            for (int i = 0; i < current; i++) {
                afterSlider.append(statusBarMeta.filledSpace);
            }

            StringBuilder beforeSlider = new StringBuilder();
            for (int i = 0; i < statusBarMeta.length - current; i++) {
                beforeSlider.append(statusBarMeta.notFiledSpace);
            }

            return statusBarMeta.leftBorder + afterSlider + statusBarMeta.slider + beforeSlider + statusBarMeta.rightBorder;
        }
    }
}
