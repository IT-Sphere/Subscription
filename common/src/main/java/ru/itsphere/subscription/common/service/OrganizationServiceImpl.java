package ru.itsphere.subscription.common.service;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;

import ru.itsphere.subscription.common.dao.DAOManager;
import ru.itsphere.subscription.domain.Organization;
import ru.itsphere.subscription.domain.Subscription;

public class OrganizationServiceImpl implements OrganizationService {

    private DAOManager daoManager;

    public OrganizationServiceImpl(DAOManager daoManager) {
        this.daoManager = daoManager;
    }

    @Override
    public Organization getOrganizationById(long id, boolean eager) {
        Organization organization = daoManager.getOrganizationDao().queryForId((int) id);
        if (eager) {
            List<Subscription> subscriptions = daoManager.getSubscriptionDao().queryForEq("organizationId", id);
            organization.setSubscriptions(new HashSet<>(subscriptions));
        }
        return organization;
    }

    @Override
    public void createOrUpdate(final Organization organization) {
        daoManager.getOrganizationDao().callBatchTasks(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                daoManager.getOrganizationDao().createOrUpdate(organization);
                for (Subscription subscription : organization.getSubscriptions()) {
                    daoManager.getSubscriptionDao().createOrUpdate(subscription);
                }
                return null;
            }
        });
    }
}
