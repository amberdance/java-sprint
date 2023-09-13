package ru.yandex.repository;

import lombok.RequiredArgsConstructor;
import ru.yandex.model.Subtask;
import ru.yandex.model.Task;
import ru.yandex.utils.IdGenerator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SubtaskRepositoryImpl implements TaskRepository<Subtask> {

    private final List<Task> taskStorage;
    private final IdGenerator idGenerator;

    @Override
    public List<Subtask> findAll() {
        return taskStorage.stream().filter(Subtask.class::isInstance).map(Subtask.class::cast)
                .collect(Collectors.toList());
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
