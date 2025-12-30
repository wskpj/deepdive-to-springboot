package com.example.deepdive_app.member.service;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.deepdive_app.auth.event.AccountCreatedEvent;
import com.example.deepdive_app.member.entity.Member;
import com.example.deepdive_app.member.entity.enums.MemberRole;
import com.example.deepdive_app.member.entity.enums.MemberStatus;
import com.example.deepdive_app.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberEventListener {

    private final MemberRepository memberRepository;

    @EventListener
    @Transactional
    public void onAccountCreated(AccountCreatedEvent event) {
        log.info("[MemberEventListener] Creating profile for account: {}", event.getAccountId());

        Member member = Member.builder()
                .name(event.getName())
                .role(MemberRole.USER)
                .status(MemberStatus.ACTIVE)
                .build();

        Member savedMember = memberRepository.save(member);
        event.setMemberId(savedMember.getId());
        
        log.info("[MemberEventListener] Profile created for member: {}", savedMember.getName());
    }
}
