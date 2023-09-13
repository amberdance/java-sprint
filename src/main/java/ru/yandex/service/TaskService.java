package ru.yandex.service;

import ru.yandex.model.Status;
import ru.yandex.model.Task;

import java.util.List;

public interface TaskService extends EpicService, SubtaskService {

    List<Task> getTasks();

    Task getTask(int id);

    Task createTask(Task task);

    Task updateTask(Task task);

    void deleteTask(int id);

    void deleteTasks();

    void updateStatus(Task task, Status status);
}
