package com.example.deepdive_app.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.deepdive_app.coupon.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
