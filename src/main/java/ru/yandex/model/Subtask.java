package ru.yandex.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Subtask extends Epic {

    public Subtask(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
