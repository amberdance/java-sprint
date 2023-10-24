package ru.yandex.managers;

import ru.yandex.model.Epic;
import ru.yandex.model.Subtask;
import ru.yandex.model.Task;
import ru.yandex.util.IdGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        addToHistory(task);

        return task;
    }

    @Override
    public Epic getEpic(int id) {
        var epic = epics.get(id);
        addToHistory(epic);

        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        var subtask = subtasks.get(id);
        addToHistory(subtask);

        return subtask;
    }

    private void addToHistory(Task task) {
        if (task != null) {
            historyManager.addTask(task);
        }
    }

    @Override
    public List<Subtask> getSubtasksByEpicId(int epicId) {
        return epics.get(epicId).getSubtasks();
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
        epic.getSubtasks().add(subtask);

        return subtask;
    }

    private void updateEpicStatus(Epic epic) {
        var subtasks = getSubtasksByEpicId(epic.getId());
        var hasAllSubtasksStatusNew =
                epic.getSubtasks().isEmpty() || subtasks.stream().allMatch(s -> s.getStatus().equals(Task.Status.NEW));
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
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        var epic = epics.get(id);
        var epicSubtasks = epic.getSubtasks();

        epicSubtasks.forEach(st -> historyManager.remove(st.getId()));
        epicSubtasks.clear();

        epics.remove(id);
        historyManager.remove(id);

    }

    @Override
    public void deleteSubtask(int id) {
        var epic = epics.get(subtasks.get(id).getEpicId());
        var subtask = subtasks.get(id);

        epic.getSubtasks().remove(subtask);
        subtasks.remove(id);
        updateEpicStatus(epic);
        historyManager.remove(id);
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
            e.getSubtasks().clear();
            e.setStatus(Task.Status.NEW);
        });
    }

}
