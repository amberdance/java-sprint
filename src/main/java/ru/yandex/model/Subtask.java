package ru.yandex.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;


@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Subtask extends Task {

    public Subtask(String name, String description) {
        this.name = name;
        this.description = description;
    }


}
