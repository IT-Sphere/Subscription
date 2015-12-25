package ru.itsphere.subscription.common.service;

import java.util.ArrayList;
import java.util.List;

import ru.itsphere.subscription.domain.Client;

/**
 * Responsible for actions related to the registration
 */
public class RegistrationService {

    private static Client client = null;
    private static List<Client> registeredClients = new ArrayList<Client>();

    static {
        client = new Client();
        client.setEmail("sasha-ne@tut.by");
        client.setFirstName("Alex");
        client.setSecondName("Budnikov");
        client.setPhone("+375257166976");
    }

    public static void register(Client newClient) {
        client = newClient;
    }

    public static boolean isAlreadyRegistered() {
        return client != null;
    }

    public static Client getCurrentClient() {
        return client;
    }

    public static void registerNewClient(Client newClient) {
        registeredClients.add(newClient);
    }

    public static List<Client> getRegisteredClients() {
        return registeredClients;
    }
}
