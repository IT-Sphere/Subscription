package ru.itsphere.subscription.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Represents abstraction for client's subscriptions or organization's subscriptions
 */
@DatabaseTable
public class Subscription {
    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField
    private String name;
    @DatabaseField
    private long clientId;
    @DatabaseField
    private long organizationId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(long organizationId) {
        this.organizationId = organizationId;
    }
}
