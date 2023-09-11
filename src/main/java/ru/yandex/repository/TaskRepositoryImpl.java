package ru.yandex.repository;

import ru.yandex.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TaskRepositoryImpl implements TaskRepository {

    private int id = 1;
    private final List<Task> taskStorage = new ArrayList<>();

    @Override
    public List<Task> findAll() {
        return Collections.unmodifiableList(taskStorage);
    }

    @Override
    public Optional<Task> findById(int id) {
        return taskStorage.stream().filter(t -> t.getId() == id).findFirst();
    }

    @Override
    public Task create(Task task) {
        try {
            task.setId(id++);
            taskStorage.add(id, task);
        } catch (Exception e) {
            id--;
        }

        return task;
    }

    @Override
    public Task update(Task task) {
        var taskOptional = findById(task.getId());

        if (taskOptional.isPresent()) {
            var taskToUpdate = taskOptional.get();
            taskToUpdate.setName(task.getName());
            taskToUpdate.setDescription(task.getDescription());

            taskStorage.set(id, taskToUpdate);

            return taskToUpdate;
        } else {
            return create(task);
        }
    }

    @Override
    public void delete(int id) {
        findById(id).ifPresent(taskStorage::remove);
    }

    @Override
    public void deleteBatch() {
        taskStorage.clear();
    }

}
