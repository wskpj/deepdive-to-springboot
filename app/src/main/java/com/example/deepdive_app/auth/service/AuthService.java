package com.example.deepdive_app.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.deepdive_app.auth.api.dto.SignupRequest;
import com.example.deepdive_app.auth.entity.Account;
import com.example.deepdive_app.auth.event.AccountCreatedEvent;
import com.example.deepdive_app.auth.repository.AccountRepository;
import com.example.deepdive_app.infrastructure.DomainException;
import com.example.deepdive_app.infrastructure.AppErrorType;
import com.example.lib.event.core.EventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final EventPublisher eventPublisher;

    @Transactional
    public Long signUp(SignupRequest request) {
        if (accountRepository.existsByEmail(request.email())) throw new DomainException(AppErrorType.MEMBER_EMAIL_EXISTING);

        Account account = Account.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();

        AccountCreatedEvent event = new AccountCreatedEvent(
                account.getId(),
                request.name(),
                account.getEmail());

        eventPublisher.publish(event);
        if (event.getMemberId() == null) throw new DomainException(AppErrorType.MEMBER_CREATION_FAILED);

        account.setMemberId(event.getMemberId());
        Account savedAccount = accountRepository.save(account);

        log.info("Successfully signed up: {}", savedAccount.getEmail());
        return savedAccount.getId();
    }
}
