package ru.yandex;

import ru.yandex.managers.HistoryManager;
import ru.yandex.managers.InMemoryHistoryManager;
import ru.yandex.managers.InMemoryTaskManager;
import ru.yandex.model.Epic;
import ru.yandex.model.Subtask;
import ru.yandex.model.Task;
import ru.yandex.util.SimpleIdGenerator;

public class Main {

    public static void main(String[] args) {
        var taskManager = new InMemoryTaskManager(new SimpleIdGenerator(), new InMemoryHistoryManager());

        var task1 = taskManager.createTask(new Task("task1", "description1"));
        var task2 = taskManager.createTask(new Task("task2", "description2"));
        var epic1 = taskManager.createEpic(new Epic("epic1", "descr1"));

        for (int i = 0; i < 3; i++) {
            var subtask = new Subtask(epic1.getId(), Integer.toString(i), Integer.toString(i));
            taskManager.createSubtask(subtask);
        }

        var epic2 = taskManager.createEpic(new Epic("epic2", "descr"));

        taskManager.getTask(task2.getId());
        taskManager.getTask(task1.getId());
        taskManager.getTask(task1.getId());
        taskManager.getTask(task1.getId());
        taskManager.getTask(task1.getId());
        taskManager.getEpic(epic2.getId());
        taskManager.getEpic(epic1.getId());

        System.out.println("До удаления");
        printHistory(taskManager.getHistoryManager());

        System.out.println("-".repeat(64));
        taskManager.deleteTask(task1.getId());
        taskManager.deleteEpic(epic1.getId());
        System.out.println("После удаления");
        printHistory(taskManager.getHistoryManager());

    }

    private static void printHistory(HistoryManager historyManager) {
        System.out.println(historyManager.getHistory());
    }
}
