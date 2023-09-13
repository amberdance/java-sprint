package ru.yandex.service;

import lombok.RequiredArgsConstructor;
import ru.yandex.exception.TaskNotFoundException;
import ru.yandex.model.Epic;
import ru.yandex.model.Status;
import ru.yandex.model.Subtask;
import ru.yandex.model.Task;
import ru.yandex.repository.TaskRepository;

import java.util.List;

@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository<Task> taskRepository;
    private final TaskRepository<Epic> epicRepository;
    private final TaskRepository<Subtask> subtaskRepository;

    @Override
    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    @Override
    public List<Epic> getEpics() {
        return epicRepository.findAll();
    }

    @Override
    public List<Subtask> getSubtasks() {
        return subtaskRepository.findAll();
    }

    @Override
    public Task getTask(int id) {
        return taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);
    }

    @Override
    public Epic getEpic(int id) {
        return null;
    }

    @Override
    public Subtask getSubtask(int id) {
        return null;
    }

    @Override
    public Task createTask(Task task) {
        return taskRepository.create(task);
    }

    @Override
    public Epic createEpic(Epic epic) {
        return null;
    }

    @Override
    public Subtask createsubtask(Subtask subtask) {
        return null;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        return null;
    }

    @Override
    public Task updateTask(Task task) {
        return taskRepository.update(task);
    }

    @Override
    public Subtask updatesubtask(Subtask subtask) {
        return null;
    }

    @Override
    public void deleteTask(int id) {
        taskRepository.delete(id);
    }

    @Override
    public void deleteEpic(int id) {

    }

    @Override
    public void deleteSubtask(int id) {

    }

    @Override
    public void deleteTasks() {
        taskRepository.deleteBatch();
    }

    @Override
    public void deleteEpics() {

    }

    @Override
    public void deleteSubtasks() {

    }

    @Override
    public void updateStatus(Task task, Status status) {
        task.setStatus(status);
        taskRepository.update(task);
    }

}
