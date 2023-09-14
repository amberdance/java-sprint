package ru.yandex.model;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Subtask extends Epic {

    private int epicId;

    public Subtask(int epicId) {
        this.epicId = epicId;
    }

    public Subtask(int epicId, @NonNull String name, @NonNull String description) {
        this.epicId = epicId;
        this.name = name;
        this.description = description;
    }

    public Subtask(@NonNull String name, @NonNull String description) {
        this.name = name;
        this.description = description;
    }
}
