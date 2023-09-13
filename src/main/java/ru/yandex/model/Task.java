package ru.yandex.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Task {

    protected int id;
    protected String name;
    protected String description;
    protected Status status = Status.NEW;
    private List<Epic> epicsList = new ArrayList<>();

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }


}
