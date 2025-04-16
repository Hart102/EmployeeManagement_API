package com.hart.employee_management.services.organization;

import com.hart.employee_management.exception.CustomException;
import com.hart.employee_management.model.Organization;
import com.hart.employee_management.repository.OrganizationRepository;
import com.hart.employee_management.request.OrganizationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class OrganizationService implements IOrganizationService {

    private final OrganizationRepository organizationRepository;

    /*
    * Get Authenticated Organization
    * */
    @Override
    public Optional<Organization> getOrganization() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return Optional.ofNullable(organizationRepository.findByEmail(email))
                .orElseThrow(() -> new CustomException("Organization not found !"));
    }

    /*
    * Get Organization bg email
    * */
    @Override
    public Optional<Organization> findOrganizationByEmail(String email) {
        return Optional.ofNullable(organizationRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("Organization not found!")));
    }


    /*
    * Get the organization to update
    * Check if the newly provided email is in use by another user
    * If the new email is not in use, updated the record
    * */
    @Override
    public Organization updateOrganization(OrganizationRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Organization organization = organizationRepository
                .findByEmail(email).orElseThrow(() -> new CustomException("Organization not found!"));

        if (organizationRepository.existsByEmail(request.getEmail()) &&
                !organization.getEmail().equals(request.getEmail()))
        {
            throw new CustomException("Email already exists");
        }
        organization.setName(request.getName());
        organization.setEmail(request.getEmail());
        return organizationRepository.save(organization);
    }


    @Override
    public void deleteAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String mail = authentication.getName();

        Organization organization = organizationRepository
                .findByEmail(mail).orElseThrow(() -> new CustomException("Organization not found!"));
        organizationRepository.deleteById(organization.getId());
    }
}
