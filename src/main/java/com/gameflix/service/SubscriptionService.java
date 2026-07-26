package com.gameflix.service;

import com.gameflix.dto.PlanDto;
import com.gameflix.dto.SubscribeRequest;
import com.gameflix.dto.SubscriptionDto;
import com.gameflix.dto.UpgradePlanRequest;
import com.gameflix.entity.Subscription;
import com.gameflix.entity.SubscriptionPlan;
import com.gameflix.entity.SubscriptionStatus;
import com.gameflix.entity.User;
import com.gameflix.exception.ResourceNotFoundException;
import com.gameflix.mapper.EntityMapper;
import com.gameflix.repository.SubscriptionPlanRepository;
import com.gameflix.repository.SubscriptionRepository;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubscriptionService {

    private static final Logger log = LoggerFactory.getLogger(SubscriptionService.class);

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository planRepository;

    public SubscriptionService(
            SubscriptionRepository subscriptionRepository,
            SubscriptionPlanRepository planRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.planRepository = planRepository;
    }

    public List<PlanDto> listPlans() {
        log.info("Entering listPlans");
        List<PlanDto> plans = EntityMapper.toPlanDtoList(planRepository.findAll());
        log.info("Exiting listPlans count={}", plans.size());
        return plans;
    }

    public SubscriptionDto getMySubscription(User user) {
        log.info("Entering getMySubscription userId={}", user.getId());
        Subscription subscription = subscriptionRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("No subscription found"));
        SubscriptionDto dto = EntityMapper.toSubscriptionDto(subscription);
        log.info("Exiting getMySubscription userId={}", user.getId());
        return dto;
    }

    @Transactional
    public SubscriptionDto subscribe(User user, SubscribeRequest request) {
        log.info("Entering subscribe userId={}, planId={}", user.getId(), request.getPlanId());
        subscriptionRepository.findByUser(user).ifPresent(existing -> {
            if (existing.getStatus() == SubscriptionStatus.ACTIVE) {
                throw new IllegalStateException("User already has an active subscription");
            }
        });

        SubscriptionPlan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + request.getPlanId()));

        Subscription subscription = subscriptionRepository.findByUser(user).orElse(new Subscription());
        subscription.setUser(user);
        subscription.setPlan(plan);
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusMonths(1));
        subscription.setAutoRenew(true);

        Subscription saved = subscriptionRepository.save(subscription);
        user.setSubscription(saved);

        SubscriptionDto dto = EntityMapper.toSubscriptionDto(saved);
        log.info("Exiting subscribe userId={}", user.getId());
        return dto;
    }

    @Transactional
    public SubscriptionDto pauseSubscription(User user) {
        log.info("Entering pauseSubscription userId={}", user.getId());
        Subscription subscription = getActiveOrPausedSubscription(user);
        if (subscription.getStatus() != SubscriptionStatus.ACTIVE) {
            throw new IllegalStateException("Only active subscriptions can be paused");
        }
        subscription.setStatus(SubscriptionStatus.PAUSED);
        Subscription saved = subscriptionRepository.save(subscription);
        log.info("Exiting pauseSubscription userId={}", user.getId());
        return EntityMapper.toSubscriptionDto(saved);
    }

    @Transactional
    public SubscriptionDto cancelSubscription(User user) {
        log.info("Entering cancelSubscription userId={}", user.getId());
        Subscription subscription = getActiveOrPausedSubscription(user);
        subscription.setStatus(SubscriptionStatus.CANCELLED);
        subscription.setAutoRenew(false);
        Subscription saved = subscriptionRepository.save(subscription);
        log.info("Exiting cancelSubscription userId={}", user.getId());
        return EntityMapper.toSubscriptionDto(saved);
    }

    @Transactional
    public SubscriptionDto reactivateSubscription(User user) {
        log.info("Entering reactivateSubscription userId={}", user.getId());
        Subscription subscription = subscriptionRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("No subscription found"));

        if (subscription.getStatus() != SubscriptionStatus.PAUSED
                && subscription.getStatus() != SubscriptionStatus.CANCELLED) {
            throw new IllegalStateException("Subscription cannot be reactivated from current status");
        }

        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setAutoRenew(true);
        subscription.setEndDate(LocalDate.now().plusMonths(1));
        Subscription saved = subscriptionRepository.save(subscription);
        log.info("Exiting reactivateSubscription userId={}", user.getId());
        return EntityMapper.toSubscriptionDto(saved);
    }

    @Transactional
    public SubscriptionDto upgradePlan(User user, UpgradePlanRequest request) {
        log.info("Entering upgradePlan userId={}, planId={}", user.getId(), request.getPlanId());
        Subscription subscription = subscriptionRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("No subscription found"));

        SubscriptionPlan newPlan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + request.getPlanId()));

        subscription.setPlan(newPlan);
        if (subscription.getStatus() == SubscriptionStatus.CANCELLED
                || subscription.getStatus() == SubscriptionStatus.EXPIRED) {
            subscription.setStatus(SubscriptionStatus.ACTIVE);
            subscription.setEndDate(LocalDate.now().plusMonths(1));
        }

        Subscription saved = subscriptionRepository.save(subscription);
        log.info("Exiting upgradePlan userId={}", user.getId());
        return EntityMapper.toSubscriptionDto(saved);
    }

    public List<SubscriptionDto> findAllSubscriptions() {
        log.info("Entering findAllSubscriptions");
        return subscriptionRepository.findAll().stream()
                .map(EntityMapper::toSubscriptionDto)
                .toList();
    }

    private Subscription getActiveOrPausedSubscription(User user) {
        Subscription subscription = subscriptionRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("No subscription found"));
        if (subscription.getStatus() == SubscriptionStatus.EXPIRED) {
            throw new IllegalStateException("Subscription has expired");
        }
        return subscription;
    }
}
