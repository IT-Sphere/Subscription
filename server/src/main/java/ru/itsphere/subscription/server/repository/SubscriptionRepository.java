package ru.itsphere.subscription.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import ru.itsphere.subscription.domain.Subscription;

/**
 * CRUD operations for subscriptions
 */
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByClientId(long clientId);

    List<Subscription> findByOrganizationId(long organizationId);

    List<Subscription> findByClientIdAndOrganizationId(long clientId, long organizationId);
}
