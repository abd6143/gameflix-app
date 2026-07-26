package com.gameflix.controller;

import com.gameflix.dto.ApiResponse;
import com.gameflix.dto.PlanDto;
import com.gameflix.dto.SubscribeRequest;
import com.gameflix.dto.SubscriptionDto;
import com.gameflix.dto.UpgradePlanRequest;
import com.gameflix.entity.User;
import com.gameflix.service.SubscriptionService;
import com.gameflix.util.SecurityUtils;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final SecurityUtils securityUtils;

    public SubscriptionController(SubscriptionService subscriptionService, SecurityUtils securityUtils) {
        this.subscriptionService = subscriptionService;
        this.securityUtils = securityUtils;
    }

    @GetMapping("/plans")
    public ResponseEntity<ApiResponse<List<PlanDto>>> listPlans() {
        List<PlanDto> plans = subscriptionService.listPlans();
        return ResponseEntity.ok(ApiResponse.ok(plans));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<SubscriptionDto>> getMySubscription() {
        User user = securityUtils.getCurrentUser();
        SubscriptionDto subscription = subscriptionService.getMySubscription(user);
        return ResponseEntity.ok(ApiResponse.ok(subscription));
    }

    @PostMapping("/subscribe")
    public ResponseEntity<ApiResponse<SubscriptionDto>> subscribe(@Valid @RequestBody SubscribeRequest request) {
        User user = securityUtils.getCurrentUser();
        SubscriptionDto subscription = subscriptionService.subscribe(user, request);
        return ResponseEntity.ok(ApiResponse.ok(subscription, "Subscribed successfully"));
    }

    @PutMapping("/my/pause")
    public ResponseEntity<ApiResponse<SubscriptionDto>> pauseSubscription() {
        User user = securityUtils.getCurrentUser();
        SubscriptionDto subscription = subscriptionService.pauseSubscription(user);
        return ResponseEntity.ok(ApiResponse.ok(subscription, "Subscription paused"));
    }

    @PutMapping("/my/cancel")
    public ResponseEntity<ApiResponse<SubscriptionDto>> cancelSubscription() {
        User user = securityUtils.getCurrentUser();
        SubscriptionDto subscription = subscriptionService.cancelSubscription(user);
        return ResponseEntity.ok(ApiResponse.ok(subscription, "Subscription cancelled"));
    }

    @PutMapping("/my/reactivate")
    public ResponseEntity<ApiResponse<SubscriptionDto>> reactivateSubscription() {
        User user = securityUtils.getCurrentUser();
        SubscriptionDto subscription = subscriptionService.reactivateSubscription(user);
        return ResponseEntity.ok(ApiResponse.ok(subscription, "Subscription reactivated"));
    }

    @PutMapping("/my/upgrade")
    public ResponseEntity<ApiResponse<SubscriptionDto>> upgradePlan(@Valid @RequestBody UpgradePlanRequest request) {
        User user = securityUtils.getCurrentUser();
        SubscriptionDto subscription = subscriptionService.upgradePlan(user, request);
        return ResponseEntity.ok(ApiResponse.ok(subscription, "Plan updated"));
    }
}
