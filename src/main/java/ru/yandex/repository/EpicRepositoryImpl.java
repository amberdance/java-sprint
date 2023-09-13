package ru.yandex.repository;

import lombok.RequiredArgsConstructor;
import ru.yandex.model.Epic;
import ru.yandex.model.Task;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class EpicRepositoryImpl implements TaskRepository<Epic> {

    private final List<Task> taskStorage;

    @Override
    public List<Epic> findAll() {
        return null;
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
