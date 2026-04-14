package com.taskflow.service;

import com.taskflow.dto.TaskRequestDto;
import com.taskflow.dto.TaskResponseDto;
import com.taskflow.entity.Priority;
import com.taskflow.entity.Task;
import com.taskflow.exception.TaskNotFoundException;
import com.taskflow.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    // Alle Tasks holen – optional nach done und/oder priority filtern
    public List<TaskResponseDto> getAllTasks(Boolean done, Priority priority) {
        List<Task> tasks;

        if (done != null && priority != null) {
            tasks = taskRepository.findByDoneAndPriority(done, priority);
        } else if (done != null) {
            tasks = taskRepository.findByDone(done);
        } else if (priority != null) {
            tasks = taskRepository.findByPriority(priority);
        } else {
            tasks = taskRepository.findAll();
        }

        return tasks.stream()
                .map(TaskResponseDto::new)
                .toList();
    }

    // Einzelnen Task per ID holen
    public TaskResponseDto getTaskById(Long id) {
        Task task = findTaskOrThrow(id);
        return new TaskResponseDto(task);
    }

    // Neuen Task erstellen
    public TaskResponseDto createTask(TaskRequestDto dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDeadline(dto.getDeadline());
        task.setPriority(dto.getPriority());
        // done bleibt false (Default aus der Entity)

        Task saved = taskRepository.save(task);
        return new TaskResponseDto(saved);
    }

    // Task vollständig aktualisieren
    public TaskResponseDto updateTask(Long id, TaskRequestDto dto) {
        Task task = findTaskOrThrow(id);

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDeadline(dto.getDeadline());
        task.setPriority(dto.getPriority());

        Task saved = taskRepository.save(task);
        return new TaskResponseDto(saved);
    }

    // Task als erledigt / nicht erledigt markieren
    public TaskResponseDto toggleDone(Long id) {
        Task task = findTaskOrThrow(id);
        task.setDone(!task.isDone());

        Task saved = taskRepository.save(task);
        return new TaskResponseDto(saved);
    }

    // Task löschen
    public void deleteTask(Long id) {
        findTaskOrThrow(id);
        taskRepository.deleteById(id);
    }

    // Hilfsmethode: Task laden oder Exception werfen
    private Task findTaskOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }
}
