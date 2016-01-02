package ru.itsphere.subscription.provider;

import android.app.Application;
import android.util.Log;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import ru.itsphere.subscription.common.service.Server;
import ru.itsphere.subscription.domain.Organization;

public class ProviderApplication extends Application {
    public static final String SERVER_URL = "http://192.168.43.176:8080";
    private static final String tag = ProviderApplication.class.getName();
    public static int CURRENT_ORG_ID = 1;

    private Server server;
    private Organization currentOrganization;

    @Override
    public void onCreate() {
        server = new Server(SERVER_URL);
        Log.i(tag, "Getting organization by id started...");
        server.getOrganizationById(CURRENT_ORG_ID).enqueue(new Callback<Organization>() {
            @Override
            public void onResponse(Response<Organization> response, Retrofit retrofit) {
                Log.i(tag, "Getting organization by id stopped.");
                currentOrganization = response.body();
                if (currentOrganization == null) {
                    Log.i(tag, "Organization hasn't registered yet");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(tag, String.format("getOrganizationById (id: %d) has thrown: ", CURRENT_ORG_ID), t);
            }
        });
        super.onCreate();
    }

    public Server getServer() {
        return server;
    }

    public Organization getCurrentOrganization() {
        return currentOrganization;
    }
}
