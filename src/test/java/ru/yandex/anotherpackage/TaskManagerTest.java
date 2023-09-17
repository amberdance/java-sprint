package ru.yandex.anotherpackage;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.Epic;
import ru.yandex.Subtask;
import ru.yandex.Task;
import ru.yandex.TaskManager;
import ru.yandex.util.IdGenerator;
import ru.yandex.util.SimpleIdGenerator;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskManagerTest {

    private static final int COUNT_OF_TASKS = 3;
    private static final IdGenerator idGenerator = new SimpleIdGenerator();
    private static final Map<Integer, Task> tasks = new HashMap<>();
    private static final Map<Integer, Epic> epics = new HashMap<>();
    private static final Map<Integer, Subtask> subtasks = new HashMap<>();
    private static TaskManager taskManager;
    private static int currentId;


    @BeforeAll
    static void beforeAll() {
        taskManager = new TaskManager(idGenerator, tasks, epics, subtasks);
    }

    @BeforeEach
    void setUp() {
        tasks.clear();
        epics.clear();
        subtasks.clear();

        for (int i = 0; i < COUNT_OF_TASKS; i++) {
            var id = idGenerator.generateId();
            var name = "Name_" + id;
            var description = "Description_" + id;

            tasks.put(id, new Task(id, name, description));
            var epic = new Epic(id, name, description);

            epics.put(id, epic);
            subtasks.put(id, new Subtask(id, epic.getId(), name, description));
        }

        currentId = idGenerator.getCurrentId() - 1;
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
        var task = taskManager.getTask(currentId);

        assertEquals("Name_" + currentId, task.getName());
        assertEquals("Description_" + currentId, task.getDescription());
        assertEquals(Task.Status.NEW, task.getStatus());
    }

    @Test
    void getEpic() {
        var epic = taskManager.getEpic(currentId);

        assertEquals("Name_" + currentId, epic.getName());
        assertEquals("Description_" + currentId, epic.getDescription());
        assertEquals(Task.Status.NEW, epic.getStatus());
    }

    @Test
    void getSubtask() {
        var subtask = taskManager.getSubtask(currentId);

        assertEquals("Name_" + currentId, subtask.getName());
        assertEquals("Description_" + currentId, subtask.getDescription());
        assertEquals(Task.Status.NEW, subtask.getStatus());
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
        var epic = new Epic();
        taskManager.createSubtask(new Subtask(epic.getId(), "SOME", "DESCRIPTION"));
        assertEquals(COUNT_OF_TASKS + 1, taskManager.getSubtasks().size());
    }

    @Test
    void updateTask() {
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
        var sizeBefore = taskManager.getTasks().size();
        taskManager.deleteTask(currentId);
        assertEquals(sizeBefore - 1, taskManager.getTasks().size());
    }

    @Test
    void testDeleteEpic() {
        var sizeBefore = taskManager.getEpics().size();
        taskManager.deleteEpic(currentId);
        assertEquals(sizeBefore - 1, taskManager.getEpics().size());
    }

    @Test
    void testDeleteSubtask() {
        var sizeBefore = taskManager.getSubtasks().size();
        taskManager.deleteSubTask(currentId);
        assertEquals(sizeBefore - 1, taskManager.getSubtasks().size());
    }

    @Test
    void getSubtasksByEpicId() {
        var epic = epics.get(currentId);
        int expectedSubtaskCount = 0;

        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == epic.getId()) {
                expectedSubtaskCount++;
            }
        }

        assertEquals(expectedSubtaskCount, taskManager.getSubtasksByEpicId(epic.getId()).size());
    }
}
