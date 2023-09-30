package ru.yandex.util;

import lombok.experimental.UtilityClass;
import ru.yandex.managers.HistoryManager;
import ru.yandex.managers.InMemoryHistoryManager;
import ru.yandex.managers.InMemoryTaskManager;
import ru.yandex.managers.TaskManager;

@UtilityClass
public class Managers {


    public static TaskManager getDefault() {
        IdGenerator idGenerator = new SimpleIdGenerator();
        HistoryManager history = Managers.getDefaultHistory();

        return new InMemoryTaskManager(idGenerator, history);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
