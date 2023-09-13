package ru.yandex.repository;

import lombok.RequiredArgsConstructor;
import ru.yandex.model.Epic;
import ru.yandex.model.Task;
import ru.yandex.utils.IdGenerator;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class EpicRepositoryImpl implements TaskRepository<Epic> {

    private final List<Task> taskStorage;
    private final IdGenerator idGenerator;
    private final Predicate<Task> filterByClass = Epic.class::isInstance;
    private final Function<Task, Epic> classMapper = Epic.class::cast;

    @Override
    public List<Epic> findAll() {
        return taskStorage.stream()
                .filter(filterByClass)
                .map(classMapper)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Optional<Epic> findById(int id) {
        return taskStorage.stream()
                .filter(filterByClass)
                .map(classMapper)
                .filter(t -> t.getId() == id)
                .findFirst();
    }

    @Override
    public Epic create(Epic epic) {
        epic.setId(idGenerator.generateId());
        taskStorage.add(epic);

        return epic;
    }

    @Override
    public Epic update(Epic epic) {
        var epicOptional = findById(epic.getId());

        if (epicOptional.isPresent()) {
            var updatedEpic = epicOptional.get();
            updatedEpic.setName(epic.getName());
            updatedEpic.setDescription(epic.getDescription());
            updatedEpic.setStatus(epic.getStatus());
            updatedEpic.setSubtasks(epic.getSubtasks());

            taskStorage.set(epic.getId() - 1, updatedEpic);

            return updatedEpic;
        } else {
            return create(epic);
        }
    }

    @Override
    public void delete(int id) {
        findById(id).ifPresent(taskStorage::remove);
    }

    @Override
    public void deleteBatch() {
        taskStorage.removeAll(findAll());
    }
}
