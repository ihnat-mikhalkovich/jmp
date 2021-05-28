# Task 4
___
Below you can find original task with my small notes.
1. <mark>Done</mark> - it means that this task point has successfully passed.
1. My small descriptions you with see in boxes as below
   > I'm the box
___

## Task description
Please, complete the following task: __Completable Future Helps to Build Open Salary Society__

Assume, we have REST endpoint that returns a list of hired Employees.
> I have created a separate project with name __open-salary__. This is
> spring boot application that provide me REST endpoint that returns a list of hired Employees. 
> This is link on controller [OpenSalarySocietyController.java](./open-salary/src/main/java/com/epam/learn/multithreading/opensalary/OpenSalarySocietyController.java)
- <mark>Done</mark>: REST endpoint is wrapped by Java service class that consuming this endpoint.
  > You can find this service in [ConsumerServiceImpl.java](./consumer/src/main/java/com/epam/learn/multithreading/consumer/ConsumerServiceImpl.java)
- <mark>Done</mark>: Fetch a list of Employee objects asynchronously by calling the hiredEmployees().
  > The code you can find in [ConsumerApplication.java](./consumer/src/main/java/com/epam/learn/multithreading/consumer/ConsumerApplication.java)
- <mark>Done</mark>: Join another CompletionStage<List> that takes care of filling the salary of each hired employee, by calling the getSalary(hiredEmployeeId) method which returns a CompletionStage that asynchronously fetches the salary (again could be consuming a REST endpoint).
  > The code you can find in [ConsumerApplication.java](./consumer/src/main/java/com/epam/learn/multithreading/consumer/ConsumerApplication.java)
- <mark>Done</mark>: When all Employee objects are filled with their salaries, we end up with a List<CompletionStage>, so we call <special operation on CF> to get a final stage that completes upon completion of all these stages.
  > The code you can find in [ConsumerApplication.java](./consumer/src/main/java/com/epam/learn/multithreading/consumer/ConsumerApplication.java)
- <mark>Done</mark>: Print hired Employees with their salaries via <special operation on CF> on the final stage.
  > The code you can find in [ConsumerApplication.java](./consumer/src/main/java/com/epam/learn/multithreading/consumer/ConsumerApplication.java)

Provide correct solution with CF usage and use appropriate CF operators instead <special operation on CF>.  

Why does the CF usage improve performance here in comparison with the synchronous approach? Discuss it with a mentor.
> CF usage improve performance because under the hood the code wrapping in a task
> for ExecutorService and running asynchronously therefore we efficiently use 
> computer hardware.

How thread waiting is implemented in a synchronous world?
> If the program wait something I guess that it is like thread sleeps until
> is it woken up.