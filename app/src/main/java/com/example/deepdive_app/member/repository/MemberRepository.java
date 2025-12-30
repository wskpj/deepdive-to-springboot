package com.example.deepdive_app.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.deepdive_app.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
