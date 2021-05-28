# Task 2

## Task description
Please, complete the following task: __Multithreading Sorting via FJP__
- Implement Merge Sort or Quick Sort algorithm that sorts a huge array of integers in parallel using Fork/Join framework.

In this task I decide __Merge Sort__.

## Solution
__First__, I have implemented the sequential (recursive)
approach of Merge Sort. The result you can find
in [MergeSort.java](./src/main/java/com/epam/learn/multithreading/mergesort/MergeSort.java).  
Tests you can find in [MergeSortTest.java](./src/test/java/com/epam/learn/multithreading/mergesort/MergeSortTest.java). 

__Thereafter__, I have marked method _merge(...)_ and _swap(...)_ 
in [MergeSort.java](./src/main/java/com/epam/learn/multithreading/mergesort/MergeSort.java) 
to reuse the common code in the implementation with __FJP__.  

__Third__, I have implemented the __Merge Sort via FJP RecursiveAction__.
The result you can find in [ParallelMergeSort.java](./src/main/java/com/epam/learn/multithreading/mergesort/ParallelMergeSort.java).  
Tests you can find in [ParallelMergeSortTest.java](./src/test/java/com/epam/learn/multithreading/mergesort/ParallelMergeSortTest.java)
> I decide __RecursiveAction__ because I don't need returning result.

__Next__, after watching [Алексей Шипилёв — ForkJoinPool в Java 8](https://www.youtube.com/watch?v=t0dGLFtRR9c&t=4967s)
video where the speaker have mentioned __CountedCompleter__ and described it as something that is more powerful 
than other implementations of __ForkJoinTasks__ I have just tried dive deeper 
and think about this and found this sentence in java docs of __CountedCompleter__: _"CountedCompleters are in general more robust in the presence of subtask stalls and blockage than are other forms of ForkJoinTasks, but are less intuitive to program."_  
> _Just small hint_. In video also told about using ForkJoinPool. 
> In video mentioned that using __commonPool__ is faster than newly created,
> because __commonPool__ is a system pool and as a result spend fewer amount 
> of time to sleep and as a result we win something about 100 milliseconds.

> _Just small hint_. Also in a video the speaker touched the __False sharing__ problem 
> and the annotation __@sun.misc.Contended__ that helps to avoid this.

> Unfortunately, I didn't find the place where
> __@sun.misc.Contended__ annotation is good to use
> in my solution. May be put it above the array to 
> sort But that is another story :)

So, I decide to implement the __Merge Sort via FJP CountedCompleter__.
The result you can find in [CountedParallelMergeSort.java](./src/main/java/com/epam/learn/multithreading/mergesort/CountedParallelMergeSort.java).  
Tests you can find in [CountedParallelMergeSortTest.java](./src/test/java/com/epam/learn/multithreading/mergesort/CountedParallelMergeSortTest.java)
