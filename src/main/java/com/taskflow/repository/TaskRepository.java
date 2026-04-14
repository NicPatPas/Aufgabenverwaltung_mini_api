package com.taskflow.repository;

import com.taskflow.entity.Priority;
import com.taskflow.entity.Task;
import com.taskflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // ── Ohne Owner-Filter (für Admin / Projekt-Ansicht) ───────────────────────

    // Alle Tasks eines Projekts holen
    List<Task> findByProject_Id(Long projectId);

    // ── Mit Owner-Filter (jeder User sieht nur seine Tasks) ──────────────────

    // Alle Tasks eines Users
    List<Task> findByOwner(User owner);

    // Gefiltert nach done
    List<Task> findByOwnerAndDone(User owner, boolean done);

    // Gefiltert nach Priorität
    List<Task> findByOwnerAndPriority(User owner, Priority priority);

    // Gefiltert nach done UND Priorität
    List<Task> findByOwnerAndDoneAndPriority(User owner, boolean done, Priority priority);

    // Suche im Titel, nur eigene Tasks
    List<Task> findByOwnerAndTitleContainingIgnoreCase(User owner, String title);

    // Einzelnen Task per ID — nur wenn er dem User gehört
    Optional<Task> findByIdAndOwner(Long id, User owner);

    // ── Statistik für den eingeloggten User ──────────────────────────────────

    long countByOwner(User owner);

    long countByOwnerAndDone(User owner, boolean done);
}
