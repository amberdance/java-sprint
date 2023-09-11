package ru.yandex.repository;

import ru.yandex.exception.TaskNotFoundException;
import ru.yandex.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskRepositoryImpl implements TaskRepository {

    private int id = 1;
    private final List<Task> taskStorage = new ArrayList<>();

    @Override
    public List<Task> findAll() {
        return taskStorage;
    }

    @Override
    public Task findById(int id) {
        return taskStorage.stream().filter(t -> t.getId() == id).findFirst().orElseThrow(TaskNotFoundException::new);
    }

    @Override
    public Task create(Task task) {
        task.setId(++id);
        taskStorage.add(id, task);

        return task;
    }

    @Override
    public Task update(Task task) {
        var taskToUpdate = findById(task.getId());
        taskToUpdate.setName(task.getName());
        taskToUpdate.setDescription(task.getDescription());

        return create(taskToUpdate);
    }

    @Override
    public void delete(int id) {
        taskStorage.remove(findById(id));
    }

    @Override
    public void deleteBatch() {
        taskStorage.clear();
    }

}
