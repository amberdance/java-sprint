package ru.yandex.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Task {

    public int id;
    public String name;
    public String description;

}
