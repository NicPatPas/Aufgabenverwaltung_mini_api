package com.taskflow.controller;

import com.taskflow.dto.TaskRequestDto;
import com.taskflow.dto.TaskResponseDto;
import com.taskflow.dto.TaskStatsDto;
import com.taskflow.entity.Priority;
import com.taskflow.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // GET /api/tasks
    // GET /api/tasks?done=true
    // GET /api/tasks?priority=HIGH
    // GET /api/tasks?done=false&priority=LOW
    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAllTasks(
            @RequestParam(required = false) Boolean done,
            @RequestParam(required = false) Priority priority
    ) {
        return ResponseEntity.ok(taskService.getAllTasks(done, priority));
    }

    // GET /api/tasks/stats
    @GetMapping("/stats")
    public ResponseEntity<TaskStatsDto> getStats() {
        return ResponseEntity.ok(taskService.getStats());
    }

    // GET /api/tasks/search?title=spring
    @GetMapping("/search")
    public ResponseEntity<List<TaskResponseDto>> searchByTitle(
            @RequestParam String title
    ) {
        return ResponseEntity.ok(taskService.searchByTitle(title));
    }

    // GET /api/tasks/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    // POST /api/tasks
    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody TaskRequestDto dto) {
        TaskResponseDto created = taskService.createTask(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT /api/tasks/{id}
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequestDto dto
    ) {
        return ResponseEntity.ok(taskService.updateTask(id, dto));
    }

    // PATCH /api/tasks/{id}/toggle
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TaskResponseDto> toggleDone(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.toggleDone(id));
    }

    // DELETE /api/tasks/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
