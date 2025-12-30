package com.example.deepdive_app.auth.event;

import com.example.deepdive_app.infrastructure.AppEventSource;
import com.example.deepdive_app.infrastructure.AppEventType;
import com.example.lib.event.core.BaseEvent;

import lombok.Getter;
import lombok.Setter;

@Getter
public class AccountCreatedEvent extends BaseEvent {

    private final Long accountId;
    private final String name;
    private final String email;

    @Setter
    private Long memberId;

    public AccountCreatedEvent(Long accountId, String name, String email) {
        super(AppEventType.MEMBER_SIGNED_UP, AppEventSource.AUTH);
        this.accountId = accountId;
        this.name = name;
        this.email = email;
    }
}
