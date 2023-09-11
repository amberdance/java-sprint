package ru.yandex;

import ru.yandex.model.Epic;
import ru.yandex.model.Subtask;
import ru.yandex.model.Task;

public class Main {
    public static void main(String[] args) {
        var task = new Task("taskname", "taskdescr");
        var epic = new Epic("epicname", "epicdescr");
        var subtask = new Subtask("subtaskname", "subtaskdescr");

        System.out.println(task);
        System.out.println(epic);
        System.out.println(subtask);
    }
}
