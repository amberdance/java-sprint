package ru.yandex;

import ru.yandex.model.Task;

public class Main {
    public static void main(String[] args) {
        var task = Task.builder().id(1).description("descr").name("name").build();

        System.out.println(task);
    }
}
