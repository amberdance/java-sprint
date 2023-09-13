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
import static ru.yandex.model.Task.Status;

class SubtaskServiceTest extends AbstractServiceTest {

    protected static final int TASKS_TO_CREATE_COUNT = 3;
    private static final int SUBTASKS_TO_CREATE_COUNT = 3;

    private static SubtaskService subtaskService;
    private static EpicService epicService;

    @BeforeAll
    static void setUp() {
        subtaskService = new TaskServiceImpl(taskRepository, epicRepository, subtaskRepository);
        epicService = new TaskServiceImpl(taskRepository, epicRepository, subtaskRepository);
    }

    @BeforeEach
    void beforeEach() {
        dataSource.clear();
        var epic = epicService.createEpic(new Epic());

        // Начинается с 2 , потому что первый id отдается для создания эпика выше
        for (int i = 2; i <= SUBTASKS_TO_CREATE_COUNT + 1; i++) {
            var subtask = new Subtask(NAME_PREFIX + i, "Description_" + i);
            subtask.setEpicId(epic.getId());

            subtaskService.createSubtask(subtask);
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
        var subtask = subtaskService.createSubtask(new Subtask(1, "NAME", "DESCRIPTION"));
        var generatedId = idGenerator.generateId() - 1;

        assertEquals(generatedId, subtask.getId());
        assertEquals(TASKS_TO_CREATE_COUNT + 1, subtaskService.getSubtasks().size());
    }

    @Test
    @DisplayName("Выбрасывает исключение, если задан несуществующий id эпика")
    void whenEpicIdNotExistsThenExceptionThrew() {
        assertThrows(TaskNotFoundException.class, () -> subtaskService.createSubtask(new Subtask(555, "test", "test")));
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
        var countOfSubtasksBefore = subtaskService.getSubtasks().size();
        subtaskService.deleteSubtask(TASKS_TO_CREATE_COUNT);
        assertEquals(countOfSubtasksBefore - 1, subtaskService.getSubtasks().size());
    }

    @Test
    @DisplayName("Должен удалять все подзадачи")
    void deleteSubtasks() {
        subtaskService.deleteSubtasks();

        // +1 id на создание Эпика, который не учитывается тоже
        assertEquals(TASKS_TO_CREATE_COUNT + 1, dataSource.size());
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
