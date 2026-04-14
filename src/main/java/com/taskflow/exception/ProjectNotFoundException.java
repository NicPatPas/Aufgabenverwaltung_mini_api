package com.taskflow.exception;

public class ProjectNotFoundException extends RuntimeException {

    public ProjectNotFoundException(Long id) {
        super("Projekt mit ID " + id + " wurde nicht gefunden");
    }
}
