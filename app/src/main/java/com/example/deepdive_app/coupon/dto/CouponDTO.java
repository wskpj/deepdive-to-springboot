package com.example.deepdive_app.coupon.dto;

public record CouponDTO(
    Long id,
    String name,
    int issuedQuantity,
    int totalQuantity
) {
}
