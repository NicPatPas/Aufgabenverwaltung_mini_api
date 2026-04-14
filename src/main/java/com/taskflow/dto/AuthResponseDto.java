package com.taskflow.dto;

import lombok.Getter;

@Getter
public class AuthResponseDto {

    private final String token;
    private final String email;
    private final String name;

    public AuthResponseDto(String token, String email, String name) {
        this.token = token;
        this.email = email;
        this.name  = name;
    }
}
