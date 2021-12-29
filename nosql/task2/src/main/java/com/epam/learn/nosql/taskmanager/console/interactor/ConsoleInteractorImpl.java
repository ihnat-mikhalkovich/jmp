package com.epam.learn.nosql.taskmanager.console.interactor;

import com.epam.learn.nosql.taskmanager.console.action.ConsoleAction;
import com.epam.learn.nosql.taskmanager.console.action.ConsoleActionDirector;
import com.epam.learn.nosql.taskmanager.console.io.InputCatcher;
import com.epam.learn.nosql.taskmanager.console.io.OutputPitcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsoleInteractorImpl implements ConsoleInteractor {

    private final ConsoleActionDirector actionDirector;
    private final InputCatcher inputCatcher;
    private final OutputPitcher outputPitcher;

    private static void printActions() {
        System.out.println("1. Show all tasks");
        System.out.println("2. Show overdue tasks");
        System.out.println("3. Show tasks with specific category");
        System.out.println("4. Show all subtasks of tasks with specific category");
        System.out.println("5. Add task");
        System.out.println("6. Update task");
        System.out.println("7. Delete task");
        System.out.println("8. Add subtasks");
        System.out.println("9. Delete subtasks");
        System.out.println("10. Full-text search by word");
    }

    @Override
    public void start() {
        boolean isContinue;
        do {
            actionDirector.forEach((id, action) ->
                    outputPitcher.send(id + ". " + action.getDescription())
            );
            final int id = inputCatcher.nextInt();

            final ConsoleAction action = actionDirector.getById(id);
            isContinue = action.act();
        } while (isContinue);
    }
}
