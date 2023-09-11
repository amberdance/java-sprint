package ru.yandex.model;

import lombok.ToString;

import java.util.UUID;

@ToString
public class ModelBase {

    protected UUID id;

    public ModelBase() {
        this.id = UUID.randomUUID();
    }
}
