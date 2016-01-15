package ru.itsphere.subscription.server.service;

import java.util.List;

import ru.itsphere.subscription.domain.Organization;

public interface OrganizationService {

    void create(Organization organization);

    Organization getById(long organizationId);

    List<Organization> getAll();
}
