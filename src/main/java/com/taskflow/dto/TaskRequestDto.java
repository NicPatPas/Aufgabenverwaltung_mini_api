package com.taskflow.dto;

import com.taskflow.entity.Priority;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskRequestDto {

    @NotBlank(message = "Titel darf nicht leer sein")
    private String title;

    private String description;

    @FutureOrPresent(message = "Deadline muss heute oder in der Zukunft liegen")
    private LocalDate deadline;

    @NotNull(message = "Priorität darf nicht null sein")
    private Priority priority;

    // Optional: Task einem Projekt zuweisen
    private Long projectId;
}
