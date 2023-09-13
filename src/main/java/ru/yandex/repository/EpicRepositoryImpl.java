package ru.yandex.repository;

import ru.yandex.model.Epic;

import java.util.List;
import java.util.Optional;

public class EpicRepositoryImpl implements TaskRepository<Epic> {
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
