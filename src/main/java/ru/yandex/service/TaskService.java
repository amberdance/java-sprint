package ru.yandex.service;

import ru.yandex.model.Epic;
import ru.yandex.model.Subtask;
import ru.yandex.model.Task;

import java.util.List;

public interface TaskService {

    List<Task> getTasks();

    Task getTask(int id);

    List<Epic> getEpicTasks();

    List<Subtask> getSubtasks();

    Task createTask(Task task);

    Task updateTask(Task task);

    void deleteTask(int id);

    void deleteTasks();
}
