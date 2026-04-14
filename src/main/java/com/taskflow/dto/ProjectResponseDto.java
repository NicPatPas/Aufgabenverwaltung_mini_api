package com.taskflow.dto;

import com.taskflow.entity.Project;
import lombok.Getter;

@Getter
public class ProjectResponseDto {

    private final Long id;
    private final String name;
    private final String description;

    public ProjectResponseDto(Project project) {
        this.id          = project.getId();
        this.name        = project.getName();
        this.description = project.getDescription();
    }
}
