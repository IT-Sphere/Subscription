package ru.itsphere.subscription.server.service;

import java.util.List;

import ru.itsphere.subscription.domain.Client;

public interface ClientService {

    void create(Client client);

    Client getById(long clientId);

    List<Client> getAll();
}
