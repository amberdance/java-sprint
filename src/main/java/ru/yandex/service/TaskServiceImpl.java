package ru.yandex.service;

import lombok.RequiredArgsConstructor;
import ru.yandex.exception.TaskNotFoundException;
import ru.yandex.model.Epic;
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
        return epicRepository.findById(id).orElseThrow(TaskNotFoundException::new);
    }

    @Override
    public Subtask getSubtask(int id) {
        return subtaskRepository.findById(id).orElseThrow(TaskNotFoundException::new);
    }

    @Override
    public Task createTask(Task task) {
        return taskRepository.create(task);
    }

    @Override
    public Epic createEpic(Epic epicCreate) {
        var epic = epicRepository.create(epicCreate);
        var subtasks = epic.getSubtasks();

        if (subtasks.size() > 0) {
            subtasks.forEach(s -> {
                s.setEpicId(epic.getId());
                subtaskRepository.create(s);
            });
        }

        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtaskCreate) {
        var epic = epicRepository.findById(subtaskCreate.getEpicId())
                .orElseThrow(() -> new TaskNotFoundException("Epic with id " + " was not found"));

        var subtask = subtaskRepository.create(subtaskCreate);
        epic.getSubtasks().add(subtask);

        return subtask;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        var subtasks = epic.getSubtasks();
        var hasSubtasks = subtasks.size() > 0;

        if (hasSubtasks && allSubtasksDone(epic)) {
            epic.setStatus(Task.Status.DONE);
        }

        return epicRepository.update(epic);
    }

    private boolean allSubtasksDone(Epic epic) {
        return epic.getSubtasks().stream().allMatch(s -> s.getStatus().equals(Task.Status.DONE));
    }

    @Override
    public Task updateTask(Task task) {
        return taskRepository.update(task);
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        return subtaskRepository.update(subtask);
    }

    @Override
    public void deleteTask(int id) {
        taskRepository.delete(id);
    }

    @Override
    public boolean deleteEpic(int id) {
        var epicOptional = epicRepository.findById(id);

        if (epicOptional.isEmpty()) {
            return false;
        }

        var subtasks = epicOptional.get().getSubtasks();
        subtasks.forEach(s -> subtaskRepository.delete(s.getId()));
        epicRepository.delete(id);

        return true;

    }

    @Override
    public void deleteSubtask(int id) {
        subtaskRepository.delete(id);
    }

    @Override
    public void deleteTasks() {
        taskRepository.deleteBatch();
    }

    @Override
    public void deleteEpics() {
        epicRepository.findAll().forEach(e -> deleteEpic(e.getId()));
    }

    @Override
    public void deleteSubtasks() {
        subtaskRepository.deleteBatch();
    }

    @Override
    public void updateStatus(Task task, Task.Status status) {
        task.setStatus(status);
        taskRepository.update(task);
    }

}
