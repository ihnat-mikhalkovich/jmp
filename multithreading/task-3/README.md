# Task 3
___
Below you can find original task with my small notes.
1. <mark>Done</mark> - it means that this task point has successfully passed.
1. My small descriptions you with see in boxes as below
   > I'm the box
___
## Task description
Please, complete the following task: __File Scanner via FJP__

Create CLI application that scans a specified folder and provides detailed statistics:

1. <mark>Done</mark>: File count.
1. <mark>Done</mark>: Folder count.
1. <mark>Done</mark>: Size (sum of all files size) (similar to Windows context menu Properties). Since the folder may contain a huge number of files the scanning process should be executed in a separate thread displaying an informational message with some simple animation like the progress bar in CLI (up to you, but I'd like to see that task is in progress).

<mark>Done, the ability to interrupt listening Enter</mark>. Once the task is done, the statistics should be displayed in the output immediately. Additionally, there should be the ability to interrupt the process by pressing some reserved key (for instance c). Of course, use Fork-Join Framework for implementation parallel scanning.

> In the implementation in [FilesAndDirectoriesCounter.java](./src/main/java/com/epam/learn/multithreading/filescanner/FilesAndDirectoriesCounter.java)
> and [PathSizeScanner.java](./src/main/java/com/epam/learn/multithreading/filescanner/PathSizeScanner.java)
> you can find something like this __Collections.reverse(forkJoinTasks)__. I have added this
> because when I do _invokeAll(forkJoinTasks)_ tasks are invoking from end to start and
> this means that the last tasks will be ready earlier than first (this
> is true because ForkJoinPool has the taskQueue task queue is LIFO). 
> So I have decided to reverse the _forkJoinTasks_ list because in multithreading world
> the reverse operation will and before the first task 
> will solve, I hope :)

> Tests for the app you can find in [FileScannerTest.java](./src/test/java/com/epam/learn/multithreading/filescanner/FileScannerTest.java).
> There I create 3 filesystems consequently (windows, osX, linux)
> and test my ForkJoin solution in-memory.