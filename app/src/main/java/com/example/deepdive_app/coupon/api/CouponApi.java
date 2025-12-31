package com.example.deepdive_app.coupon.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.deepdive_app.coupon.dto.CouponDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "쿠폰 API")
@RequestMapping("/api/coupons")
public interface CouponApi {

    @Operation(summary = "쿠폰 발행")
    @PostMapping("/issue")
    CouponDTO issue(Long couponId, Long memberId);

}