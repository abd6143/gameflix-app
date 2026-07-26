package com.gameflix.repository;

import com.gameflix.entity.SubscriptionPlan;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {

    Optional<SubscriptionPlan> findByNameIgnoreCase(String name);
}
