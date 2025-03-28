package com.hart.employee_management.services.organization;

import com.hart.employee_management.exception.CustomException;
import com.hart.employee_management.model.Organization;
import com.hart.employee_management.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class OrganizationService implements IOrganizationService {

    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public Organization findOrganizationByEmail(String email) {
        return organizationRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("Organization not found!"));
    }

    @Override
    public Organization createOrganization(Organization org) {
        organizationRepository.findByEmail(org.getEmail())
                .orElseThrow(() -> new CustomException("Organization not found!"));

        return organizationRepository.save(org);
    }

    @Override
    public Organization updateOrganization(Organization request, Long org_id) {
        var response = organizationRepository.findById(org_id)
                .orElseThrow(() -> new CustomException("Organization not found!"));

        Organization org = new Organization();
        org.setName(request.getName());
        org.setEmail(request.getEmail());
        return organizationRepository.save(org);
    }

    /*
    * Reset password:
    * Verify Organization with their Id
    * Check if old password match the existing password
    * Hash and save new password
    * */
    @Override
    public String resetPassword(String oldPassword, String newPassword, Long org_id) {
        var organization = organizationRepository.findById(org_id)
                .orElseThrow(() -> new CustomException("Organization not found!"));

        if (!passwordEncoder.matches(oldPassword, organization.getPassword())) {
            throw new CustomException("Incorrect password");
        }
        organization.setPassword(passwordEncoder.encode(newPassword));
        organizationRepository.save(organization);
        return "Password updated successfully";
    }

    /*
    * Verify Organization exist using their mail
    * Generate new password using their mail
    * send the new mail to them through mail
    * Has and save a copy of the password in the database
    * */
    @Override
    public String forgotPassword(String email) {
        var organization = organizationRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("Organization not found!"));

        int passwordLength = 6;
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < passwordLength; i++) {
            password.append(organization.getEmail().charAt(RANDOM.nextInt(organization.getEmail().length())));
        }
        String newPassword = password.toString();

        // Send new Password to user through mail
        return "Password reset successful";
    }


}
