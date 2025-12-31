package com.example.deepdive_app.member.api;

import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "회원 API")
@RequestMapping("/api/members")
public interface MemberApi {
}
