package com.gameflix.dto;

import com.gameflix.entity.SubscriptionStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public class SubscriptionDto {

    private Long id;
    private Long userId;
    private Long planId;
    private String planName;
    private BigDecimal planPrice;
    private SubscriptionStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean autoRenew;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public BigDecimal getPlanPrice() {
        return planPrice;
    }

    public void setPlanPrice(BigDecimal planPrice) {
        this.planPrice = planPrice;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(boolean autoRenew) {
        this.autoRenew = autoRenew;
    }
}
