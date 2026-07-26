package com.gameflix.dto;

import jakarta.validation.constraints.NotNull;

public class SubscribeRequest {

    @NotNull(message = "Plan ID is required")
    private Long planId;

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }
}
