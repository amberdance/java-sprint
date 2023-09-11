package ru.yandex.repository;

import ru.yandex.model.Task;

import java.util.List;

public interface TaskRepository {

    List<Task> findAll();

    Task findById(int id);

    Task create(Task task);

    Task update(Task task);

    void delete(int id);

    void deleteBatch();
}
