package com.epam.learn.multithreading.filescanner;

import com.epam.learn.multithreading.filescanner.exception.FileNotFoundFileScannerException;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class FileScannerTest {
    private final FileScanner fileScanner = new FileScannerImpl();

    private static Stream<Arguments> provideFileSystem() throws IOException {
        final String fileMessage = "Hello world. I'm really happy to create the file scanner firstly with In-memory file system";
        final TestFileSystemMetadata unixMetadata = new TestFileSystemMetadata(30, 50, 40, fileMessage);
        final TestFileSystemMetadata windowsMetadata = new TestFileSystemMetadata(300, 500, 400, fileMessage);
        final TestFileSystemMetadata osXMetadata = new TestFileSystemMetadata(100, 300, 200, fileMessage);
        return Stream.of(
                Arguments.of(prepareFileSystem(Jimfs.newFileSystem(Configuration.unix()), unixMetadata), unixMetadata),
                Arguments.of(prepareFileSystem(Jimfs.newFileSystem(Configuration.windows()), windowsMetadata), windowsMetadata),
                Arguments.of(prepareFileSystem(Jimfs.newFileSystem(Configuration.osX()), osXMetadata), osXMetadata));
    }

    public static FileSystem prepareFileSystem(FileSystem fileSystem, TestFileSystemMetadata metadata) throws IOException {
        final String testPathString = "test";
        final Path test = fileSystem.getPath(testPathString);
        Files.createDirectory(test);

        for (int i = 0; i < metadata.dirsInTestFolder; i++) {
            final String dirPathString = testPathString + fileSystem.getSeparator() + "dir-" + i;
            final Path dir = fileSystem.getPath(dirPathString);
            Files.createDirectory(dir);

            for (int j = 0; j < metadata.filesInSubFolders; j++) {
                final String filePathString = dirPathString + fileSystem.getSeparator() + "file-" + j + ".txt";
                final Path file = fileSystem.getPath(filePathString);
                Files.createFile(file);
                final String message = metadata.fileMessage;
                Files.write(file, message.getBytes(StandardCharsets.UTF_8));
            }
            for (int j = 0; j < metadata.filesInTestFolder; j++) {
                final String filePathString = testPathString + fileSystem.getSeparator() + "file-" + j + ".txt";
                final Path file = fileSystem.getPath(filePathString);
                final String message = metadata.fileMessage;
                Files.write(file, message.getBytes(StandardCharsets.UTF_8));
            }

            for (int g = 0; g < metadata.dirsInTestFolder; g++) {
                final String dirPathString1 = testPathString + fileSystem.getSeparator() + "dir-" + i + fileSystem.getSeparator() + "folder-" + g;
                final Path dir1 = fileSystem.getPath(dirPathString1);
                Files.createDirectory(dir1);
            }
        }

        return fileSystem;
    }

    @ParameterizedTest
    @MethodSource("provideFileSystem")
    public void scanTest(FileSystem fileSystem, TestFileSystemMetadata metadata) throws IOException {
        final Path testPath = fileSystem.getPath("test");
        final PathStatistic result = fileScanner.scan(testPath);


        final long expectedFiles = metadata.filesInTestFolder
                + (long) metadata.dirsInTestFolder * metadata.filesInSubFolders;

        final int expectedDirectories = metadata.dirsInTestFolder
                + metadata.dirsInTestFolder * metadata.dirsInTestFolder + 1;

        final long expectedSize = (metadata.filesInTestFolder
                + (long) metadata.dirsInTestFolder * metadata.filesInSubFolders)
                * metadata.fileMessage.length();

        final PathStatistic expectedStatistic = new PathStatistic(expectedFiles, expectedDirectories, expectedSize);

        assertEquals(expectedStatistic, result);
    }

    @ParameterizedTest
    @MethodSource("provideFileSystem")
    public void fileScanTest(FileSystem fileSystem, TestFileSystemMetadata metadata) throws IOException {
        final Path testPath = fileSystem.getPath("test", "file-2.txt");
        final PathStatistic result = fileScanner.scan(testPath);

        final PathStatistic expected = new PathStatistic(1, 0, metadata.fileMessage.length());

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("provideFileSystem")
    public void fileNotExitsTest(FileSystem fileSystem, TestFileSystemMetadata metadata) throws IOException {
        final Path testPath = fileSystem.getPath("test", "this-file-not-exist.txt");
        assertThrows(FileNotFoundFileScannerException.class, () -> fileScanner.scan(testPath).getSize());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TestFileSystemMetadata {
        private int dirsInTestFolder;
        private int filesInSubFolders;
        private int filesInTestFolder;
        private String fileMessage;
    }
}
