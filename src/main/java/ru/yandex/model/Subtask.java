package ru.yandex.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Subtask extends Epic {

    private int epicId;

    public Subtask(int id, int epicId, @NonNull String name, @NonNull String description) {
        this.id = id;
        this.epicId = epicId;
        this.name = name;
        this.description = description;
    }


    public Subtask(int epicId, @NonNull String name, @NonNull String description) {
        this.epicId = epicId;
        this.name = name;
        this.description = description;
    }

}
