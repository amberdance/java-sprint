package ru.yandex.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;


@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Epic extends Task {

    public Epic(String name, String description) {
        this.name = name;
        this.description = description;
    }


}
