package ru.yandex.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.exception.TaskNotFoundException;
import ru.yandex.model.Epic;
import ru.yandex.model.Status;
import ru.yandex.model.Subtask;
import ru.yandex.model.Task;
import ru.yandex.repository.TaskRepositoryImpl;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskServiceImplTest {

    private static TaskService taskService;
    private final static int TASKS_TO_CREATE_COUNT = 5;
    private final static String NAME_PREFIX = "Name_";


    @BeforeAll
    static void setUp() {
        taskService = new TaskServiceImpl(new TaskRepositoryImpl(new ArrayList<>()));
    }

    @BeforeEach
    void beforeEach() {
        taskService.deleteTasks();

        for (int i = 1; i <= TASKS_TO_CREATE_COUNT; i++) {
            taskService.createTask(new Task(NAME_PREFIX + i, "Description_" + i));
        }
    }

    @Test
    @DisplayName("Количество возвращенных задач == количеству задач в хранилище")
    void getTasks() {
        assertEquals(taskService.getTasks().size(), TASKS_TO_CREATE_COUNT);
    }

    @Test
    @DisplayName("Должен вернуть таску по заданному существующему id")
    void getTask() {
        var task = taskService.getTask(TASKS_TO_CREATE_COUNT);

        assertNotNull(task);
        assertEquals(task.getId(), TASKS_TO_CREATE_COUNT);
        assertEquals(task.getName(), NAME_PREFIX + TASKS_TO_CREATE_COUNT);
    }

    @Test
    @DisplayName("При несуществующем id выбрасывается исключение")
    void shouldThrowsExceptionWhenTaskNotFound() {
        assertThrows(TaskNotFoundException.class, () -> taskService.getTask(999));
    }

    @Test
    @DisplayName("Должен создать новую таску и задать последующий id")
    void createTask() {
        var taskToCreate = new Task();
        taskToCreate.setName("NAME");
        taskToCreate.setDescription("DESCRIPTION");

        taskService.createTask(taskToCreate);
        var task = taskService.getTask(taskToCreate.getId());

        assertEquals(task.getId(), TASKS_TO_CREATE_COUNT + 1);
        assertEquals(taskService.getTasks().size(), TASKS_TO_CREATE_COUNT + 1);
    }

    @Test
    @DisplayName("Должен обновлять поля таски")
    void updateTask() {
        var task = taskService.getTask(TASKS_TO_CREATE_COUNT);
        task.setName("UPDATED_NAME");
        task.setDescription("UPDATED_DESCRIPTION");
        task.setStatus(Status.IN_PROGRESS);

        taskService.updateTask(task);

        var updatedTask = taskService.getTask(TASKS_TO_CREATE_COUNT);

        assertEquals(updatedTask.getName(), task.getName());
        assertEquals(updatedTask.getDescription(), task.getDescription());
        assertEquals(updatedTask.getStatus(), Status.IN_PROGRESS);
    }

    @Test
    @DisplayName("Должен удалять таску по id")
    void deleteTask() {
        assertEquals(taskService.getTasks().size(), TASKS_TO_CREATE_COUNT);
        taskService.deleteTask(1);
        assertEquals(taskService.getTasks().size(), TASKS_TO_CREATE_COUNT - 1);

    }

    @Test
    @DisplayName("Должен удалять все таски")
    void deleteTasks() {
        assertEquals(taskService.getTasks().size(), TASKS_TO_CREATE_COUNT);
        taskService.deleteTasks();
        assertTrue(taskService.getTasks().isEmpty());
    }

    @Test
    @DisplayName("Нельзя изменять список задач напрямую через List API")
    void shouldThrowsExceptionWhenAccessedDirectlyToList() {
        assertThrows(UnsupportedOperationException.class, () -> {
            taskService.getTasks().add(new Task());
            taskService.getTasks().clear();
            taskService.getTasks().remove(1);
        });
    }

    @Test
    @DisplayName("Должен возвращать только список эпиков")
    void getEpicTasks() {
        taskService.createTask(new Epic("epic", "epic"));
        assertEquals(1, taskService.getEpicTasks().size());
    }

    @Test
    @DisplayName("Должен возвращать только список подзадача")
    void getSubtasks() {
        taskService.createTask(new Subtask("subtask", "subtask"));
        assertEquals(1, taskService.getSubtasks().size());
    }

    @Test
    @DisplayName("Должен обновлять статус на указанный")
    void updateStatus() {
        var task = taskService.getTask(TASKS_TO_CREATE_COUNT);

        assertEquals(task.getStatus(), Status.NEW);
        taskService.updateStatus(task, Status.IN_PROGRESS);
        assertEquals(task.getStatus(), Status.IN_PROGRESS);
    }
}
