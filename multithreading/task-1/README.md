# Task 1 (Optional)

## Task description
Please, complete the following task: __Factorial via FJP__
- Use FJP to calculate the factorial.
  Compare with the sequential implementation.
  Use BigInteger to keep values. 

## Solution
__First__, I have implemented the sequential (recursive) 
approach of Factorial calculation. The result you can find
in [Factorial.java](./src/main/java/com/epam/learn/multithreading/fjpfactorial/Factorial.java).  

__Thereafter__, I have moved the common code to util class
[RangeFactorial.java](./src/main/java/com/epam/learn/multithreading/fjpfactorial/RangeFactorial.java)
to reuse this code in __Factorial via FJP__ implementation.  

__Third__, I have implemented the __Factorial via FJP__.
The result you can find in [ParallelFactorial.java](./src/main/java/com/epam/learn/multithreading/fjpfactorial/ParallelFactorial.java). 
> I decide __RecursiveTask__ because I need returning result.

__Last__, I have compared both approaches. You can get in
[Main.java](./src/main/java/com/epam/learn/multithreading/fjpfactorial/Main.java)