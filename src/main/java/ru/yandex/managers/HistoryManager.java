package ru.yandex.managers;

import ru.yandex.model.Task;

import java.util.List;

public interface HistoryManager {

    void addTask(Task task);

    void remove(int id);

    List<Task> getHistory();

}
