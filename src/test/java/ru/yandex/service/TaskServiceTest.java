package ru.yandex.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.exception.TaskNotFoundException;
import ru.yandex.model.Epic;
import ru.yandex.model.Subtask;
import ru.yandex.model.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.yandex.model.Task.Status;

class TaskServiceTest extends AbstractServiceTest {

    private static TaskService taskService;


    @BeforeAll
    static void setUp() {
        taskService = new TaskServiceImpl(taskRepository, epicRepository, subtaskRepository);
    }

    @BeforeEach
    void beforeEach() {
        dataSource.clear();

        for (int i = 1; i <= TASKS_TO_CREATE_COUNT; i++) {
            taskService.createTask(new Task(NAME_PREFIX + i, "Description_" + i));
        }
    }

    @Test
    @DisplayName("Количество возвращенных задач == количеству задач в хранилище")
    void getTasks() {
        assertEquals(TASKS_TO_CREATE_COUNT, taskService.getTasks().size());
    }

    @Test
    @DisplayName("Должен вернуть таску по заданному существующему id")
    void getTask() {
        var task = taskService.getTask(TASKS_TO_CREATE_COUNT);

        assertNotNull(task);
        assertEquals(TASKS_TO_CREATE_COUNT, task.getId());
        assertEquals(NAME_PREFIX + TASKS_TO_CREATE_COUNT, task.getName());
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

        assertEquals(TASKS_TO_CREATE_COUNT + 1, task.getId());
        assertEquals(TASKS_TO_CREATE_COUNT + 1, taskService.getTasks().size());
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

        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getDescription(), updatedTask.getDescription());
        assertEquals(Status.IN_PROGRESS, updatedTask.getStatus());
    }

    @Test
    @DisplayName("Должен удалять таску по id")
    void deleteTask() {
        assertEquals(TASKS_TO_CREATE_COUNT, taskService.getTasks().size());
        taskService.deleteTask(1);
        assertEquals(TASKS_TO_CREATE_COUNT - 1, taskService.getTasks().size());
    }

    @Test
    @DisplayName("Должен удалять все таски")
    void deleteTasks() {
        assertEquals(TASKS_TO_CREATE_COUNT, taskService.getTasks().size());
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
        assertEquals(1, taskService.getEpics().size());
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

        assertEquals(Status.NEW, task.getStatus());
        taskService.updateStatus(task, Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, task.getStatus());
    }
}
