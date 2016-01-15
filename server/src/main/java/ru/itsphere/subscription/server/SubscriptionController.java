package ru.itsphere.subscription.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import ru.itsphere.subscription.domain.Subscription;
import ru.itsphere.subscription.server.service.SubscriptionService;

@RestController
public class SubscriptionController {

    @Autowired
    private SubscriptionService service;

    @RequestMapping(value = "/subscription", method = RequestMethod.PUT)
    public void create(@RequestBody Subscription subscription) {
        service.create(subscription);
    }

    @RequestMapping(value = "/subscription", method = RequestMethod.GET)
    public List<Subscription> list(
            @RequestParam(name = "clientId", required = false) Long clientId,
            @RequestParam(name = "organizationId", required = false) Long organizationId) {
        if (clientId != null && organizationId == null) {
            return service.getByClientId(clientId);
        }

        if (clientId == null && organizationId != null) {
            return service.getByOrganizationId(organizationId);
        }

        if (clientId != null && organizationId != null) {
            return service.getByClientIdAndOrganizationId(clientId, organizationId);
        }

        throw new RuntimeException("There are neither clientId nor organizationId");
    }
}
