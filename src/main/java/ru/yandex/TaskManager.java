package ru.yandex;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.yandex.util.IdGenerator;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class TaskManager {

    private final IdGenerator idGenerator;
    private final Map<Integer, Task> tasks;
    private final Map<Integer, Epic> epics;
    private final Map<Integer, Subtask> subtasks;


    public Task getTask(int id) {
        return Objects.requireNonNull(tasks.get(id));
    }

    public Epic getEpic(int id) {
        return Objects.requireNonNull(epics.get(id));
    }

    public Subtask getSubtask(int id) {
        return Objects.requireNonNull(subtasks.get(id));
    }

    public List<Subtask> getSubtasksByEpicId(int epicId) {
        return subtasks.values()
                .stream()
                .filter(s -> s.getEpicId() == epicId)
                .collect(Collectors.toList());
    }

    public Task createTask(Task task) {
        tasks.put(idGenerator.generateId(), task);

        return task;
    }

    public Epic createEpic(Epic epic) {
        epics.put(idGenerator.generateId(), epic);

        return epic;
    }


    public Subtask createSubtask(Subtask subtask) {
        subtasks.put(idGenerator.generateId(), subtask);

        return subtask;
    }


    public Task updateTask(Task taskToUpdate) {
        var task = Objects.requireNonNull(tasks.get(taskToUpdate.getId()));

        task.setName(taskToUpdate.getName());
        task.setDescription(taskToUpdate.getDescription());
        task.setStatus(taskToUpdate.getStatus());

        return task;
    }

    public Epic updateEpic(Epic epicToUpdate) {
        var epic = Objects.requireNonNull(epics.get(epicToUpdate.getId()));

        epic.setName(epicToUpdate.getName());
        epic.setDescription(epicToUpdate.getDescription());
        epic.setStatus(epicToUpdate.getStatus());

        var hasAllSubtasksStatusDone = getSubtasksByEpicId(epic.getId()).stream()
                .allMatch(s -> s.getStatus().equals(Task.Status.DONE));

        if (hasAllSubtasksStatusDone) {
            subtasks.values().forEach(s -> s.setStatus(Task.Status.DONE));
        }

        return epic;
    }

    public Subtask updateSubtask(Subtask subtaskToUpdate) {
        var subtask = Objects.requireNonNull(subtasks.get(subtaskToUpdate.getId()));

        subtask.setName(subtaskToUpdate.getName());
        subtask.setDescription(subtaskToUpdate.getDescription());
        subtask.setStatus(subtaskToUpdate.getStatus());

        if (subtaskToUpdate.getStatus().equals(Task.Status.IN_PROGRESS)) {
            var epic = Objects.requireNonNull(epics.get(subtaskToUpdate.getEpicId()));
            epic.setStatus(Task.Status.IN_PROGRESS);
        }

        return subtask;
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteEpic(int id) {
        epics.remove(id);

        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == id) {
                subtasks.remove(id);
            }
        }
    }

    public void deleteSubTask(int id) {
        subtasks.remove(id);
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteEpics() {
        for (Integer id : epics.keySet()) {
            if (id != null) {
                subtasks.remove(id);
            }
        }

        epics.clear();
    }

    public void deleteSubtasks() {
        subtasks.clear();
    }

}
