package com.hart.employee_management.security.user;

import com.hart.employee_management.exception.CustomException;
import com.hart.employee_management.repository.OrganizationRepository;
import com.hart.employee_management.services.organization.IOrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    private final OrganizationRepository organizationRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            var org = organizationRepository.findByEmail(email)
                    .orElseThrow(() -> new CustomException("Organization not found"));

            return User.builder()
                    .username(org.getEmail())
                    .password(org.getPassword())
                    .build();
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }
}
