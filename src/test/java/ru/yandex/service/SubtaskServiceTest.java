package ru.yandex.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.exception.TaskNotFoundException;
import ru.yandex.model.Subtask;
import ru.yandex.model.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.yandex.model.Task.Status;

class SubtaskServiceTest extends AbstractServiceTest {

    protected static final int TASKS_TO_CREATE_COUNT = 3;
    private static final int SUBTASKS_TO_CREATE_COUNT = 3;
    private static SubtaskService subtaskService;

    @BeforeAll
    static void setUp() {
        subtaskService = new TaskServiceImpl(taskRepository, epicRepository, subtaskRepository);
    }

    @BeforeEach
    void beforeEach() {
        dataSource.clear();

        for (int i = 1; i <= SUBTASKS_TO_CREATE_COUNT; i++) {
            subtaskService.createSubtask(new Subtask(NAME_PREFIX + i, "Description_" + i));
        }

        for (int i = 0; i < TASKS_TO_CREATE_COUNT; i++) {
            var task = new Task();
            task.setId(idGenerator.generateId());
            dataSource.add(task);
        }
    }

    @Test
    @DisplayName("Должен вернуть подзадачу по заданному существующему id")
    void getSubtask() {
        var subtask = subtaskService.getSubtask(SUBTASKS_TO_CREATE_COUNT);
        assertNotNull(subtask);
        assertEquals(SUBTASKS_TO_CREATE_COUNT, subtask.getId());
        assertEquals(NAME_PREFIX + SUBTASKS_TO_CREATE_COUNT, subtask.getName());
    }

    @Test
    @DisplayName("Количество возвращенных подзадач == количеству подзадач в хранилище")
    void getSubtasks() {
        assertEquals(SUBTASKS_TO_CREATE_COUNT, subtaskService.getSubtasks().size());
    }

    @Test
    @DisplayName("При несуществующем id выбрасывается исключение")
    void shouldThrowsExceptionWhenSubtaskNotFound() {
        assertThrows(TaskNotFoundException.class, () -> subtaskService.getSubtask(999));
    }

    @Test
    @DisplayName("Должен создать подзадачу и сгенерировать id")
    void createSubtask() {
        var subtask = subtaskService.createSubtask(new Subtask("NAME", "DESCRIPTION"));

        assertEquals(SUBTASKS_TO_CREATE_COUNT + TASKS_TO_CREATE_COUNT + 1, subtask.getId());
        assertEquals(SUBTASKS_TO_CREATE_COUNT + 1, subtaskService.getSubtasks().size());
    }

    @Test
    @DisplayName("Должен обновлять поля подзадачи")
    void updateSubtask() {
        var subtask = subtaskService.getSubtask(SUBTASKS_TO_CREATE_COUNT);
        subtask.setName("UPDATED_NAME");
        subtask.setDescription("UPDATED_DESCRIPTION");
        subtask.setStatus(Status.IN_PROGRESS);

        var updatedSubtask = subtaskService.updateSubtask(subtask);

        assertEquals(subtask.getName(), updatedSubtask.getName());
        assertEquals(subtask.getDescription(), updatedSubtask.getDescription());
        assertEquals(Status.IN_PROGRESS, updatedSubtask.getStatus());
    }

    @Test
    @DisplayName("Должен удалять подзадачу по заданному id")
    void deleteSubtask() {
        subtaskService.deleteSubtask(1);
        assertEquals(SUBTASKS_TO_CREATE_COUNT - 1, subtaskService.getSubtasks().size());
    }

    @Test
    @DisplayName("Должен удалять все эпики")
    void deleteSubtasks() {
        subtaskService.deleteSubtasks();
        assertEquals(TASKS_TO_CREATE_COUNT, dataSource.size());
    }

    @Test
    @DisplayName("Нельзя изменять список подзадач напрямую через List API")
    void shouldThrowsExceptionWhenAccessedDirectlyToList() {
        assertThrows(UnsupportedOperationException.class, () -> {
            subtaskService.getSubtasks().add(new Subtask());
            subtaskService.getSubtasks().clear();
            subtaskService.getSubtasks().remove(1);
        });
    }

}
