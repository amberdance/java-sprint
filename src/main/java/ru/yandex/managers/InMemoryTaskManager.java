package ru.yandex.managers;

import ru.yandex.model.Epic;
import ru.yandex.model.Subtask;
import ru.yandex.model.Task;
import ru.yandex.util.IdGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class InMemoryTaskManager implements TaskManager {

    private final IdGenerator idGenerator;
    private final HistoryManager historyManager;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();

    public InMemoryTaskManager(IdGenerator idGenerator, HistoryManager historyManager) {
        this.idGenerator = idGenerator;
        this.historyManager = historyManager;
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public Task getTask(int id) {
        var task = tasks.get(id);
        historyManager.addTask(task);

        return task;
    }

    @Override
    public Epic getEpic(int id) {
        var epic = epics.get(id);
        historyManager.addTask(epic);

        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        var subtask = subtasks.get(id);
        historyManager.addTask(subtask);

        return subtask;
    }

    @Override
    public List<Subtask> getSubtasksByEpicId(int epicId) {
        return epics.get(epicId).getSubtaskIds()
                .stream()
                .map(subtasks::get)
                .collect(Collectors.toList());
    }

    @Override
    public Task createTask(Task task) {
        var id = idGenerator.generateId();
        task.setId(id);
        tasks.put(id, task);

        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        var id = idGenerator.generateId();
        epic.setId(id);
        epics.put(id, epic);

        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        int id = idGenerator.generateId();
        subtask.setId(id);
        subtasks.put(id, subtask);

        var epic = epics.get(subtask.getEpicId());
        updateEpicStatus(epic);
        epic.getSubtaskIds().add(id);

        return subtask;
    }

    private void updateEpicStatus(Epic epic) {
        var subtasks = getSubtasksByEpicId(epic.getId());
        var hasAllSubtasksStatusNew = epic.getSubtaskIds().isEmpty() || subtasks.stream().allMatch(s -> s.getStatus().equals(Task.Status.NEW));
        var hasAllSubtasksStatusDone = subtasks.stream().anyMatch(s -> s.getStatus().equals(Task.Status.DONE));

        if (hasAllSubtasksStatusNew) {
            epic.setStatus(Task.Status.NEW);
        } else if (hasAllSubtasksStatusDone) {
            epic.setStatus(Task.Status.DONE);
        } else {
            epic.setStatus(Task.Status.IN_PROGRESS);
        }
    }

    @Override
    public Task updateTask(Task taskToUpdate) {
        var task = tasks.get(taskToUpdate.getId());

        task.setName(taskToUpdate.getName());
        task.setDescription(taskToUpdate.getDescription());
        task.setStatus(taskToUpdate.getStatus());

        return task;
    }

    @Override
    public Epic updateEpic(Epic epicToUpdate) {
        var epic = epics.get(epicToUpdate.getId());

        epic.setName(epicToUpdate.getName());
        epic.setDescription(epicToUpdate.getDescription());

        return epic;
    }

    @Override
    public Subtask updateSubtask(Subtask subtaskToUpdate) {
        var subtask = subtasks.get(subtaskToUpdate.getId());

        if (subtask == null) {
            return null;
        }

        subtask.setName(subtaskToUpdate.getName());
        subtask.setDescription(subtaskToUpdate.getDescription());
        subtask.setStatus(subtaskToUpdate.getStatus());

        var epic = epics.get(subtaskToUpdate.getEpicId());
        updateEpicStatus(epic);

        return subtask;
    }

    @Override
    public void deleteTask(int id) {
        var epic = epics.get(subtasks.get(id).getEpicId());
        updateEpicStatus(epic);

        tasks.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        var epic = epics.get(id);
        epic.getSubtaskIds().forEach(subtasks::remove);
        epics.remove(id);
    }

    @Override
    public void deleteSubtask(int id) {
        var epic = epics.get(subtasks.get(id).getEpicId());
        epic.getSubtaskIds().remove(id);
        subtasks.remove(id);
        updateEpicStatus(epic);
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        subtasks.clear();
        epics.clear();
    }

    @Override
    public void deleteSubtasks() {
        subtasks.clear();
        epics.values().forEach(e -> {
            e.getSubtaskIds().clear();
            e.setStatus(Task.Status.NEW);
        });
    }

}
