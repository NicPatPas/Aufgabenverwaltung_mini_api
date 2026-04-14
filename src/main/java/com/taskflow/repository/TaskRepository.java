package com.taskflow.repository;

import com.taskflow.entity.Priority;
import com.taskflow.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Alle Tasks nach done-Status filtern (GET /api/tasks?done=true)
    List<Task> findByDone(boolean done);

    // Alle Tasks nach Priorität filtern (GET /api/tasks?priority=HIGH)
    List<Task> findByPriority(Priority priority);

    // Kombination: nach done UND Priorität filtern
    List<Task> findByDoneAndPriority(boolean done, Priority priority);

    // Anzahl erledigter Tasks zählen (für Statistik)
    long countByDone(boolean done);

    // Tasks nach Titel durchsuchen, Groß-/Kleinschreibung egal (für Suche)
    List<Task> findByTitleContainingIgnoreCase(String title);

    // Alle Tasks eines Projekts holen
    List<Task> findByProject_Id(Long projectId);
}
