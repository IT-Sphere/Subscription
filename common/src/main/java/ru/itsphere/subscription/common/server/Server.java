package ru.itsphere.subscription.common.server;

import android.util.Log;

import java.util.List;

import retrofit.Call;
import retrofit.Retrofit;
import ru.itsphere.subscription.domain.Client;
import ru.itsphere.subscription.domain.Organization;
import ru.itsphere.subscription.domain.Subscription;

/**
 * Responsible for access to the repository
 */
public class Server {

    private static final String tag = Server.class.getName();
    private SubscriptionServerInvoker subscriptionServerInvoker;
    private ClientServerInvoker clientServerInvoker;
    private OrganizationServerInvoker organizationServerInvoker;

    public Server(String serverUrl) {
        initRepository(serverUrl);
    }

    private String initRepository(String serverUrl) {
        Retrofit retrofit = RetrofitFactory.getRetrofit(serverUrl);

        subscriptionServerInvoker = retrofit.create(SubscriptionServerInvoker.class);
        Log.i(tag, String.format("SubscriptionServerInvoker initialized with url %s", serverUrl));

        clientServerInvoker = retrofit.create(ClientServerInvoker.class);
        Log.i(tag, String.format("ClientServerInvoker initialized with url %s", serverUrl));

        organizationServerInvoker = retrofit.create(OrganizationServerInvoker.class);
        Log.i(tag, String.format("OrganizationServerInvoker initialized with url %s", serverUrl));
        return serverUrl;
    }

    public Call<List<Subscription>> getClientSubscriptions(Client client) {
        return subscriptionServerInvoker.list(client.getId());
    }

    public Call<Void> createClient(Client newClient) {
        return clientServerInvoker.save(newClient);
    }

    public Call<Void> createOrganization(Organization newOrganization) {
        return organizationServerInvoker.save(newOrganization);
    }

    public Call<Client> getClientById(long id) {
        return clientServerInvoker.get(id);
    }

    public Call<Void> subscribeClientForOrganization(Client client, Organization organization) {
        Subscription subscription = new Subscription();
        subscription.setName("Subscription for " + organization.getName());
        subscription.setClientId(client.getId());
        subscription.setOrganizationId(organization.getId());
        return subscriptionServerInvoker.save(subscription);
    }

    public Call<Organization> getOrganizationById(int organizationId) {
        return organizationServerInvoker.get(organizationId);
    }

    public Call<Void> registerVisit(Subscription subscription) {
        int visitsNumber = subscription.getVisitsNumber();
        if (visitsNumber > 0) {
            visitsNumber--;
            subscription.setVisitsNumber(visitsNumber);
            return subscriptionServerInvoker.save(subscription);
        }
        return null;
    }
}
