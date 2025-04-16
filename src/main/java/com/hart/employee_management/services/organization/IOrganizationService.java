package com.hart.employee_management.services.organization;

import com.hart.employee_management.model.Organization;
import com.hart.employee_management.request.OrganizationRequest;

import java.util.Optional;

public interface IOrganizationService {
    Optional<Organization> getOrganization();
    Organization updateOrganization(OrganizationRequest request);
    Optional<Organization> findOrganizationByEmail(String email);
    void deleteAccount();
}
