package com.epam.learn.multithreading.mergesort;

public class MergeSort {

    public static void swap(int[] array, int start, int end) {
        int t = array[start];
        array[start] = array[end];
        array[end] = t;
    }

    public static void merge(int[] array, int firstStart, int firstEnd, int secondStart, int secondEnd) {
        final int subArrayLength = secondEnd - firstStart + 1;
        final int[] sortedArray = new int[subArrayLength];
        for (int i = firstStart, j = secondStart, k = 0; k < sortedArray.length; k++) {
            if (i > firstEnd || j <= secondEnd && (array[i] > array[j])) {
                sortedArray[k] = array[j];
                j++;
            } else {
                sortedArray[k] = array[i];
                i++;
            }
        }

        for (int i = 0, j = firstStart; i < sortedArray.length; i++, j++) {
            array[j] = sortedArray[i];
        }
    }

    public void sort(int[] array) {
        sort(array, 0, array.length - 1);
    }

    public void sort(int[] array, int start, int end) {
        final int distance = end - start;
        if (distance == 0) {
            return;
        } else if (distance == 1) {
            if (array[start] > array[end]) {
                swap(array, start, end);
            }
            return;
        }

        final int subArrayLength = (distance + 1) / 2;

        final int firstEnd = start + subArrayLength - 1;
        sort(array, start, firstEnd);

        final int secondStart = firstEnd + 1;
        sort(array, secondStart, end);

        merge(array, start, firstEnd, secondStart, end);
    }
}
