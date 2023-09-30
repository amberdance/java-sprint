package ru.yandex;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.managers.InMemoryTaskManager;
import ru.yandex.managers.TaskManager;
import ru.yandex.model.Epic;
import ru.yandex.model.Subtask;
import ru.yandex.model.Task;
import ru.yandex.util.IdGenerator;
import ru.yandex.util.Managers;
import ru.yandex.util.SimpleIdGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.yandex.managers.InMemoryHistoryManager.HISTORY_CAPACITY;


class InMemoryTaskManagerTest {

    private static final IdGenerator idGenerator = new SimpleIdGenerator();
    private static final TaskManager taskManager = new InMemoryTaskManager(idGenerator, Managers.getDefaultHistory());


    @BeforeEach
    void setUp() {
        idGenerator.resetIndex();
        taskManager.deleteTasks();
        taskManager.deleteEpics();
        taskManager.deleteSubtasks();

        taskManager.createTask(new Task("Task", "d"));
        var epic = taskManager.createEpic(new Epic("Epic", "d"));
        taskManager.createSubtask(new Subtask(epic.getId(), "Subtask", "d"));
    }

    @Test
    void getHistory() {
        taskManager.getTask(1); // for saving to history

        for (int i = 0; i <= HISTORY_CAPACITY; i++) {
            var task = new Task("t", "t");
            taskManager.createTask(task);
            taskManager.getTask(task.getId());
        }

        taskManager.getEpic(2); // for saving to history
        assertEquals(HISTORY_CAPACITY, taskManager.getHistoryManager().getHistory().size());
    }

    @Test
    void getTasks() {
        assertEquals(1, taskManager.getTasks().size());
    }

    @Test
    void getEpics() {
        assertEquals(1, taskManager.getEpics().size());
    }

    @Test
    void getSubtasks() {
        assertEquals(1, taskManager.getSubtasks().size());
    }

    @Test
    void getTask() {
        var task = taskManager.getTask(1);

        assertEquals("Task", task.getName());
        assertEquals(Task.Status.NEW, task.getStatus());
    }

    @Test
    void getEpic() {
        var epic = taskManager.getEpic(2);

        assertEquals("Epic", epic.getName());
        assertEquals(Task.Status.NEW, epic.getStatus());
    }

    @Test
    void getSubtask() {
        var subtask = taskManager.getSubtask(3);

        assertEquals("Subtask", subtask.getName());
        assertEquals(Task.Status.NEW, subtask.getStatus());
    }

    @Test
    void createTask() {
        taskManager.createTask(new Task("SOME", "DESCRIPTION"));
        assertEquals(2, taskManager.getTasks().size());
    }

    @Test
    void createEpic() {
        taskManager.createEpic(new Epic("SOME", "DESCRIPTION"));
        assertEquals(2, taskManager.getEpics().size());

    }

    @Test
    void createSubtask() {
        var epic = taskManager.getEpic(2);

        taskManager.createSubtask(new Subtask(epic.getId(), "SOME", "DESCRIPTION"));
        assertEquals(2, taskManager.getSubtasks().size());
        assertEquals(2, epic.getSubtasks().size());
    }

    @Test
    void updateTask() {
        var task = taskManager.getTask(1);
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
        var epic = taskManager.getEpic(2);
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
        var subtask = taskManager.getSubtask(3);
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
        assertTrue(taskManager.getTasks().isEmpty());
    }

    @Test
    void deleteEpics() {
        taskManager.deleteEpics();
        assertTrue(taskManager.getEpics().isEmpty());
        assertTrue(taskManager.getSubtasks().isEmpty());
    }

    @Test
    void deleteSubtasks() {
        taskManager.deleteSubtasks();
        assertTrue(taskManager.getSubtasks().isEmpty());
    }

    @Test
    void testDeleteTask() {
        var sizeBefore = taskManager.getTasks().size();
        taskManager.deleteTask(1);
        assertEquals(sizeBefore - 1, taskManager.getTasks().size());
    }

    @Test
    void testDeleteEpic() {
        var sizeBefore = taskManager.getEpics().size();
        taskManager.deleteEpic(2);
        assertEquals(sizeBefore - 1, taskManager.getEpics().size());
    }

    @Test
    void deleteSubtask() {
        var sizeBefore = taskManager.getSubtasks().size();
        var epicSubtasksIdsBefore = taskManager.getEpic(2).getSubtasks().size();

        taskManager.deleteSubtask(3);
        assertEquals(sizeBefore - 1, taskManager.getSubtasks().size());
        assertEquals(epicSubtasksIdsBefore - 1, taskManager.getEpic(2).getSubtasks().size());
    }

    @Test
    void getSubtasksByEpicId() {
        var epic = taskManager.getEpic(2);
        int expectedSubtaskCount = 1;
        assertEquals(expectedSubtaskCount, taskManager.getSubtasksByEpicId(epic.getId()).size());
    }

}
