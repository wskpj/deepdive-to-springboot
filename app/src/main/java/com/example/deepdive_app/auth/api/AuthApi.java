package com.example.deepdive_app.auth.api;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.deepdive_app.auth.api.dto.SignupRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "계정/인증 API")
@RequestMapping("/api/auth")
public interface AuthApi {

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    Long signUp(@Validated @RequestBody SignupRequest request);
}
