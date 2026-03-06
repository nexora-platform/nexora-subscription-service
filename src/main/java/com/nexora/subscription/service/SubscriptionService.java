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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SubscriptionService {

    private final PlanRepository planRepository;
    private final TenantSubscriptionRepository subscriptionRepository;

    public PlanResponse createPlan(CreatePlanRequest request) {

        log.info("Creating plan: {}", request.getName());

        if (planRepository.existsByName(request.getName())) {
            log.warn("Plan creation failed, already exists: {}", request.getName());
            throw new IllegalArgumentException("Plan already exists");
        }

        Plan plan = Plan.builder()
                .name(request.getName())
                .features(request.getFeatures())
                .price(request.getPrice())
                .build();

        planRepository.save(plan);

        log.info("Plan created successfully id={} name={}", plan.getId(), plan.getName());

        return toPlanResponse(plan);
    }

    public List<PlanResponse> listPlans() {

        log.info("Fetching all subscription plans");

        return planRepository.findAll()
                .stream()
                .map(this::toPlanResponse)
                .collect(Collectors.toList());
    }

    public TenantSubscriptionResponse assignPlanToTenant(UUID tenantId, AssignPlanRequest request) {

        log.info("Assigning plan {} to tenant {}", request.getPlanId(), tenantId);

        Plan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));

        subscriptionRepository.findByTenantId(tenantId)
                .ifPresent(sub -> {
                    log.info("Cancelling existing subscription for tenant {}", tenantId);
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

        log.info("Subscription activated tenant={} plan={}", tenantId, plan.getName());

        return toTenantSubscriptionResponse(subscription);
    }

    public TenantSubscriptionResponse getTenantSubscription(UUID tenantId) {

        log.info("Fetching subscription for tenant {}", tenantId);

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