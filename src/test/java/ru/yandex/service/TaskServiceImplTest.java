package ru.yandex.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.model.Task;
import ru.yandex.repository.TaskRepositoryImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskServiceImplTest {

    private static TaskService taskService;
    private final static int TASKS_TO_CREATE_COUNT = 5;


    @BeforeAll
    static void setUp() {
        taskService = new TaskServiceImpl(new TaskRepositoryImpl());

        for (int i = 1; i <= TASKS_TO_CREATE_COUNT; i++) {
            taskService.createTask(new Task("Name_" + i, "Description_" + i));
        }
    }

    @Test
    void getTasks() {
        assertEquals(taskService.getTasks().size(), TASKS_TO_CREATE_COUNT);
    }

    @Test
    void getTask() {
    }

    @Test
    void createTask() {
    }

    @Test
    void updateTask() {
    }

    @Test
    void deleteTask() {
    }

    @Test
    void deleteTasks() {
    }
}
