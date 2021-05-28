package com.epam.learn.multithreading.fjpfactorial;

import java.math.BigInteger;

public class RangeFactorial {
    public BigInteger calculate(int start, int end) {
        if (start == end) {
            return BigInteger.valueOf(end);
        } else {
            final BigInteger bigInteger = BigInteger.valueOf(start);
            return bigInteger.multiply(calculate(--start, end));
        }
    }
}
