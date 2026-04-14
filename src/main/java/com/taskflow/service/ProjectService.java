package com.taskflow.service;

import com.taskflow.dto.ProjectRequestDto;
import com.taskflow.dto.ProjectResponseDto;
import com.taskflow.dto.TaskResponseDto;
import com.taskflow.entity.Project;
import com.taskflow.exception.ProjectNotFoundException;
import com.taskflow.repository.ProjectRepository;
import com.taskflow.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    // Alle Projekte holen
    public List<ProjectResponseDto> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(ProjectResponseDto::new)
                .toList();
    }

    // Einzelnes Projekt holen
    public ProjectResponseDto getProjectById(Long id) {
        Project project = findProjectOrThrow(id);
        return new ProjectResponseDto(project);
    }

    // Neues Projekt erstellen
    public ProjectResponseDto createProject(ProjectRequestDto dto) {
        Project project = new Project();
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());

        Project saved = projectRepository.save(project);
        return new ProjectResponseDto(saved);
    }

    // Projekt aktualisieren
    public ProjectResponseDto updateProject(Long id, ProjectRequestDto dto) {
        Project project = findProjectOrThrow(id);
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());

        Project saved = projectRepository.save(project);
        return new ProjectResponseDto(saved);
    }

    // Projekt löschen
    public void deleteProject(Long id) {
        findProjectOrThrow(id);
        projectRepository.deleteById(id);
    }

    // Alle Tasks eines Projekts holen
    public List<TaskResponseDto> getTasksByProject(Long projectId) {
        findProjectOrThrow(projectId);
        return taskRepository.findByProject_Id(projectId)
                .stream()
                .map(TaskResponseDto::new)
                .toList();
    }

    // Hilfsmethode
    public Project findProjectOrThrow(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
    }
}
