package ru.itsphere.subscription.common.service;

import java.util.List;
import java.util.concurrent.Callable;

import ru.itsphere.subscription.common.dao.DAOManager;
import ru.itsphere.subscription.domain.Client;
import ru.itsphere.subscription.domain.Subscription;

public class ClientServiceImpl implements ClientService {

    private DAOManager daoManager;

    public ClientServiceImpl(DAOManager daoManager) {
        this.daoManager = daoManager;
    }

    @Override
    public Client getClientById(long id, boolean eager) {
        Client client = daoManager.getClientDao().queryForId((int) id);
        if (eager) {
            List<Subscription> subscriptions = daoManager.getSubscriptionDao().queryForEq("clientId", id);
            client.setSubscriptions(subscriptions);
        }
        return client;
    }

    @Override
    public void createOrUpdate(final Client client) {
        daoManager.getClientDao().callBatchTasks(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                daoManager.getClientDao().createOrUpdate(client);
                for (Subscription subscription : client.getSubscriptions()) {
                    daoManager.getSubscriptionDao().createOrUpdate(subscription);
                }
                return null;
            }
        });
    }
}
