package ru.yandex;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.yandex.managers.HistoryManager;
import ru.yandex.managers.InMemoryTaskManager;
import ru.yandex.managers.TaskManager;
import ru.yandex.model.Epic;
import ru.yandex.model.Subtask;
import ru.yandex.model.Task;
import ru.yandex.util.IdGenerator;
import ru.yandex.util.Managers;
import ru.yandex.util.SimpleIdGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.yandex.managers.InMemoryHistoryManager.HISTORY_CAPACITY;


@Disabled
class InMemoryTaskManagerTest {


    private static final short COUNT_OF_TASKS = 3;
    private static final IdGenerator idGenerator = new SimpleIdGenerator();
    private static final HistoryManager history = Managers.getDefaultHistory();
    private static final TaskManager taskManager = new InMemoryTaskManager(idGenerator, history);


    @BeforeEach
    void setUp() {
        taskManager.deleteTasks();
        taskManager.deleteEpics();
        taskManager.deleteSubtasks();

        for (int i = 1; i <= COUNT_OF_TASKS; i++) {
            var name = "Name_" + i;
            var description = "Description_" + i;

            taskManager.createTask(new Task(name, description));

            var epic = taskManager.createEpic(new Epic(name, description));
            var subtask = taskManager.createSubtask(new Subtask(epic.getId(), name, description));

            epic.getSubtaskIds().add(subtask.getId());
        }
    }

    @Test
    void getHistory() {
        var currentId = idGenerator.getCurrentId();
        taskManager.getTask(idGenerator.getCurrentId()); // for saving to history

        for (int i = 0; i <= HISTORY_CAPACITY; i++) {
            var task = new Task("t", "t");
            taskManager.createTask(task);
            taskManager.getTask(task.getId());
        }

        taskManager.getEpic(currentId); // for saving to history
        assertEquals(HISTORY_CAPACITY, history.getHistory().size());
    }

    @Test
    void getTasks() {
        assertEquals(COUNT_OF_TASKS, taskManager.getTasks().size());
    }

    @Test
    void getEpics() {
        assertEquals(COUNT_OF_TASKS, taskManager.getEpics().size());
    }

    @Test
    void getSubtasks() {
        assertEquals(COUNT_OF_TASKS, taskManager.getSubtasks().size());
    }

    @Test
    void getTask() {
        var id = 1;
        var task = taskManager.getTask(id);

        assertEquals("Name_" + id, task.getName());
        assertEquals("Description_" + id, task.getDescription());
        assertEquals(Task.Status.NEW, task.getStatus());
        assertThrows(NullPointerException.class, () -> taskManager.getTask(999999));
    }

    @Test
    void getEpic() {
        var id = 4;
        var epic = taskManager.getEpic(id);

        assertEquals("Name_" + id, epic.getName());
        assertEquals("Description_" + id, epic.getDescription());
        assertEquals(Task.Status.NEW, epic.getStatus());
        assertThrows(NullPointerException.class, () -> taskManager.getEpic(999999));

    }

    @Test
    void getSubtask() {
        var id = 3;
        var subtask = taskManager.getSubtask(id);

        assertEquals("Name_" + id, subtask.getName());
        assertEquals("Description_" + id, subtask.getDescription());
        assertEquals(Task.Status.NEW, subtask.getStatus());
        assertThrows(NullPointerException.class, () -> taskManager.getSubtask(999999));
    }

    @Test
    void createTask() {
        taskManager.createTask(new Task("SOME", "DESCRIPTION"));
        assertEquals(COUNT_OF_TASKS + 1, taskManager.getTasks().size());
    }

    @Test
    void createEpic() {
        taskManager.createEpic(new Epic("SOME", "DESCRIPTION"));
        assertEquals(COUNT_OF_TASKS + 1, taskManager.getEpics().size());

    }

    @Test
    void createSubtask() {
        var epic = taskManager.getEpic(3);

        taskManager.createSubtask(new Subtask(epic.getId(), "SOME", "DESCRIPTION"));
        assertEquals(COUNT_OF_TASKS + 1, taskManager.getSubtasks().size());
    }

    @Test
    void updateTask() {
        var currentId = idGenerator.getCurrentId();
        var task = taskManager.getTask(currentId);
        task.setStatus(Task.Status.DONE);
        task.setName("NAME");
        task.setDescription("DESCRIPTION");

        var taskCompare = taskManager.updateTask(task);

        assertEquals("NAME", taskCompare.getName());
        assertEquals("DESCRIPTION", taskCompare.getDescription());
        assertEquals(Task.Status.DONE, taskCompare.getStatus());
    }

    @Test
    void updateEpic() {
        var currentId = idGenerator.getCurrentId();
        var epic = taskManager.getEpic(currentId);
        epic.setName("NAME");
        epic.setDescription("DESCRIPTION");

        var epicToCompare = taskManager.updateEpic(epic);

        assertEquals("NAME", epicToCompare.getName());
        assertEquals("DESCRIPTION", epicToCompare.getDescription());
        assertEquals(Task.Status.NEW, epicToCompare.getStatus());

        var anySubtask = taskManager.getSubtasksByEpicId(epicToCompare.getId()).get(0);
        anySubtask.setStatus(Task.Status.IN_PROGRESS);
        taskManager.updateSubtask(anySubtask);

        assertEquals(Task.Status.IN_PROGRESS, epicToCompare.getStatus());
    }

    @Test
    void updateSubtask() {
        var currentId = idGenerator.getCurrentId();
        var subtask = taskManager.getSubtask(currentId);
        subtask.setStatus(Task.Status.IN_PROGRESS);
        subtask.setName("NAME");
        subtask.setDescription("DESCRIPTION");

        var subtaskToCompare = taskManager.updateSubtask(subtask);

        assertEquals("NAME", subtaskToCompare.getName());
        assertEquals("DESCRIPTION", subtaskToCompare.getDescription());
        assertEquals(Task.Status.IN_PROGRESS, subtaskToCompare.getStatus());
        assertEquals(Task.Status.IN_PROGRESS, taskManager.getEpic(subtaskToCompare.getEpicId()).getStatus());
    }

    @Test
    void deleteTasks() {
        taskManager.deleteTasks();
        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    void deleteEpics() {
        taskManager.deleteEpics();
        assertEquals(0, taskManager.getEpics().size());
        assertEquals(0, taskManager.getSubtasks().size());
    }

    @Test
    void deleteSubtasks() {
        taskManager.deleteSubtasks();
        assertEquals(0, taskManager.getSubtasks().size());
    }

    @Test
    void testDeleteTask() {
        var currentId = idGenerator.getCurrentId();
        var sizeBefore = taskManager.getTasks().size();
        taskManager.deleteTask(currentId);
        assertEquals(sizeBefore - 1, taskManager.getTasks().size());
    }

    @Test
    void testDeleteEpic() {
        var currentId = idGenerator.getCurrentId();
        var sizeBefore = taskManager.getEpics().size();
        taskManager.deleteEpic(currentId);
        assertEquals(sizeBefore - 1, taskManager.getEpics().size());
    }

    @Test
    @Disabled
    void deleteSubtask() {
        var currentId = idGenerator.getCurrentId();
        var sizeBefore = taskManager.getSubtasks().size();
        var epicSubtasksIdsBefore = taskManager.getEpics().get(currentId).getSubtaskIds().size();

        taskManager.deleteSubtask(currentId);
        assertEquals(sizeBefore - 1, taskManager.getSubtasks().size());
        assertEquals(epicSubtasksIdsBefore - 1, taskManager.getEpics().get(currentId).getSubtaskIds().size());
    }

    @Test
    @Disabled
    void getSubtasksByEpicId() {
        var currentId = idGenerator.getCurrentId();
        var epic = taskManager.getEpic(currentId);
        int expectedSubtaskCount = 0;

        for (Subtask subtask : taskManager.getSubtasks()) {
            if (subtask.getEpicId() == epic.getId()) {
                expectedSubtaskCount++;
            }
        }

        assertEquals(expectedSubtaskCount, taskManager.getSubtasksByEpicId(epic.getId()).size());
    }

}
