package com.taskflow.service;

import com.taskflow.dto.AuthResponseDto;
import com.taskflow.dto.LoginRequestDto;
import com.taskflow.dto.RegisterRequestDto;
import com.taskflow.entity.User;
import com.taskflow.exception.EmailAlreadyExistsException;
import com.taskflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDto register(RegisterRequestDto dto) {
        // Prüfen ob Email bereits vergeben
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException(dto.getEmail());
        }

        // Neuen User anlegen, Passwort hashen (niemals Plaintext speichern!)
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        userRepository.save(user);

        // Direkt nach der Registrierung einloggen → Token zurückgeben
        String token = jwtService.generateToken(user);
        return new AuthResponseDto(token, user.getEmail(), user.getName());
    }

    public AuthResponseDto login(LoginRequestDto dto) {
        // Spring Security überprüft Email + Passwort (wirft Exception bei Fehler)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        // Authentifizierung war erfolgreich → User laden und Token generieren
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow();
        String token = jwtService.generateToken(user);
        return new AuthResponseDto(token, user.getEmail(), user.getName());
    }
}
