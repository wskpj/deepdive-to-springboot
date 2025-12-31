package com.example.deepdive_app.coupon.api;

import org.springframework.web.bind.annotation.RestController;

import com.example.deepdive_app.coupon.dto.CouponDTO;
import com.example.deepdive_app.coupon.service.CouponService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CouponController implements CouponApi {

    private final CouponService couponService;

    @Override
    public CouponDTO issue(Long couponId, Long memberId) {
        return couponService.issue(couponId, memberId);
    }
}
