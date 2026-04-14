package com.taskflow.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {

    @NotBlank(message = "Email darf nicht leer sein")
    @Email(message = "Email muss ein gueltiges Format haben")
    private String email;

    @NotBlank(message = "Passwort darf nicht leer sein")
    private String password;
}
