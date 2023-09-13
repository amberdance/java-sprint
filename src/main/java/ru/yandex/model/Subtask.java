package ru.yandex.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Subtask extends Epic {

    private int epicId;

    public Subtask(int epicId, String name, String description) {
        this.epicId = epicId;
        this.name = name;
        this.description = description;
    }

    public Subtask(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
