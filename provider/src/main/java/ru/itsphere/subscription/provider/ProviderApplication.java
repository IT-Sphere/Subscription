package ru.itsphere.subscription.provider;

import android.util.Log;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import ru.itsphere.subscription.common.service.ServerRelatedApplication;
import ru.itsphere.subscription.common.service.BlockedField;
import ru.itsphere.subscription.domain.Organization;

public class ProviderApplication extends ServerRelatedApplication {
    public static final String SERVER_URL = "http://192.168.43.176:8080";
    private static final String tag = ProviderApplication.class.getName();
    public static int CURRENT_ORG_ID = 1;

    private BlockedField<Organization> currentOrganization = new BlockedField<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(tag, "Getting organization by id started...");
        getServer().getOrganizationById(CURRENT_ORG_ID).enqueue(new Callback<Organization>() {
            @Override
            public void onResponse(Response<Organization> response, Retrofit retrofit) {
                Log.i(tag, "Getting organization by id stopped.");
                currentOrganization.set(response.body());
                if (getCurrentOrganization() == null) {
                    Log.i(tag, "Organization hasn't registered yet");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(tag, String.format("getOrganizationById (id: %d) has thrown: ", CURRENT_ORG_ID), t);
            }
        });
    }

    @Override
    protected String getServerUrl() {
        return SERVER_URL;
    }

    public Organization getCurrentOrganization() {
        return currentOrganization.get();
    }
}
