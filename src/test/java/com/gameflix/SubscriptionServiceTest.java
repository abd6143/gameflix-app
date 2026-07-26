package com.gameflix;

import com.gameflix.dto.SubscribeRequest;
import com.gameflix.dto.SubscriptionDto;
import com.gameflix.dto.UpgradePlanRequest;
import com.gameflix.entity.Subscription;
import com.gameflix.entity.SubscriptionPlan;
import com.gameflix.entity.SubscriptionStatus;
import com.gameflix.entity.User;
import com.gameflix.repository.SubscriptionPlanRepository;
import com.gameflix.repository.SubscriptionRepository;
import com.gameflix.service.SubscriptionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private SubscriptionPlanRepository planRepository;

    @InjectMocks
    private SubscriptionService subscriptionService;

    @Test
    void subscribe_setsStatusActive() {
        User user = createUser();
        SubscriptionPlan plan = createPlan(2L, "Standard", new BigDecimal("9.99"));
        SubscribeRequest request = new SubscribeRequest();
        request.setPlanId(2L);

        when(subscriptionRepository.findByUser(user)).thenReturn(Optional.empty());
        when(planRepository.findById(2L)).thenReturn(Optional.of(plan));
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(inv -> {
            Subscription sub = inv.getArgument(0);
            sub.setId(1L);
            return sub;
        });

        SubscriptionDto result = subscriptionService.subscribe(user, request);

        assertEquals(SubscriptionStatus.ACTIVE, result.getStatus());
        assertEquals("Standard", result.getPlanName());
    }

    @Test
    void cancel_activeSubscription_setsCancelled() {
        User user = createUser();
        Subscription subscription = createSubscription(user, SubscriptionStatus.ACTIVE);

        when(subscriptionRepository.findByUser(user)).thenReturn(Optional.of(subscription));
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(inv -> inv.getArgument(0));

        SubscriptionDto result = subscriptionService.cancelSubscription(user);

        assertEquals(SubscriptionStatus.CANCELLED, result.getStatus());
    }

    @Test
    void subscribe_whenAlreadyActive_throwsIllegalStateException() {
        User user = createUser();
        Subscription existing = createSubscription(user, SubscriptionStatus.ACTIVE);
        SubscribeRequest request = new SubscribeRequest();
        request.setPlanId(2L);

        when(subscriptionRepository.findByUser(user)).thenReturn(Optional.of(existing));

        assertThrows(IllegalStateException.class, () -> subscriptionService.subscribe(user, request));
        verify(planRepository, never()).findById(2L);
    }

    @Test
    void upgradePlan_updatesPlanReference() {
        User user = createUser();
        Subscription subscription = createSubscription(user, SubscriptionStatus.ACTIVE);
        SubscriptionPlan premiumPlan = createPlan(3L, "Premium", new BigDecimal("14.99"));

        UpgradePlanRequest request = new UpgradePlanRequest();
        request.setPlanId(3L);

        when(subscriptionRepository.findByUser(user)).thenReturn(Optional.of(subscription));
        when(planRepository.findById(3L)).thenReturn(Optional.of(premiumPlan));
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(inv -> inv.getArgument(0));

        SubscriptionDto result = subscriptionService.upgradePlan(user, request);

        assertEquals("Premium", result.getPlanName());
        assertEquals(3L, result.getPlanId());
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("gamer");
        user.setEmail("gamer@gameflix.com");
        return user;
    }

    private SubscriptionPlan createPlan(Long id, String name, BigDecimal price) {
        SubscriptionPlan plan = new SubscriptionPlan();
        plan.setId(id);
        plan.setName(name);
        plan.setPrice(price);
        plan.setMaxDevices(2);
        plan.setDownloadAllowed(false);
        plan.setDescription("Test plan");
        return plan;
    }

    private Subscription createSubscription(User user, SubscriptionStatus status) {
        Subscription subscription = new Subscription();
        subscription.setId(1L);
        subscription.setUser(user);
        subscription.setPlan(createPlan(2L, "Standard", new BigDecimal("9.99")));
        subscription.setStatus(status);
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusMonths(1));
        subscription.setAutoRenew(true);
        return subscription;
    }
}
