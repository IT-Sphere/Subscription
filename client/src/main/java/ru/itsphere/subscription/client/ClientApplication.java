package ru.itsphere.subscription.client;

import android.util.Log;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import ru.itsphere.subscription.common.utils.BlockedField;
import ru.itsphere.subscription.common.CommonApplication;
import ru.itsphere.subscription.domain.Client;

public class ClientApplication extends CommonApplication {

    public static final String SERVER_HOST = "subscription-server.herokuapp.com";
    public static final String SERVER_URL = "http://" + SERVER_HOST;
    private static final String tag = ClientApplication.class.getName();
    private static long CURRENT_USER_ID = 1;

    private BlockedField<Client> currentClient = new BlockedField<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(tag, "Getting client by id started...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isServerAvailable()) {
                    initDataFromServer();
                } else {
                    initDataFromDatabase();
                }
            }
        }).start();
    }

    private void initDataFromDatabase() {
        Log.i(tag, "Getting client by id from db started...");
        Client client = getClientService().getClientById(CURRENT_USER_ID, true);
        Log.i(tag, "Getting client by id from db stopped");
        Log.i(tag, String.format("currentClient was set %s", client == null ? client : client.getId()));
        currentClient.set(client);
    }

    private void initDataFromServer() {
        Log.i(tag, "Getting client by id from server started...");
        getServer().getClientById(CURRENT_USER_ID).enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Response<Client> response, Retrofit retrofit) {
                Log.i(tag, "Getting client by id from server stopped.");
                currentClient.set(response.body());
                if (getCurrentClient() == null) {
                    Log.i(tag, "Client hasn't registered yet");
                } else {
                    getClientService().createOrUpdate(getCurrentClient());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(tag, String.format("getClientById from server (id: %d) has thrown: ", CURRENT_USER_ID), t);
                initDataFromDatabase();
            }
        });
    }

    @Override
    protected String getServerUrl() {
        return SERVER_URL;
    }

    @Override
    protected String getServerHost() {
        return SERVER_HOST;
    }

    public Client getCurrentClient() {
        return currentClient.get();
    }
}
