package com.epam.learn.multithreading.mergesort;

public class ParallelMergeSortTest extends AbstractSortTest {

    @Override
    protected void sort(int[] array) {
        ParallelMergeSort.sort(array);
    }
}