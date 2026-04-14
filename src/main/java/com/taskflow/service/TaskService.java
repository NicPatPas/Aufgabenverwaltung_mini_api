package com.taskflow.service;

import com.taskflow.dto.TaskRequestDto;
import com.taskflow.dto.TaskResponseDto;
import com.taskflow.dto.TaskStatsDto;
import com.taskflow.entity.Priority;
import com.taskflow.entity.Task;
import com.taskflow.entity.User;
import com.taskflow.exception.ForbiddenException;
import com.taskflow.exception.TaskNotFoundException;
import com.taskflow.repository.TaskRepository;
import com.taskflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectService projectService;
    private final UserRepository userRepository;

    // Alle Tasks des eingeloggten Users – optional gefiltert
    public List<TaskResponseDto> getAllTasks(Boolean done, Priority priority) {
        User currentUser = getCurrentUser();
        List<Task> tasks;

        if (done != null && priority != null) {
            tasks = taskRepository.findByOwnerAndDoneAndPriority(currentUser, done, priority);
        } else if (done != null) {
            tasks = taskRepository.findByOwnerAndDone(currentUser, done);
        } else if (priority != null) {
            tasks = taskRepository.findByOwnerAndPriority(currentUser, priority);
        } else {
            tasks = taskRepository.findByOwner(currentUser);
        }

        return tasks.stream().map(TaskResponseDto::new).toList();
    }

    // Einzelnen Task holen – nur wenn er dem eingeloggten User gehört
    public TaskResponseDto getTaskById(Long id) {
        return new TaskResponseDto(findTaskOrThrow(id));
    }

    // Neuen Task erstellen – Owner wird automatisch gesetzt
    public TaskResponseDto createTask(TaskRequestDto dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDeadline(dto.getDeadline());
        task.setPriority(dto.getPriority());
        task.setOwner(getCurrentUser());

        if (dto.getProjectId() != null) {
            task.setProject(projectService.findProjectOrThrow(dto.getProjectId()));
        }

        return new TaskResponseDto(taskRepository.save(task));
    }

    // Task aktualisieren – nur der Owner darf das
    public TaskResponseDto updateTask(Long id, TaskRequestDto dto) {
        Task task = findTaskOrThrow(id);

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDeadline(dto.getDeadline());
        task.setPriority(dto.getPriority());

        if (dto.getProjectId() != null) {
            task.setProject(projectService.findProjectOrThrow(dto.getProjectId()));
        } else {
            task.setProject(null);
        }

        return new TaskResponseDto(taskRepository.save(task));
    }

    // Done umschalten – nur der Owner darf das
    public TaskResponseDto toggleDone(Long id) {
        Task task = findTaskOrThrow(id);
        task.setDone(!task.isDone());
        return new TaskResponseDto(taskRepository.save(task));
    }

    // Task löschen – nur der Owner darf das
    public void deleteTask(Long id) {
        findTaskOrThrow(id);
        taskRepository.deleteById(id);
    }

    // Statistik – nur Tasks des eingeloggten Users
    public TaskStatsDto getStats() {
        User currentUser = getCurrentUser();
        long total = taskRepository.countByOwner(currentUser);
        long done  = taskRepository.countByOwnerAndDone(currentUser, true);
        return new TaskStatsDto(total, done);
    }

    // Suche – nur im eigenen Task-Pool suchen
    public List<TaskResponseDto> searchByTitle(String title) {
        User currentUser = getCurrentUser();
        return taskRepository.findByOwnerAndTitleContainingIgnoreCase(currentUser, title)
                .stream()
                .map(TaskResponseDto::new)
                .toList();
    }

    // ── Hilfsmethoden ────────────────────────────────────────────────────────

    // Task laden + Eigentümer prüfen (404 oder 403)
    private Task findTaskOrThrow(Long id) {
        User currentUser = getCurrentUser();

        // Erst prüfen ob Task existiert
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        // Dann prüfen ob der eingeloggte User der Owner ist
        if (!task.getOwner().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("Du hast keinen Zugriff auf diesen Task");
        }

        return task;
    }

    // Den aktuell eingeloggten User aus dem SecurityContext lesen
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Eingeloggter User nicht gefunden"));
    }
}
