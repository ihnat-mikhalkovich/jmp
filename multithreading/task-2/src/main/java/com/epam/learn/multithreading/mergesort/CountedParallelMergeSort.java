package com.epam.learn.multithreading.mergesort;

import java.util.concurrent.CountedCompleter;

import static com.epam.learn.multithreading.mergesort.MergeSort.merge;
import static com.epam.learn.multithreading.mergesort.MergeSort.swap;

public class CountedParallelMergeSort extends CountedCompleter<Void> {

    private final int[] array;

    private final int start, end;

    private Runnable onComplete = () -> {};

    public static void mergeSort(int[] array) {
        if (array == null) {
            throw new IllegalArgumentException("'array' should not be null");
        }

        if (array.length == 0) {
            throw new IllegalArgumentException("'array' should not be empty");
        }

        new CountedParallelMergeSort(null, array, 0, array.length - 1).invoke();
    }

    private CountedParallelMergeSort(CountedParallelMergeSort parent, int[] array, int start, int end) {
        super(parent);
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    public void compute() {
        final int distance = end - start;
        if (distance >= 2) {
            final int subArrayLength = (distance + 1) / 2;

            setPendingCount(2);

            final int firstEnd = start + subArrayLength - 1;
            new CountedParallelMergeSort(this, array, start, firstEnd).fork();

            final int secondStart = firstEnd + 1;
            new CountedParallelMergeSort(this, array, secondStart, end).compute();

            this.onComplete = () -> merge(array, start, firstEnd, secondStart, end);

        } else if (end > start && array[start] > array[end]) {
            swap(array, start, end);
        }

        tryComplete();
    }

    @Override
    public void onCompletion(CountedCompleter<?> caller) {
        onComplete.run();
    }
}
