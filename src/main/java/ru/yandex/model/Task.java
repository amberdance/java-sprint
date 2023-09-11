package ru.yandex.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class Task extends ModelBase {

    private String name;
    private String description;

}
