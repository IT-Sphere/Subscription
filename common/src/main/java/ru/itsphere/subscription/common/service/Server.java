package ru.itsphere.subscription.common.service;

import android.util.Log;

import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import ru.itsphere.subscription.domain.Client;
import ru.itsphere.subscription.domain.Organization;
import ru.itsphere.subscription.domain.Subscription;

/**
 * Responsible for access to the repository
 */
public class Server {

    private static final String tag = Server.class.getName();
    private SubscriptionServiceInvoker subscriptionServiceInvoker;
    private ClientServiceInvoker clientServiceInvoker;
    private OrganizationServiceInvoker organizationServiceInvoker;

    public Server(String serverUrl) {
        initRepository(serverUrl);
    }

    private String initRepository(String serverUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        subscriptionServiceInvoker = retrofit.create(SubscriptionServiceInvoker.class);
        Log.i(tag, String.format("SubscriptionServiceInvoker initialized with url %s", serverUrl));

        clientServiceInvoker = retrofit.create(ClientServiceInvoker.class);
        Log.i(tag, String.format("ClientServiceInvoker initialized with url %s", serverUrl));

        organizationServiceInvoker = retrofit.create(OrganizationServiceInvoker.class);
        Log.i(tag, String.format("OrganizationServiceInvoker initialized with url %s", serverUrl));
        return serverUrl;
    }

    public Call<List<Subscription>> getClientSubscriptions(Client client) {
        return subscriptionServiceInvoker.list(client.getId());
    }

    public Call<Void> createClient(Client newClient) {
        return clientServiceInvoker.create(newClient);
    }

    public Call<Client> getClientById(long id) {
        return clientServiceInvoker.get(id);
    }

    public Call<Void> subscribeClientForOrganization(Client client, Organization organization) {
        Subscription subscription = new Subscription();
        subscription.setName("Subscription for " + organization.getName());
        subscription.setClientId(client.getId());
        subscription.setOrganizationId(organization.getId());
        return subscriptionServiceInvoker.create(subscription);
    }

    public Call<Organization> getOrganizationById(int organizationId) {
        return organizationServiceInvoker.get(organizationId);
    }
}
