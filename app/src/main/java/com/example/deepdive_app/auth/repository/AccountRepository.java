package com.example.deepdive_app.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.deepdive_app.auth.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {}
