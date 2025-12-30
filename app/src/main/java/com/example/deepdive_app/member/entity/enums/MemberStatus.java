package com.example.deepdive_app.member.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberStatus {
    ACTIVE("Active"),
    DELETED("Deleted"),
    LOCKED("Locked");

    private final String description;
}
