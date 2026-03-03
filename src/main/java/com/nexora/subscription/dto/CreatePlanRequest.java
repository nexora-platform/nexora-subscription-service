package com.nexora.subscription.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreatePlanRequest {
    @NotBlank
    private String name;

    private String features;

    @NotNull
    private Double price;
}