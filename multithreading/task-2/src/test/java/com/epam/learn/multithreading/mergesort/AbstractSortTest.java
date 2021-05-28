package com.epam.learn.multithreading.mergesort;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public abstract class AbstractSortTest {

    private static Stream<Arguments> arraysToSort() {
        return Stream.of(
                Arguments.of(new int[]{3, 5, 3, 2, 6, 7, 1, 2, 1}),
                Arguments.of(new int[]{4, 5, 10, 13, 15, 16, 19, 23, 30, 33, 37, 42, 47, 55, 57, 58, 59, 60, 61, 62, 68, 76, 81, 86, 89, 90, 93, 95, 98, 102, 104, 105, 112, 113, 116, 122, 128, 132, 134, 139, 140, 141, 143, 147, 155, 156, 157, 159, 161, 163, 165, 166, 169, 171, 172, 174, 176, 178, 185, 186, 187, 192, 194, 197, 202, 211, 214, 216, 217, 221, 231, 232, 235, 237, 241, 246, 248, 253, 255, 258, 259, 261, 262, 263, 270, 276, 281, 288, 294, 296}),
                Arguments.of(new int[]{246, 204, 160, 65, 106, 234, 113, 12, 47, 116, 282, 249, 115, 80, 5, 27, 117, 16, 189, 207, 132, 56, 283, 64, 299, 296, 288, 157, 77, 198, 168, 243, 32, 125, 298, 133, 261, 119, 108, 110, 256, 200, 99, 100, 267, 61, 31, 180, 205, 242, 215, 240, 18, 35, 91, 170, 188, 233, 103, 147, 285, 138, 195, 17, 255, 259, 82, 134, 175, 272, 11, 292, 127, 14, 155, 287, 8, 42, 142, 206, 45, 294, 277, 278, 78, 7, 236, 104, 74, 146}),
                Arguments.of(new int[]{248, 99, 153, 242, 215, 226, 194, 239, 119, 223, 278, 131, 267, 84, 211, 46, 82, 265, 150, 52, 293, 19, 76, 60, 45, 251, 221, 135, 87, 222, 214, 78, 29, 257, 74, 175, 96, 253, 235, 152, 289, 7, 115, 261, 122, 210, 169, 61, 173, 118, 259, 130, 128, 111, 247, 43, 256, 30, 120, 117, 26, 281, 91, 185, 11, 204, 297, 171, 123, 27, 179, 148, 17, 41, 28, 252, 90, 67, 268, 187, 176, 127, 284, 170, 15, 145, 100, 51, 97, 191})
        );
    }

    @ParameterizedTest
    @MethodSource("arraysToSort")
    public void testSort(int[] array) {
        int[] expectedArray = Arrays.stream(array)
                .sorted()
                .toArray();

        sort(array);


        assertArrayEquals(expectedArray, array);
    }

    protected abstract void sort(int[] array);
}
