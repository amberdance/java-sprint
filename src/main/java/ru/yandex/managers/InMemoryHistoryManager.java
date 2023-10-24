package ru.yandex.managers;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import ru.yandex.model.Task;

import java.util.Queue;

public class InMemoryHistoryManager implements HistoryManager {

    private final Queue<Task> history = new CircularFifoQueue<>();

    @Override
    public void addTask(Task task) {
        var lastTask = history.peek();

        if (lastTask == null || !lastTask.equals(task)) {
            history.add(task);
        }
    }

    @Override
    public void remove(int id) {
        try {
            history.removeIf(task -> task.getId() == id);
        } catch (NullPointerException ignored) {
        }
    }

    @Override
    public Queue<Task> getHistory() {
        return history;
    }
}
