package com.hart.employee_management.repository;

import com.hart.employee_management.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository <Organization, Long> {
    Optional<Organization> findByEmail(String email);

    boolean existsByEmail(String email);
}
