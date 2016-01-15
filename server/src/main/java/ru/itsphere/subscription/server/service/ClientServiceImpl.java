package ru.itsphere.subscription.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import ru.itsphere.subscription.domain.Client;
import ru.itsphere.subscription.server.repository.ClientRepository;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepository repository;

    @Override
    public void create(Client client) {
        repository.save(client);
    }

    @Override
    public Client getById(long clientId) {
        return repository.findOne(clientId);
    }

    @Override
    public List<Client> getAll() {
        return repository.findAll();
    }
}
