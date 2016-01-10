package ru.itsphere.subscription.common.service;

import ru.itsphere.subscription.domain.Client;

/**
 * Responsible for actions related to the clients
 */
public interface ClientService {
    /**
     * Returns client by id
     *
     * @param id    entity identifier
     * @param eager associated entities will be initialised if true
     * @return entity
     */
    Client getClientById(long id, boolean eager);

    /**
     * Create new entity or update already existed
     *
     * @param client entity
     */
    void createOrUpdate(Client client);
}
