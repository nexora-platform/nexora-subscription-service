package com.nexora.subscription.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class AssignPlanRequest {
    @NotNull
    private UUID planId;
}