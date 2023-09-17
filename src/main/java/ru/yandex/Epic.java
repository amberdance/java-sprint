package ru.yandex;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Epic extends Task {

    List<Integer> subtaskIds = new ArrayList<>();

    public Epic(int id, @NonNull String name, @NonNull String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Epic(@NonNull String name, @NonNull String description) {
        this.name = name;
        this.description = description;
    }


}
