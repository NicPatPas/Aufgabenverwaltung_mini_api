package com.taskflow.exception;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email) {
        super("Ein Benutzer mit der Email '" + email + "' existiert bereits");
    }
}
