package ru.itsphere.subscription.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.itsphere.subscription.domain.Client;

/**
 * CRUD operations for client
 */
public interface ClientRepository extends JpaRepository<Client, Long> {
}
