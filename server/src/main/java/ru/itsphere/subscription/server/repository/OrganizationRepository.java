package ru.itsphere.subscription.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.itsphere.subscription.domain.Organization;

/**
 * CRUD operations for organization
 */
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
