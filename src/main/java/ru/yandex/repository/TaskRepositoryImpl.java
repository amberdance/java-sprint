package ru.yandex.repository;

import lombok.RequiredArgsConstructor;
import ru.yandex.model.Task;
import ru.yandex.utils.IdGenerator;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository<Task> {

    private final List<Task> taskStorage;
    private final IdGenerator idGenerator;
    private final Predicate<Task> taskFilter = task -> task.getClass().equals(Task.class);

    @Override
    public List<Task> findAll() {
        return taskStorage.stream()
                .filter(taskFilter)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Optional<Task> findById(int id) {
        return taskStorage.stream()
                .filter(t -> t.getId() == id && taskFilter.test(t))
                .findFirst();
    }

    @Override
    public Task create(Task task) {
        if (!taskFilter.test(task)) {
            return task;
        }

        task.setId(idGenerator.generateId());
        taskStorage.add(task);

        return task;
    }

    @Override
    public Task update(Task task) {
        if (!taskFilter.test(task)) {
            return task;
        }

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
        taskStorage.removeAll(findAll());
    }

}
