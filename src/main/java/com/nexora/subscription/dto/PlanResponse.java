package com.nexora.subscription.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PlanResponse {
    private UUID id;
    private String name;
    private String features;
    private Double price;
}