package ru.yandex.service;

import ru.yandex.model.Epic;
import ru.yandex.model.Subtask;
import ru.yandex.model.Task;
import ru.yandex.repository.EpicRepositoryImpl;
import ru.yandex.repository.SubtaskRepositoryImpl;
import ru.yandex.repository.TaskRepository;
import ru.yandex.repository.TaskRepositoryImpl;
import ru.yandex.utils.ArrayListIdGenerator;
import ru.yandex.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class BaseServiceTest {

    protected static final int TASKS_TO_CREATE_COUNT = 5;
    protected static final String NAME_PREFIX = "Name_";
    protected static final List<Task> dataSource = new ArrayList<>();

    protected static final IdGenerator idGenerator = new ArrayListIdGenerator(dataSource);
    protected static final TaskRepository<Task> taskRepository = new TaskRepositoryImpl(dataSource, idGenerator);
    protected static final TaskRepository<Epic> epicRepository = new EpicRepositoryImpl(dataSource, idGenerator);
    protected static final TaskRepository<Subtask> subtaskRepository =
            new SubtaskRepositoryImpl(dataSource, idGenerator);
}
