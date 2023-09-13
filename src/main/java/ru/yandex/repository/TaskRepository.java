package ru.yandex.repository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository<T> {

    List<T> findAll();

    Optional<T> findById(int id);

    T create(T task);

    T update(T task);

    void delete(int id);

    void deleteBatch();
}
