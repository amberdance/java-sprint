package ru.yandex.service;

import ru.yandex.model.Subtask;

import java.util.List;

public interface SubtaskService {

    List<Subtask> getSubtasks();

    Subtask getSubtask(int id);

    Subtask createsubtask(Subtask subtask);

    Subtask updatesubtask(Subtask subtask);

    void deleteSubtask(int id);

    void deleteSubtasks();

}
