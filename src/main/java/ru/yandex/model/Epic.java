package ru.yandex.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Getter
@Setter
public class Epic extends Task {

    private List<Subtask> subtasks = new ArrayList<>();

    public Epic(String name, String description) {
        this.name = name;
        this.description = description;
    }


}
