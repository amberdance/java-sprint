package ru.yandex.repository;

import lombok.RequiredArgsConstructor;
import ru.yandex.model.Task;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository<Task> {

    private final List<Task> taskStorage;

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
        int id = getIncrementedId();
        task.setId(id);
        taskStorage.add(task);

        return task;
    }

    @Override
    public Task update(Task task) {
        var taskOptional = findById(task.getId());

        if (taskOptional.isPresent()) {
            var taskToUpdate = taskOptional.get();
            taskToUpdate.setName(task.getName());
            taskToUpdate.setDescription(task.getDescription());
            taskToUpdate.setStatus(task.getStatus());

            taskStorage.set(task.getId() - 1, taskToUpdate);

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

    private int getIncrementedId() {
        try {
            return taskStorage.get(taskStorage.size() - 1).getId() + 1;
        } catch (NullPointerException | IndexOutOfBoundsException ignored) {
            return 1;
        }
    }

}
