package ru.yandex.managers;

import ru.yandex.model.Task;

import java.util.Queue;

public interface HistoryManager {

    boolean addTask(Task task);

    Queue<Task> getHistory();

}
