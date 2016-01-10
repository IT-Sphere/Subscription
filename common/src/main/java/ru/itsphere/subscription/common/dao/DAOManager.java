package ru.itsphere.subscription.common.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import ru.itsphere.subscription.domain.Client;
import ru.itsphere.subscription.domain.Organization;
import ru.itsphere.subscription.domain.Subscription;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DAOManager extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "subscription.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    private RuntimeExceptionDao<Client, Integer> clientDao = null;
    private RuntimeExceptionDao<Subscription, Integer> subscriptionDao = null;
    private RuntimeExceptionDao<Organization, Integer> organizationDao = null;

    public DAOManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION/*, R.raw.ormlite_config*/);
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DAOManager.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, Client.class);
            TableUtils.createTable(connectionSource, Subscription.class);
            TableUtils.createTable(connectionSource, Organization.class);
        } catch (SQLException e) {
            Log.e(DAOManager.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DAOManager.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, Client.class, true);
            TableUtils.dropTable(connectionSource, Subscription.class, true);
            TableUtils.dropTable(connectionSource, Organization.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DAOManager.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    public RuntimeExceptionDao<Client, Integer> getClientDao() {
        if (clientDao == null) {
            clientDao = getRuntimeExceptionDao(Client.class);
        }
        return clientDao;
    }

    public RuntimeExceptionDao<Subscription, Integer> getSubscriptionDao() {
        if (subscriptionDao == null) {
            subscriptionDao = getRuntimeExceptionDao(Subscription.class);
        }
        return subscriptionDao;
    }

    public RuntimeExceptionDao<Organization, Integer> getOrganizationDao() {
        if (organizationDao == null) {
            organizationDao = getRuntimeExceptionDao(Organization.class);
        }
        return organizationDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        clientDao = null;
        subscriptionDao = null;
        organizationDao = null;
    }
}
