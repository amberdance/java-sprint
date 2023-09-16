package ru.yandex.util;

public class SimpleIdGenerator implements IdGenerator {

    private int id = 1;

    @Override
    public int getCurrentId() {
        return id;
    }

    @Override
    public int generateId() {
        return id++;
    }
}
