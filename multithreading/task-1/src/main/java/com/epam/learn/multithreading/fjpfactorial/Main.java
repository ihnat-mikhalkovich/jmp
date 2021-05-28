package com.epam.learn.multithreading.fjpfactorial;

import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.concurrent.ForkJoinPool;

@Slf4j
public class Main {
    public static void main(String[] args) {
        final Factorial factorial = new Factorial();
        long start = System.currentTimeMillis();
        final BigInteger sequentialResult = factorial.factorial(5);
        long end = System.currentTimeMillis();
        log.debug("Result for sequential flow is {}", sequentialResult);
        log.debug("Time for sequential flow is {}.", end - start);

        final ParallelFactorial parallelFactorial = new ParallelFactorial(5, 3);
        final ForkJoinPool forkJoinPool = new ForkJoinPool();
        start = System.currentTimeMillis();
        final BigInteger parallelResult = forkJoinPool.invoke(parallelFactorial);
        end = System.currentTimeMillis();
        log.debug("Result for parallel flow is {}", parallelResult);
        log.debug("Time for parallel flow is {}.", end - start);

        log.debug((sequentialResult.equals(parallelResult)) ? "Both flows have the same result." : "Something was wrong and the results are different");
    }


}
