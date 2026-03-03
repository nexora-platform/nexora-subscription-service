package com.nexora.subscription.controller;

import com.nexora.subscription.dto.AssignPlanRequest;
import com.nexora.subscription.dto.CreatePlanRequest;
import com.nexora.subscription.dto.PlanResponse;
import com.nexora.subscription.dto.TenantSubscriptionResponse;
import com.nexora.subscription.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/plans")
    public ResponseEntity<PlanResponse> createPlan(@Valid @RequestBody CreatePlanRequest request) {
        return new ResponseEntity<>(subscriptionService.createPlan(request), HttpStatus.CREATED);
    }

    @GetMapping("/plans")
    public ResponseEntity<List<PlanResponse>> listPlans() {
        return ResponseEntity.ok(subscriptionService.listPlans());
    }

    @PostMapping("/tenants/{tenantId}/subscription")
    public ResponseEntity<TenantSubscriptionResponse> assignPlan(
            @PathVariable UUID tenantId,
            @Valid @RequestBody AssignPlanRequest request) {
        return new ResponseEntity<>(subscriptionService.assignPlanToTenant(tenantId, request), HttpStatus.CREATED);
    }

    @GetMapping("/tenants/{tenantId}/subscription")
    public ResponseEntity<TenantSubscriptionResponse> getTenantSubscription(@PathVariable UUID tenantId) {
        return ResponseEntity.ok(subscriptionService.getTenantSubscription(tenantId));
    }
}