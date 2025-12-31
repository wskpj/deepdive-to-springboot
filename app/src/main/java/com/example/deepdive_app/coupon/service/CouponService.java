package com.example.deepdive_app.coupon.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.deepdive_app.coupon.dto.CouponDTO;
import com.example.deepdive_app.coupon.entity.Coupon;
import com.example.deepdive_app.coupon.entity.CouponIssue;
import com.example.deepdive_app.coupon.repository.CouponIssueRepository;
import com.example.deepdive_app.coupon.repository.CouponRepository;
import com.example.deepdive_app.global.base.BaseService;
import com.example.deepdive_app.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponService implements BaseService<CouponDTO, Coupon, Long> {

    private final CouponRepository couponRepository;
    private final CouponIssueRepository couponIssueRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CouponDTO issue(Long couponId, Long memberId) {
        // 1. 멤버 확인 (간단하게 존재 여부만)
        memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // 2. 쿠폰 확인
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));

        // 3. 중복 발급 확인
        couponIssueRepository.findByMemberIdAndCouponId(memberId, couponId)
                .ifPresent(ci -> {
                    throw new RuntimeException("Coupon already issued to this member");
                });

        // 4. 발급 처리 (수량 감소 및 내역 저장)
        coupon.issue();
        
        CouponIssue couponIssue = CouponIssue.builder()
                .memberId(memberId)
                .couponId(couponId)
                .build();
        
        couponIssueRepository.save(couponIssue);
        return toDto(coupon);
    }

    @Override
    public JpaRepository<Coupon, Long> repository() {
        return couponRepository;
    }

    @Override
    public CouponDTO toDto(Coupon entity) {
        return new CouponDTO(
            entity.getId(),
            entity.getName(),
            entity.getTotalQuantity(),
            entity.getIssuedQuantity()
        );
    }

    @Override
    public Coupon toEntity(CouponDTO dto) {
        return Coupon.builder()
                .id(dto.id())
                .name(dto.name())
                .totalQuantity(dto.totalQuantity())
                .issuedQuantity(dto.issuedQuantity())
                .build();
    }
}
