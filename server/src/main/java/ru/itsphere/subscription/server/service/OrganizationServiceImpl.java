package ru.itsphere.subscription.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import ru.itsphere.subscription.domain.Organization;
import ru.itsphere.subscription.server.repository.OrganizationRepository;

@Service
public class OrganizationServiceImpl implements OrganizationService {
    @Autowired
    private OrganizationRepository repository;

    @Override
    public void create(Organization organization) {
        repository.save(organization);
    }

    @Override
    public Organization getById(long organizationId) {
        return repository.findOne(organizationId);
    }

    @Override
    public List<Organization> getAll() {
        return repository.findAll();
    }
}
