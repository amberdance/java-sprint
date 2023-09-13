package ru.yandex;

import ru.yandex.model.Epic;
import ru.yandex.model.Subtask;
import ru.yandex.model.Task;
import ru.yandex.repository.EpicRepositoryImpl;
import ru.yandex.repository.SubtaskRepositoryImpl;
import ru.yandex.repository.TaskRepository;
import ru.yandex.repository.TaskRepositoryImpl;
import ru.yandex.service.TaskService;
import ru.yandex.service.TaskServiceImpl;
import ru.yandex.utils.ArrayListIdGenerator;
import ru.yandex.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class Main {

    //Driver code
    public static void main(String[] args) {
        List<Task> dataSource = new ArrayList<>();
        IdGenerator idGenerator = new ArrayListIdGenerator(dataSource);
        TaskRepository<Task> taskRepository = new TaskRepositoryImpl(dataSource, idGenerator);
        TaskRepository<Epic> epicRepository = new EpicRepositoryImpl(dataSource, idGenerator);
        TaskRepository<Subtask> subtaskRepository = new SubtaskRepositoryImpl(dataSource, idGenerator);
        TaskService taskService = new TaskServiceImpl(taskRepository, epicRepository, subtaskRepository);

        taskService.createTask(new Task("taskname", "taskdescr"));
        taskService.createTask(new Epic("epicname", "epicdescr"));
        taskService.createTask(new Subtask("subtaskname", "subtaskdescr"));

        System.out.println(taskService.getTasks());
    }
}
