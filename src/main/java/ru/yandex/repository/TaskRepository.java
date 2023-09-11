package ru.yandex.repository;

import ru.yandex.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    List<Task> findAll();

    Optional<Task> findById(int id);

    Task create(Task task);

    Task update(Task task);

    void delete(int id);

    void deleteBatch();
}
