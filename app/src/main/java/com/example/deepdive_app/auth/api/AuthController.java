package com.example.deepdive_app.auth.api;

import org.springframework.web.bind.annotation.RestController;

import com.example.deepdive_app.auth.api.dto.SignupRequest;
import com.example.deepdive_app.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    public Long signUp(SignupRequest request) {
        return authService.signUp(request);
    }
}
