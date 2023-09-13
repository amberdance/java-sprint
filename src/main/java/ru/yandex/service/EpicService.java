package ru.yandex.service;

import ru.yandex.model.Epic;

import java.util.List;

public interface EpicService {
    List<Epic> getEpics();

    Epic getEpic(int id);

    Epic createEpic(Epic epic);

    Epic updateEpic(Epic epic);

    void deleteEpic(int id);

    void deleteEpics();
}
