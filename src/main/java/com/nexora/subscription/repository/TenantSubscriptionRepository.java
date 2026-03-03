package com.nexora.subscription.repository;

import com.nexora.subscription.entity.TenantSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TenantSubscriptionRepository extends JpaRepository<TenantSubscription, UUID> {
    Optional<TenantSubscription> findByTenantId(UUID tenantId);
    boolean existsByTenantIdAndStatus(UUID tenantId, com.nexora.subscription.entity.SubscriptionStatus status);
}