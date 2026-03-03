package com.nexora.subscription.dto;

import com.nexora.subscription.entity.SubscriptionStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TenantSubscriptionResponse {
    private UUID id;
    private UUID tenantId;
    private UUID planId;
    private SubscriptionStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}