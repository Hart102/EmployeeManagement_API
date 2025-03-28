package com.hart.employee_management.repository;

import com.hart.employee_management.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Organization, Long> {
    Boolean existsByEmail(String email);
    Organization findOrganizationByEmail(String email);
}
