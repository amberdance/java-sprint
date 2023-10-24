package ru.yandex.managers;

import ru.yandex.model.Task;

import java.util.Queue;

public interface HistoryManager {

    void addTask(Task task);

    void remove(int id);

    Queue<Task> getHistory();

}
