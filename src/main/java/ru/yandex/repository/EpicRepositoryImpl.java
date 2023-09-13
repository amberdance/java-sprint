package ru.yandex.repository;

import lombok.RequiredArgsConstructor;
import ru.yandex.model.Epic;
import ru.yandex.model.Task;
import ru.yandex.utils.IdGenerator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class EpicRepositoryImpl implements TaskRepository<Epic> {

    private final List<Task> taskStorage;
    private final IdGenerator idGenerator;

    @Override
    public List<Epic> findAll() {
        return taskStorage.stream().filter(Epic.class::isInstance).map(Epic.class::cast).collect(Collectors.toList());
    }

    @Override
    public Optional<Epic> findById(int id) {
        return Optional.empty();
    }

    @Override
    public Epic create(Epic task) {
        return null;
    }

    @Override
    public Epic update(Epic task) {
        return null;
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void deleteBatch() {

    }
}
