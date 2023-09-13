package ru.yandex.repository;

import ru.yandex.model.Subtask;

import java.util.List;
import java.util.Optional;

public class SubtaskRepositoryImpl implements TaskRepository<Subtask> {
    @Override
    public List<Subtask> findAll() {
        return null;
    }

    @Override
    public Optional<Subtask> findById(int id) {
        return Optional.empty();
    }

    @Override
    public Subtask create(Subtask task) {
        return null;
    }

    @Override
    public Subtask update(Subtask task) {
        return null;
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void deleteBatch() {

    }
}
