package com.epam.learn.multithreading.fjpfactorial;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class ParallelFactorial extends RecursiveTask<BigInteger> {
    private final int end;
    private final int threshold;
    private RangeFactorial rangeFactorial;
    private int start = 2;

    private ParallelFactorial(int start, int end, int threshold, RangeFactorial rangeFactorial) {
        if (start <= 0) {
            throw new IllegalArgumentException("'start' argument should be positive (greater then zero)");
        }

        if (end <= 0) {
            throw new IllegalArgumentException("'end' argument should be positive (greater then zero)");
        }

        if (threshold <= 0) {
            throw new IllegalArgumentException("'threshold' argument should be positive (greater then zero)");
        }

        this.start = start;
        this.end = end;
        this.threshold = threshold;
        this.rangeFactorial = rangeFactorial;
    }

    public ParallelFactorial(int number, int threshold) {
        if (number <= 0) {
            throw new IllegalArgumentException("'number' argument should be positive (greater then zero)");
        }

        if (threshold <= 0) {
            throw new IllegalArgumentException("'threshold' argument should be positive (greater then zero)");
        }

        this.threshold = threshold;
        this.end = number;
        this.rangeFactorial = new RangeFactorial();
    }

    public void setRangeFactorial(RangeFactorial rangeFactorial) {
        this.rangeFactorial = rangeFactorial;
    }

    @Override
    protected BigInteger compute() {
        final int distance = end - start;
        if (distance <= threshold) {
            return rangeFactorial.calculate(end, start);
        }

        final int capacity = (int) Math.ceil(((double) distance + 1) / threshold);
        final List<ParallelFactorial> tasks = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            final int newStart = threshold * i + start;
            final int newEnd = Math.min(newStart + threshold - 1, end);
            final ParallelFactorial task = new ParallelFactorial(newStart, newEnd, threshold, rangeFactorial);
            tasks.add(task);
        }

        invokeAll(tasks);

        return tasks.stream()
                .map(ParallelFactorial::join)
                .reduce(BigInteger::multiply)
                .orElseThrow(() -> new RuntimeException("Exception in stream."));
    }
}
