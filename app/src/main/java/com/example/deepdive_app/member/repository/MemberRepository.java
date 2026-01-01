package com.example.deepdive_app.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.deepdive_app.member.entity.Member;
import com.example.lib.common.core.annotation.Throws;
import com.example.lib.common.core.exception.SystemException;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
