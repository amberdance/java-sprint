package ru.yandex.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Task {

    protected int id;
    protected String name;
    protected String description;
    protected Status status = Status.NEW;

    public Task(int id, @NonNull String name, @NonNull String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Task(@NonNull String name, @NonNull String description) {
        this.name = name;
        this.description = description;
    }

    public enum Status {
        NEW,
        IN_PROGRESS,
        DONE
    }

}
