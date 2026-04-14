package com.taskflow.dto;

import com.taskflow.entity.Priority;
import com.taskflow.entity.Task;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TaskResponseDto {

    private final Long id;
    private final String title;
    private final String description;
    private final LocalDate deadline;
    private final boolean done;
    private final Priority priority;

    // Konstruktor nimmt direkt eine Task-Entity entgegen und mappt die Felder
    public TaskResponseDto(Task task) {
        this.id          = task.getId();
        this.title       = task.getTitle();
        this.description = task.getDescription();
        this.deadline    = task.getDeadline();
        this.done        = task.isDone();
        this.priority    = task.getPriority();
    }
}
