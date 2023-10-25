package ru.yandex.managers;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import ru.yandex.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class InMemoryHistoryManager implements HistoryManager {

    private final Queue<Task> history = new CircularFifoQueue<>();

    @Override
    public void addTask(Task task) {
        if (history.contains(task)) {
            remove(task);
        }

        history.add(task);
    }

    @Override
    public void remove(Task task) {
        // Вообщем, да, с текущим классом при данной реализации не добиться О(1)
        history.remove(task);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
