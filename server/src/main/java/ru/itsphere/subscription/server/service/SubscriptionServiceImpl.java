package ru.itsphere.subscription.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import ru.itsphere.subscription.domain.Subscription;
import ru.itsphere.subscription.server.repository.SubscriptionRepository;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository repository;

    @Override
    public List<Subscription> getByClientId(long clientId) {
        return repository.findByClientId(clientId);
    }

    @Override
    public List<Subscription> getByOrganizationId(long organizationId) {
        return repository.findByOrganizationId(organizationId);
    }

    @Override
    public List<Subscription> getByClientIdAndOrganizationId(long clientId, long organizationId) {
        return repository.findByClientIdAndOrganizationId(clientId, organizationId);
    }

    @Override
    public void create(Subscription subscription) {
        repository.save(subscription);
    }
}
