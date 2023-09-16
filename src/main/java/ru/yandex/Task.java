package ru.yandex;

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

    protected String name;
    protected String description;
    protected Status status = Status.NEW;

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
