package ru.itsphere.subscription.common.service;

import android.util.Log;

import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import ru.itsphere.subscription.common.dao.DAOManager;
import ru.itsphere.subscription.domain.Client;
import ru.itsphere.subscription.domain.Subscription;

public class ApplicationServiceImpl implements ApplicationService {

    private static final String tag = ApplicationServiceImpl.class.getName();
    private final DAOManager daoManager;

    public ApplicationServiceImpl(DAOManager daoManager) {
        this.daoManager = daoManager;
    }

    @Override
    public void cleanDataBase() {
        Log.i(tag, "Executing DB cleaning");
        try {
            recreateTable(Subscription.class);
            recreateTable(Client.class);
            Log.i(tag, "DB is cleaned");
        } catch (SQLException e) {
            throw new RuntimeException("DB cleaning has thrown exception", e);
        }
    }

    private void recreateTable(Class<? extends Object> aClass) throws SQLException {
        TableUtils.dropTable(daoManager.getConnectionSource(), aClass, true);
        Log.i(tag, String.format("Table for %s dropped.", aClass.getName()));
        TableUtils.createTable(daoManager.getConnectionSource(), aClass);
        Log.i(tag, String.format("Table for %s created.", aClass.getName()));
    }
}
