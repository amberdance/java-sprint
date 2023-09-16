package ru.yandex;

import ru.yandex.model.Epic;
import ru.yandex.model.Subtask;
import ru.yandex.model.Task;

import java.util.HashMap;

public class Main {

    //Driver code
    public static void main(String[] args) {
        var tasks = new HashMap<Integer, Task>();
        var epics = new HashMap<Integer, Epic>();
        var subtasks = new HashMap<Integer, Subtask>();
        var taskManager = new TaskManager(tasks, epics, subtasks);
    }
}
