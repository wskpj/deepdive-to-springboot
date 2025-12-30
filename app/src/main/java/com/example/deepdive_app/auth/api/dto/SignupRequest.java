package com.example.deepdive_app.auth.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
    @NotBlank
    @Email
    @Schema(example = "user@example.com")
    String email,

    @NotBlank
    @Size(min = 8)
    @Schema(example = "password1@Q")
    String password,

    @NotBlank
    @Schema(example = "홍길동")
    String name
) {
}

