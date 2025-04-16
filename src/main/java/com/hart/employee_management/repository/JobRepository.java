package com.hart.employee_management.repository;

import com.hart.employee_management.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Boolean existsByTitle(String title);

    Job findByTitle(String title);
}
