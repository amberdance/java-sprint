package ru.yandex.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.exception.TaskNotFoundException;
import ru.yandex.model.Epic;
import ru.yandex.model.Subtask;
import ru.yandex.model.Task;

import static org.junit.jupiter.api.Assertions.*;
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

        for (int i = 1; i <= COUNT_OF_TASKS; i++) {
            taskService.createTask(new Task(NAME_PREFIX + i, "Description_" + i));
            var epic = new Epic();
            taskService.createEpic(epic);
            taskService.createSubtask(new Subtask(epic.getId()));
        }
    }

    @Test
    @DisplayName("Количество возвращенных задач == количеству задач в хранилище")
    void getTasks() {
        assertEquals(COUNT_OF_TASKS, taskService.getTasks().size());
    }

    @Test
    @DisplayName("Должен вернуть задачу по заданному id")
    void getTask() {
        var task = taskService.getTask(1);

        assertEquals(1, task.getId());
        assertEquals(NAME_PREFIX + 1, task.getName());
    }

    @Test
    @DisplayName("При несуществующем id выбрасывается исключение")
    void shouldThrowsExceptionWhenTaskNotFound() {
        assertThrows(TaskNotFoundException.class, () -> taskService.getTask(COUNT_OF_TASKS));
    }

    @Test
    @DisplayName("Должен создать новую задачу и задать последующий id")
    void createTask() {
        var taskToCreate = new Task();
        taskToCreate.setName("NAME");
        taskToCreate.setDescription("DESCRIPTION");

        taskService.createTask(taskToCreate);
        var task = taskService.getTask(taskToCreate.getId());

        assertTrue(task.getId() > COUNT_OF_TASKS);
        assertEquals(COUNT_OF_TASKS + 1, taskService.getTasks().size());
    }

    @Test
    @DisplayName("Должен обновлять поля задачии")
    void updateTask() {
        var task = taskService.getTask(1);
        task.setName("UPDATED_NAME");
        task.setDescription("UPDATED_DESCRIPTION");
        task.setStatus(Status.IN_PROGRESS);

        taskService.updateTask(task);

        var updatedTask = taskService.getTask(1);

        assertEquals(task.getName(), updatedTask.getName());
        assertEquals(task.getDescription(), updatedTask.getDescription());
        assertEquals(Status.IN_PROGRESS, updatedTask.getStatus());
    }

    @Test
    @DisplayName("Должен удалять задачу по id")
    void deleteTask() {
        assertEquals(COUNT_OF_TASKS, taskService.getTasks().size());
        taskService.deleteTask(1);
        assertEquals(COUNT_OF_TASKS - 1, taskService.getTasks().size());
    }

    @Test
    @DisplayName("Если задан id не задачи, а наследника, то ничего не удаляется")
    void shouldThrewExceptionWhenIdNotExists() {
        var countBefore = dataSource.size();
        taskService.deleteTask(COUNT_OF_TASKS + 1);
        assertEquals(countBefore, dataSource.size());
    }

    @Test
    @DisplayName("Должен удалять все задачи")
    void deleteTasks() {
        assertEquals(COUNT_OF_TASKS, taskService.getTasks().size());
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
    @DisplayName("Должен обновлять статус на указанный")
    void updateStatus() {
        var task = taskService.getTask(1);

        assertEquals(Status.NEW, task.getStatus());
        taskService.updateStatus(task, Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, task.getStatus());
    }
}
