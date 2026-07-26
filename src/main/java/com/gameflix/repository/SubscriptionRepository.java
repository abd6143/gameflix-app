package com.gameflix.repository;

import com.gameflix.entity.Subscription;
import com.gameflix.entity.SubscriptionStatus;
import com.gameflix.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByUser(User user);

    Optional<Subscription> findByUserId(Long userId);

    List<Subscription> findByStatus(SubscriptionStatus status);

    long countByStatus(SubscriptionStatus status);
}
