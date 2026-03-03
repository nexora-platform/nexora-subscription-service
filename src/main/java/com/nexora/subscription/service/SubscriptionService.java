package com.nexora.subscription.service;

import com.nexora.subscription.dto.AssignPlanRequest;
import com.nexora.subscription.dto.CreatePlanRequest;
import com.nexora.subscription.dto.PlanResponse;
import com.nexora.subscription.dto.TenantSubscriptionResponse;
import com.nexora.subscription.entity.Plan;
import com.nexora.subscription.entity.SubscriptionStatus;
import com.nexora.subscription.entity.TenantSubscription;
import com.nexora.subscription.repository.PlanRepository;
import com.nexora.subscription.repository.TenantSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionService {

    private final PlanRepository planRepository;
    private final TenantSubscriptionRepository subscriptionRepository;

    // Create a new plan
    public PlanResponse createPlan(CreatePlanRequest request) {
        if (planRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Plan already exists");
        }
        Plan plan = Plan.builder()
                .name(request.getName())
                .features(request.getFeatures())
                .price(request.getPrice())
                .build();
        planRepository.save(plan);
        return toPlanResponse(plan);
    }

    // List all plans
    public List<PlanResponse> listPlans() {
        return planRepository.findAll().stream().map(this::toPlanResponse).collect(Collectors.toList());
    }

    // Assign plan to tenant
    public TenantSubscriptionResponse assignPlanToTenant(UUID tenantId, AssignPlanRequest request) {
        Plan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));

        // Cancel existing active subscription
        subscriptionRepository.findByTenantId(tenantId)
                .ifPresent(sub -> {
                    sub.setStatus(SubscriptionStatus.CANCELLED);
                    sub.setEndDate(java.time.LocalDateTime.now());
                    subscriptionRepository.save(sub);
                });

        TenantSubscription subscription = TenantSubscription.builder()
                .tenantId(tenantId)
                .plan(plan)
                .status(SubscriptionStatus.ACTIVE)
                .build();

        subscriptionRepository.save(subscription);
        return toTenantSubscriptionResponse(subscription);
    }

    // Get tenant subscription
    public TenantSubscriptionResponse getTenantSubscription(UUID tenantId) {
        TenantSubscription sub = subscriptionRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant subscription not found"));
        return toTenantSubscriptionResponse(sub);
    }

    private PlanResponse toPlanResponse(Plan plan) {
        return PlanResponse.builder()
                .id(plan.getId())
                .name(plan.getName())
                .features(plan.getFeatures())
                .price(plan.getPrice())
                .build();
    }

    private TenantSubscriptionResponse toTenantSubscriptionResponse(TenantSubscription sub) {
        return TenantSubscriptionResponse.builder()
                .id(sub.getId())
                .tenantId(sub.getTenantId())
                .planId(sub.getPlan().getId())
                .status(sub.getStatus())
                .startDate(sub.getStartDate())
                .endDate(sub.getEndDate())
                .build();
    }
}