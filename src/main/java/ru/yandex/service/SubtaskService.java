package ru.yandex.service;

import ru.yandex.model.Subtask;

import java.util.List;

public interface SubtaskService {

    List<Subtask> getSubtasks();

    Subtask getSubtask(int id);

    Subtask createSubtask(Subtask subtask);

    Subtask updateSubtask(Subtask subtask);

    void deleteSubtask(int id);

    void deleteSubtasks();

}
