package ru.itsphere.subscription.common.service;

/**
 * Responsible for actions related to the subscriptions
 */
public class SubscriptionService {

    private static String[] subscriptions = {"Swimming pool", "Sauna", "Gym", "Java classes"};

    public static String[] getAll() {
        return subscriptions;
    }
}
