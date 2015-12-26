package ru.itsphere.subscription.common.service;

import java.util.ArrayList;
import java.util.List;

import ru.itsphere.subscription.domain.Client;

/**
 * Responsible for actions related to the registration
 */
public class RegistrationService {

    private static List<Client> registeredClients = new ArrayList<Client>();

    public static void registerNewClient(Client newClient) {
        registeredClients.add(newClient);
    }
}
