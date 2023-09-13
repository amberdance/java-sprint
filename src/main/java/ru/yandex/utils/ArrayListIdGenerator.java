package ru.yandex.utils;

import lombok.RequiredArgsConstructor;
import ru.yandex.model.Task;

import java.util.List;

@RequiredArgsConstructor
public class ArrayListIdGenerator implements IdGenerator {

    private final List<Task> dataSource;

    @Override
    public int generateId() {
        try {
            return dataSource.get(dataSource.size() - 1).getId() + 1;
        } catch (NullPointerException | IndexOutOfBoundsException ignored) {
            return 1;
        }
    }

}
