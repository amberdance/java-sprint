package ru.yandex.service;

import ru.yandex.model.Task;

import java.util.List;

public interface TaskService {

    List<Task> getTasks();

    Task getTask(int id);

    Task createTask(Task task);

    Task updateTask(Task task);

    void deleteTask(int id);

    void deleteTasks();
}
