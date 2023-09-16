package ru.yandex;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Epic extends Task {

    public Epic(@NonNull String name, @NonNull String description) {
        this.name = name;
        this.description = description;
    }

    public Epic(@NonNull String name, @NonNull String description, List<Subtask> subtasks) {
        this.name = name;
        this.description = description;
    }
}
