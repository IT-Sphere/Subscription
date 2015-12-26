package ru.itsphere.subscription.common.service;

import android.util.Log;

import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import ru.itsphere.subscription.domain.Client;
import ru.itsphere.subscription.domain.Subscription;

/**
 * Responsible for access to the repository
 */
public class Repository {

    public static final String SERVER_URL = "http://10.0.2.2:8080";
    private static final String tag = Repository.class.getName();

    private SubscriptionServiceInvoker subscriptionServiceInvoker;
    private RegistrationServiceInvoker registrationServiceInvoker;

    private static long CURRENT_USER_ID = 1;

    public Repository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        subscriptionServiceInvoker = retrofit.create(SubscriptionServiceInvoker.class);
        Log.i(tag, String.format("SubscriptionServiceInvoker initialized with url %s", SERVER_URL));

        registrationServiceInvoker = retrofit.create(RegistrationServiceInvoker.class);
        Log.i(tag, String.format("RegistrationServiceInvoker initialized with url %s", SERVER_URL));
    }

    public Call<List<Subscription>> getAllSubscriptions() {
        final long clientId = 0;
        return subscriptionServiceInvoker.list(clientId);
    }

    public Call<Void> registerClient(Client newClient) {
        return registrationServiceInvoker.register(newClient);
    }

    public Call<Client> getClientById(long clientId) {
        return registrationServiceInvoker.get(clientId);
    }

    public Call<Client> getCurrentUser() {
        return registrationServiceInvoker.get(CURRENT_USER_ID);
    }
}
