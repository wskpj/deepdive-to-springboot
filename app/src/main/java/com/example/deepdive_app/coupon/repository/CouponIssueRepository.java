package com.example.deepdive_app.coupon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.deepdive_app.coupon.entity.CouponIssue;

public interface CouponIssueRepository extends JpaRepository<CouponIssue, Long> {
    Optional<CouponIssue> findByMemberIdAndCouponId(Long memberId, Long couponId);
}
