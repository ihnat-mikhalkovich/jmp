package com.epam.learn.multithreading.mergesort;


public class MergeSortTest extends AbstractSortTest {

    @Override
    protected void sort(int[] array) {
        new MergeSort().sort(array);
    }
}