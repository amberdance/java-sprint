package ru.yandex.util;

import lombok.experimental.UtilityClass;
import ru.yandex.managers.HistoryManager;
import ru.yandex.managers.InMemoryHistoryManager;
import ru.yandex.managers.InMemoryTaskManager;
import ru.yandex.managers.TaskManager;
import ru.yandex.model.Epic;
import ru.yandex.model.Subtask;
import ru.yandex.model.Task;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class Managers {


    public static TaskManager getDefault() {
        IdGenerator idGenerator = new SimpleIdGenerator();
        HistoryManager history = Managers.getDefaultHistory();
        Map<Integer, Task> tasks = new HashMap<>();
        Map<Integer, Epic> epics = new HashMap<>();
        Map<Integer, Subtask> subtasks = new HashMap<>();

        return new InMemoryTaskManager(idGenerator, history, tasks, epics, subtasks);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
