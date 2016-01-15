package ru.itsphere.subscription.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import ru.itsphere.subscription.domain.Organization;
import ru.itsphere.subscription.server.service.OrganizationService;

@RestController
public class OrganizationController {

    @Autowired
    private OrganizationService service;

    @RequestMapping(value = "/organization", method = RequestMethod.PUT)
    public void create(@RequestBody Organization organization) {
        service.create(organization);
    }

    @RequestMapping(value = "/organization", method = RequestMethod.GET)
    public List<Organization> list() {
        return service.getAll();
    }

    @RequestMapping(value = "/organization/{organizationId}", method = RequestMethod.GET)
    public Organization get(@PathVariable long organizationId) {
        return service.getById(organizationId);
    }
}
