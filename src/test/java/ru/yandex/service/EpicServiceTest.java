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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EpicServiceTest extends AbstractServiceTest {

    protected static final int TASKS_TO_CREATE_COUNT = 3;
    private static final int EPICS_TO_CREATE_COUNT = 3;
    private static EpicService epicService;


    @BeforeAll
    static void setUp() {
        epicService = new TaskServiceImpl(taskRepository, epicRepository, subtaskRepository);
    }

    @BeforeEach
    void beforeEach() {
        dataSource.clear();

        for (int i = 1; i <= EPICS_TO_CREATE_COUNT; i++) {
            var name = NAME_PREFIX + i;
            var descr = "Description_" + i;

            epicService.createEpic(new Epic(name, descr,
                    List.of(new Subtask(name, descr), new Subtask(name, descr), new Subtask(name, descr))));

        }

        for (int i = 0; i < TASKS_TO_CREATE_COUNT; i++) {
            var task = new Task();
            task.setId(idGenerator.generateId());
            dataSource.add(task);
        }
    }

    @Test
    @DisplayName("Должен вернуть эпик по заданному существующему id")
    void getEpic() {
        var epic = epicService.getEpic(EPICS_TO_CREATE_COUNT);

        assertNotNull(epic);
        assertEquals(EPICS_TO_CREATE_COUNT, epic.getId());
        assertEquals(NAME_PREFIX + EPICS_TO_CREATE_COUNT, epic.getName());
        assertEquals(3, epic.getSubtasks().size());
    }

    @Test
    @DisplayName("Количество возвращенных эпиков == количеству эпиков в хранилище")
    void getEpics() {
        assertEquals(EPICS_TO_CREATE_COUNT, epicService.getEpics().size());
    }

    @Test
    @DisplayName("При несуществующем id выбрасывается исключение")
    void shouldThrowsExceptionWhenEpicNotFound() {
        assertThrows(TaskNotFoundException.class, () -> epicService.getEpic(999));
    }

    @Test
    @DisplayName("Должен создать эпик и сгенерировать id")
    void createEpic() {
        var epic =
                epicService.createEpic(new Epic("NAME", "DESCRIPTION", List.of(new Subtask("Subtask", "Description"))));

        assertEquals(EPICS_TO_CREATE_COUNT + TASKS_TO_CREATE_COUNT + 1, epic.getId());
        assertEquals(EPICS_TO_CREATE_COUNT + 1, epicService.getEpics().size());
        assertEquals(1, epic.getSubtasks().size());
    }

    @Test
    @DisplayName("Должен обновлять поля эпика")
    void updateEpic() {
        var epic = epicService.getEpic(EPICS_TO_CREATE_COUNT);
        epic.setName("UPDATED_NAME");
        epic.setDescription("UPDATED_DESCRIPTION");
        epic.setStatus(Status.IN_PROGRESS);
        epic.setSubtasks(List.of(new Subtask("Subtask", "Description")));

        epicService.updateEpic(epic);

        var updatedEpic = epicService.getEpic(EPICS_TO_CREATE_COUNT);

        assertEquals(epic.getName(), updatedEpic.getName());
        assertEquals(epic.getDescription(), updatedEpic.getDescription());
        assertEquals(Status.IN_PROGRESS, updatedEpic.getStatus());
        assertEquals(1, updatedEpic.getSubtasks().size());
    }

    @Test
    @DisplayName("Должен удалять эпик по id")
    void deleteEpic() {
        assertEquals(EPICS_TO_CREATE_COUNT, epicService.getEpics().size());
        epicService.deleteEpic(1);
        assertEquals(EPICS_TO_CREATE_COUNT - 1, epicService.getEpics().size());
    }

    @Test
    @DisplayName("Должен удалять все эпики")
    void deleteEpics() {
        epicService.deleteEpics();
        assertEquals(TASKS_TO_CREATE_COUNT, dataSource.size());
    }

    @Test
    @DisplayName("Нельзя изменять список эпиков напрямую через List API")
    void shouldThrowsExceptionWhenAccessedDirectlyToList() {
        assertThrows(UnsupportedOperationException.class, () -> {
            epicService.getEpics().add(new Epic());
            epicService.getEpics().clear();
            epicService.getEpics().remove(1);
        });
    }


}
