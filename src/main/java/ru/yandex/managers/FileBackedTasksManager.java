package ru.yandex.managers;

import ru.yandex.exception.ManagerSaveException;
import ru.yandex.model.Epic;
import ru.yandex.model.Subtask;
import ru.yandex.model.Task;
import ru.yandex.model.TaskType;
import ru.yandex.util.IdGenerator;
import ru.yandex.util.SimpleIdGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTasksManager(IdGenerator idGenerator, HistoryManager historyManager, File file) {
        super(idGenerator, historyManager);
        var fileName = "./data/data.csv";
        this.file = new File(fileName);

        if (!file.isFile()) {
            try {
                Files.createFile(Paths.get(fileName));
            } catch (IOException e) {
                throw new ManagerSaveException("Ошибка создания файла.");
            }
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager =
                new FileBackedTasksManager(new SimpleIdGenerator(), new InMemoryHistoryManager(), file);
        String data;

        try {
            data = Files.readString(Path.of(file.getAbsolutePath()));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла.");
        }

        String[] lines = data.split("\n");
        String lineOfHistory = "";

        boolean isTitle = true;
        boolean itsTask = true;
        int maxId = 0;
        int id;

        for (String line : lines) {
            if (isTitle) {
                isTitle = false;
                continue;
            }
            if (line.isEmpty() || line.equals("\r")) {
                itsTask = false;
                continue;
            }

            if (itsTask) {
                TaskType taskType = TaskType.valueOf(line.split(",")[1]);

                switch (taskType) {
                    case EPIC:
                        Epic epic = (Epic) fromString(line, TaskType.EPIC, fileBackedTasksManager);
                        id = epic.getId();
                        if (id > maxId) {
                            maxId = id;
                        }
                        fileBackedTasksManager.epics.put(id, epic);
                        //epics.add(line);
                        break;

                    case SUBTASK:
                        Subtask subtask = (Subtask) fromString(line, TaskType.SUBTASK, fileBackedTasksManager);
                        id = subtask.getId();
                        if (id > maxId) {
                            maxId = id;
                        }
                        fileBackedTasksManager.subtasks.put(id, subtask);
                        //subtasks.add(line);
                        break;

                    case TASK:
                        Task task = fromString(line, TaskType.TASK, fileBackedTasksManager);

                        id = task.getId();
                        if (id > maxId) {
                            maxId = id;
                        }
                        fileBackedTasksManager.tasks.put(id, task);
                        //tasks.add(line);
                        break;

                }
            } else {
                lineOfHistory = line;
            }
        }

        List<Integer> ids = fromString(lineOfHistory);

        for (Integer taskId : ids) {
            fileBackedTasksManager.historyManager.addTask(getTaskAllKind(taskId, fileBackedTasksManager));
        }

        return fileBackedTasksManager;
    }

    private static Task getTaskAllKind(int id, InMemoryTaskManager inMemoryTaskManager) {
        Task task = inMemoryTaskManager.getTasks().get(id);
        if (!(task == null)) {
            return task;
        }
        Task epic = inMemoryTaskManager.getEpics().get(id);
        if (!(epic == null)) {
            return epic;
        }
        Task subtask = inMemoryTaskManager.getSubtasks().get(id);
        if (!(subtask == null)) {
            return subtask;
        }
        return null;
    }

    private static String toString(HistoryManager manager) {
        List<String> s = new ArrayList<>();
        for (Task task : manager.getHistory()) {
            s.add(String.valueOf(task.getId()));
        }
        String hist = String.join(",", s);
        return hist;
    }

    private static List<Integer> fromString(String value) {
        String[] idsString = value.split(",");
        List<Integer> tasksId = new ArrayList<>();
        for (String idString : idsString) {
            tasksId.add(Integer.valueOf(idString));
        }
        return tasksId;
    }

    private static Task fromString(String value, TaskType taskType, FileBackedTasksManager fileBackedTasksManager) {
        String[] dataOfTask = value.split(",", 6);
        int id = Integer.valueOf(dataOfTask[0]);
        String name = dataOfTask[2];
        Task.Status status = Task.Status.valueOf(dataOfTask[3]);
        String description = dataOfTask[4];
        String epicIdString = dataOfTask[5].trim();

        switch (taskType) {
            case TASK:
                return new Task(id, name, description, status);
            case EPIC:
                return new Epic(name, description);
            case SUBTASK:
                return new Subtask(fileBackedTasksManager.epics.get(Integer.valueOf(epicIdString)).getId(), name,
                        description);
            default:
                return null;
        }
    }

    public void save() {
        try (Writer writer = new FileWriter(file)) {
            writer.write("id,type,name,status,description,epic\n");
            HashMap<Integer, String> allTasks = new HashMap<>();

            for (Integer id : tasks.keySet()) {
                allTasks.put(id, tasks.get(id).toStringFromFile());
            }

            for (Integer id : epics.keySet()) {
                allTasks.put(id, epics.get(id).toStringFromFile());
            }

            for (Integer id : subtasks.keySet()) {
                allTasks.put(id, subtasks.get(id).toStringFromFile());
            }

            for (String value : allTasks.values()) {
                writer.write(String.format("%s\n", value));
            }

            writer.write("\n");
            writer.write(toString(this.historyManager));

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи файла.");
        }
    }


    @Override
    public Task createTask(Task task) {
        var tsk = super.createTask(task);
        save();

        return tsk;
    }

    @Override
    public Task updateTask(Task task) {
        var tsk = super.updateTask(task);
        save();

        return tsk;
    }

    @Override
    public Task getTask(int id) {
        var task = super.getTask(id);
        save();

        return task;
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public Epic createEpic(Epic epic) {
        var tsk = super.createEpic(epic);
        save();

        return tsk;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        var epc = super.updateEpic(epic);
        save();

        return epc;
    }

    @Override
    public Epic getEpic(int id) {
        var epic = super.getEpic(id);
        save();

        return epic;
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        var sbtsk = super.createSubtask(subtask);
        save();

        return sbtsk;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        var sbtsk = super.updateSubtask(subtask);
        save();

        return sbtsk;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        save();

        return subtask;
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

}
