package ru.yandex.managers;

import ru.yandex.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    static class Node<Task> {
        private Task task;
        private Node<Task> next;
        private Node<Task> prev;

        Node(Node<Task> prev, Node<Task> next, Task task) {
            this.prev = prev;
            this.next = next;
            this.task = task;
        }
    }

    private final Map<Integer, Node<Task>> history = new HashMap<>();
    private Node<Task> tail;
    private Node<Task> head;

    @Override
    public void addTask(Task task) {
        if (task != null) {
            remove(task.getId());
            linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        removeNode(history.get(id));
    }

    @Override
    public List<Task> getHistory() {
        var result = new ArrayList<Task>();
        var current = head;

        while (current != null) {
            result.add(current.task);
            current = current.next;
        }

        return result;
    }

    private void linkLast(Task task) {
        var prev = tail;
        var next = new Node<>(prev, null, task);

        tail = next;

        history.put(task.getId(), next);

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
        }
    }

    private void removeNode(Node<Task> node) {
        if (node == null) {
            return;
        }

        var next = node.next;
        var prev = node.prev;

        node.task = null;

        if (head == node && tail == node) {
            head = null;
            tail = null;
        } else if (head == node) {
            head = next;
            head.prev = null;
        } else if (tail == node) {
            tail = prev;
            tail.next = null;
        } else {
            prev.next = next;
            next.prev = prev;
        }
    }
}
