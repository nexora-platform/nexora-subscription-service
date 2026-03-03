package com.nexora.subscription.repository;

import com.nexora.subscription.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface PlanRepository extends JpaRepository<Plan, UUID> {
    Optional<Plan> findByName(String name);
    boolean existsByName(String name);
}