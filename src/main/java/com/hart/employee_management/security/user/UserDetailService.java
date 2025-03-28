package com.hart.employee_management.security.user;

import com.hart.employee_management.exception.CustomException;
import com.hart.employee_management.model.Organization;
import com.hart.employee_management.services.organization.IOrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    private final IOrganizationService organizationService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Organization user = organizationService.findOrganizationByEmail(email);

            // Map fetched organization to UserDetails
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .build();

        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }
}
