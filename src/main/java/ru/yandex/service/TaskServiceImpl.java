package ru.yandex.service;

import lombok.RequiredArgsConstructor;
import ru.yandex.exception.TaskNotFoundException;
import ru.yandex.model.Epic;
import ru.yandex.model.Status;
import ru.yandex.model.Subtask;
import ru.yandex.model.Task;
import ru.yandex.repository.TaskRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTask(int id) {
        return taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);
    }

    @Override
    public List<Epic> getEpicTasks() {
        return filterByClass(Epic.class);
    }

    @Override
    public List<Subtask> getSubtasks() {
        return filterByClass(Subtask.class);
    }

    @Override
    public Task createTask(Task task) {
        return taskRepository.create(task);
    }

    @Override
    public Task updateTask(Task task) {
        return taskRepository.update(task);
    }

    @Override
    public void deleteTask(int id) {
        taskRepository.delete(id);
    }

    @Override
    public void deleteTasks() {
        taskRepository.deleteBatch();
    }

    @Override
    public void updateStatus(Task task, Status status) {
        task.setStatus(status);
        taskRepository.update(task);
    }

    @SuppressWarnings("unchecked")
    private <T extends Task> List<T> filterByClass(Class<? extends Task> clasz) {
        return (List<T>) taskRepository.findAll().stream().filter(clasz::isInstance).collect(Collectors.toList());
    }
}
