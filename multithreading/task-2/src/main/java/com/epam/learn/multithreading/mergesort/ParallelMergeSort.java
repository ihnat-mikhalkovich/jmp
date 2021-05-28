package com.epam.learn.multithreading.mergesort;

import java.util.concurrent.RecursiveAction;

import static com.epam.learn.multithreading.mergesort.MergeSort.merge;
import static com.epam.learn.multithreading.mergesort.MergeSort.swap;

public class ParallelMergeSort extends RecursiveAction {

    private final int[] array;

    private final int start, end;

    public static void sort(int[] array) {
        if (array == null) {
            throw new IllegalArgumentException("'array' should not be null");
        }

        if (array.length == 0) {
            throw new IllegalArgumentException("'array' should not be empty");
        }

        new ParallelMergeSort(array, 0, array.length - 1).invoke();
    }

    private ParallelMergeSort(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
        final int distance = end - start;
        if (distance >= 2) {
            final int subArrayLength = (distance + 1) / 2;

            final int firstEnd = start + subArrayLength - 1;
            final ParallelMergeSort first = new ParallelMergeSort(array, start, firstEnd);

            final int secondStart = firstEnd + 1;
            final ParallelMergeSort second = new ParallelMergeSort(array, secondStart, end);

            invokeAll(second, first);

            merge(array, start, firstEnd, secondStart, end);
        } else if (end > start && array[start] > array[end]) {
            swap(array, start, end);
        }
    }
}
