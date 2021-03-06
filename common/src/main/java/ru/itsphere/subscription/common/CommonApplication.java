package ru.itsphere.subscription.common;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.net.InetAddress;

import ru.itsphere.subscription.common.dao.DAOManager;
import ru.itsphere.subscription.common.server.Server;
import ru.itsphere.subscription.common.service.ApplicationService;
import ru.itsphere.subscription.common.service.ApplicationServiceImpl;
import ru.itsphere.subscription.common.service.ClientService;
import ru.itsphere.subscription.common.service.ClientServiceImpl;
import ru.itsphere.subscription.common.service.OrganizationService;
import ru.itsphere.subscription.common.service.OrganizationServiceImpl;

/**
 * Abstract class for applications related to server
 */
public abstract class CommonApplication extends Application {
    private static final String tag = CommonApplication.class.getName();

    private Server server;
    private ClientService clientService;
    private OrganizationService organizationService;
    private ApplicationService applicationService;
    private DAOManager DAOManager;

    protected abstract String getServerUrl();

    protected abstract String getServerHost();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(tag, String.format("Initializing server with url %s", getServerUrl()));
        server = new Server(getServerUrl());
        DAOManager = OpenHelperManager.getHelper(this, DAOManager.class);
        clientService = new ClientServiceImpl(DAOManager);
        organizationService = new OrganizationServiceImpl(DAOManager);
        applicationService = new ApplicationServiceImpl(DAOManager);
    }

    public Server getServer() {
        return server;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (DAOManager != null) {
            OpenHelperManager.releaseHelper();
            DAOManager = null;
            clientService = null;
            organizationService = null;
        }
    }

    public ClientService getClientService() {
        return clientService;
    }

    public OrganizationService getOrganizationService() {
        return organizationService;
    }

    public ApplicationService getApplicationService() {
        return applicationService;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public boolean isServerAvailable() {
        try {
            if (InetAddress.getByName(getServerHost()).equals("")) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
