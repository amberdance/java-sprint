package ru.yandex.service;

import lombok.RequiredArgsConstructor;
import ru.yandex.model.Task;
import ru.yandex.repository.TaskRepository;

import java.util.List;

@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTask(int id) {
        return taskRepository.findById(id);
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
}
