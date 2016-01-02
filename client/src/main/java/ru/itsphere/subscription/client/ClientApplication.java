package ru.itsphere.subscription.client;

import android.app.Application;
import android.util.Log;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import ru.itsphere.subscription.common.service.Server;
import ru.itsphere.subscription.domain.Client;

public class ClientApplication extends Application {

    public static final String SERVER_URL = "http://192.168.43.176:8080";
    private static final String tag = ClientApplication.class.getName();
    private static long CURRENT_USER_ID = 1;

    private Server server;
    private Client currentClient;

    @Override
    public void onCreate() {
        server = new Server(SERVER_URL);
        Log.i(tag, "Getting client by id started...");
        server.getClientById(CURRENT_USER_ID).enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Response<Client> response, Retrofit retrofit) {
                Log.i(tag, "Getting client by id stopped.");
                currentClient = response.body();
                if (currentClient == null) {
                    Log.i(tag, "Client hasn't registered yet");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(tag, String.format("getClientById (id: %d) has thrown: ", CURRENT_USER_ID), t);
            }
        });
        super.onCreate();
    }

    public Server getServer() {
        return server;
    }

    public Client getCurrentClient() {
        return currentClient;
    }
}
