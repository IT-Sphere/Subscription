package ru.itsphere.subscription.server.service;

import java.util.List;

import ru.itsphere.subscription.domain.Subscription;

public interface SubscriptionService {

    List<Subscription> getByClientId(long clientId);

    List<Subscription> getByOrganizationId(long organizationId);

    List<Subscription> getByClientIdAndOrganizationId(long clientId, long organizationId);

    void create(Subscription subscription);
}
