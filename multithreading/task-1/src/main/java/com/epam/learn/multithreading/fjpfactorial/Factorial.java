package com.epam.learn.multithreading.fjpfactorial;

import java.math.BigInteger;

public class Factorial {
    private final RangeFactorial rangeFactorial;

    public Factorial() {
        rangeFactorial = new RangeFactorial();
    }

    public Factorial(RangeFactorial rangeFactorial) {
        this.rangeFactorial = rangeFactorial;
    }

    public BigInteger factorial(int number) {
        return rangeFactorial.calculate(number, 1);
    }
}
