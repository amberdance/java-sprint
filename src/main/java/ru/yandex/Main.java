package ru.yandex;

import ru.yandex.model.Epic;
import ru.yandex.model.Subtask;
import ru.yandex.model.Task;
import ru.yandex.repository.EpicRepositoryImpl;
import ru.yandex.repository.SubtaskRepositoryImpl;
import ru.yandex.repository.TaskRepositoryImpl;
import ru.yandex.service.TaskServiceImpl;
import ru.yandex.utils.ArrayListLastIdGenerator;

import java.util.ArrayList;

public class Main {

    //Driver code
    public static void main(String[] args) {
        var dataSource = new ArrayList<Task>();
        var idGenerator = new ArrayListLastIdGenerator(dataSource);
        var taskRepository = new TaskRepositoryImpl(dataSource, idGenerator);
        var epicRepository = new EpicRepositoryImpl(dataSource, idGenerator);
        var subtaskRepository = new SubtaskRepositoryImpl(dataSource, idGenerator);
        var taskService = new TaskServiceImpl(taskRepository, epicRepository, subtaskRepository);

        taskService.createTask(new Task("taskname", "taskdescr"));
        taskService.createTask(new Epic("epicname", "epicdescr"));
        taskService.createTask(new Subtask("subtaskname", "subtaskdescr"));

        System.out.println(taskService.getTasks());
    }
}
