package ru.itsphere.subscription.common.service;

import android.app.Application;
import android.util.Log;

/**
 * Abstract class for applications related to server
 */
public abstract class ServerRelatedApplication extends Application {
    private static final String tag = ServerRelatedApplication.class.getName();

    private Server server;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(tag, String.format("Initializing server with url %s", getServerUrl()));
        server = new Server(getServerUrl());
    }

    protected abstract String getServerUrl();

    public Server getServer() {
        return server;
    }
}
