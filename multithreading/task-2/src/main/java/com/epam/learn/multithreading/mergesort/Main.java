package com.epam.learn.multithreading.mergesort;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        int[] array = {3, 5, 3, 2, 6, 7, 1, 2, 1};
        log.debug("Start array:  {}", array);

        final MergeSort mergeSort = new MergeSort();
        mergeSort.sort(array);

        log.debug("Result array: {}", array);
    }
}
