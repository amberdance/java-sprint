package ru.yandex.managers;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import ru.yandex.model.Task;

import java.util.Queue;

@RequiredArgsConstructor
public class InMemoryHistoryManager implements HistoryManager {
    public static final short HISTORY_CAPACITY = 10;
    private final Queue<Task> history = new CircularFifoQueue<>(HISTORY_CAPACITY);

    @Override
    public boolean addTask(Task task) {
        return history.add(task);
    }

    @Override
    public Queue<Task> getHistory() {
        return history;
    }
}
