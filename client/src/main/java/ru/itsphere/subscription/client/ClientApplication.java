package ru.itsphere.subscription.client;

import android.util.Log;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import ru.itsphere.subscription.common.service.ServerRelatedApplication;
import ru.itsphere.subscription.common.service.BlockedField;
import ru.itsphere.subscription.domain.Client;

public class ClientApplication extends ServerRelatedApplication {

    public static final String SERVER_URL = "http://192.168.43.176:8080";
    private static final String tag = ClientApplication.class.getName();
    private static long CURRENT_USER_ID = 1;

    private BlockedField<Client> currentClient = new BlockedField<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(tag, "Getting client by id started...");
        getServer().getClientById(CURRENT_USER_ID).enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Response<Client> response, Retrofit retrofit) {
                Log.i(tag, "Getting client by id stopped.");
                currentClient.set(response.body());
                if (getCurrentClient() == null) {
                    Log.i(tag, "Client hasn't registered yet");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(tag, String.format("getClientById (id: %d) has thrown: ", CURRENT_USER_ID), t);
            }
        });
    }

    @Override
    protected String getServerUrl() {
        return SERVER_URL;
    }

    public Client getCurrentClient() {
        return currentClient.get();
    }
}
