package com.epam.learn.multithreading.mergesort;

public class CountedParallelMergeSortTest extends AbstractSortTest {

    @Override
    protected void sort(int[] array) {
        CountedParallelMergeSort.mergeSort(array);
    }
}