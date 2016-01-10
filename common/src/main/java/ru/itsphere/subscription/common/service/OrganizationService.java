package ru.itsphere.subscription.common.service;

import ru.itsphere.subscription.domain.Organization;

/**
 * Responsible for actions related to the organization
 */
public interface OrganizationService {
    /**
     * Returns organization by id
     *
     * @param id    entity identifier
     * @param eager associated entities will be initialised if true
     * @return entity
     */
    Organization getOrganizationById(long id, boolean eager);

    /**
     * Create new entity or update already existed
     *
     * @param organization entity
     */
    void createOrUpdate(Organization organization);
}
