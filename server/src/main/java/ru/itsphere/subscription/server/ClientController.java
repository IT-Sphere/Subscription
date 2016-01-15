package ru.itsphere.subscription.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import ru.itsphere.subscription.domain.Client;
import ru.itsphere.subscription.server.service.ClientService;

@RestController
public class ClientController {

    @Autowired
    private ClientService service;

    @RequestMapping(value = "/client", method = RequestMethod.PUT)
    public void create(@RequestBody Client client) {
        service.create(client);
    }

    @RequestMapping(value = "/client", method = RequestMethod.GET)
    public List<Client> list() {
        return service.getAll();
    }

    @RequestMapping(value = "/client/{clientId}", method = RequestMethod.GET)
    public Client get(@PathVariable long clientId) {
        return service.getById(clientId);
    }
}
