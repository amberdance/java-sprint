package ru.yandex.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.exception.TaskNotFoundException;
import ru.yandex.model.Epic;
import ru.yandex.model.Subtask;
import ru.yandex.model.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.model.Task.Status;

public class EpicServiceTest extends AbstractServiceTest {

    protected static final int COUNT_OF_TASKS = 3;
    private static final int COUNT_OF_EPICS = 3;
    private static EpicService epicService;


    @BeforeAll
    static void setUp() {
        epicService = new TaskServiceImpl(taskRepository, epicRepository, subtaskRepository);
    }

    @BeforeEach
    void beforeEach() {
        dataSource.clear();

        for (int i = 1; i <= COUNT_OF_EPICS; i++) {
            epicService.createEpic(new Epic(NAME_PREFIX + i, "Description_" + i,
                    List.of(new Subtask(), new Subtask(), new Subtask())));
        }

        // Добавляются обычные задачи, чтобы убедиться, что удаление производится только над эпиками
        for (int i = 0; i < COUNT_OF_TASKS; i++) {
            var task = new Task();
            task.setId(idGenerator.generateId());
            dataSource.add(task);
        }
    }

    @Test
    @DisplayName("Должен вернуть эпик по заданному id")
    void getEpic() {
        var epic = epicService.getEpic(1);

        assertEquals(1, epic.getId());
        assertEquals(3, epic.getSubtasks().size());
        assertEquals(NAME_PREFIX + 1, epic.getName());
    }


    @Test
    @DisplayName("При несуществующем id выбрасывается исключение")
    void shouldThrowsExceptionWhenEpicNotFound() {
        assertThrows(TaskNotFoundException.class, () -> epicService.getEpic(999));
    }

    @Test
    @DisplayName("Количество возвращенных эпиков == количеству эпиков в хранилище")
    void getEpics() {
        var epicsCount = (int) dataSource.stream().filter(e -> e.getClass().equals(Epic.class)).count();

        assertEquals(epicsCount, epicService.getEpics().size());
    }

    @Test
    @DisplayName("Должен создать эпик, подзадачи, связать их с эпиком и сгенерировать для всех id")
    void createEpic() {
        var epic = epicService.createEpic(new Epic("NAME", "DESCRIPTION",
                List.of(new Subtask("Subtask", "Description"))));
        assertEquals(dataSource.size() - 1, epic.getId());
        assertEquals(COUNT_OF_EPICS + 1, epicService.getEpics().size());
        assertEquals(1, epic.getSubtasks().size());

        epic.getSubtasks().forEach(s -> assertTrue(s.getEpicId() == epic.getId() && s.getId() != 0));
    }

    @Test
    @DisplayName("Должен обновлять поля эпика")
    void updateEpic() {
        var epic = epicService.getEpic(1);
        epic.setName("UPDATED_NAME");
        epic.setDescription("UPDATED_DESCRIPTION");
        epic.setStatus(Status.IN_PROGRESS);
        epic.setSubtasks(List.of(new Subtask("Subtask", "Description")));

        var updatedEpic = epicService.updateEpic(epic);
        assertEquals(epic.getName(), updatedEpic.getName());
        assertEquals(epic.getDescription(), updatedEpic.getDescription());
        assertEquals(Task.Status.IN_PROGRESS, updatedEpic.getStatus());
        assertEquals(1, updatedEpic.getSubtasks().size());
    }

    @Test
    @DisplayName("Если все подзадачи DONE, то эпик тоже должен быть DONE")
    void updateStatusDoneTest() {
        var epic = epicService.getEpic(1);
        epic.getSubtasks().forEach(s -> s.setStatus(Status.DONE));
        epicService.updateEpic(epic);

        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    @DisplayName("Если есть подзадача IN_PROGRESS, то эпик тоже должен быть IN_PROGRESS")
    void updateStatusInProgressTest() {
        var epic = epicService.getEpic(1);
        epic.getSubtasks().get(0).setStatus(Status.IN_PROGRESS);
        epicService.updateEpic(epic);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    @DisplayName("Должен удалять эпик по id и связанные с ним подзадачи")
    void deleteEpic() {
        assertEquals(COUNT_OF_EPICS, epicService.getEpics().size());
        epicService.deleteEpic(1);
        assertEquals(COUNT_OF_EPICS - 1, epicService.getEpics().size());
    }

    @Test
    @DisplayName("Должен удалять все эпики")
    void deleteEpics() {
        epicService.deleteEpics();
        assertEquals(COUNT_OF_TASKS, dataSource.size());
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
