package com.hart.employee_management.services.employee;

import com.hart.employee_management.exception.CustomException;
import com.hart.employee_management.model.Employee;
import com.hart.employee_management.model.Job;
import com.hart.employee_management.model.Organization;
import com.hart.employee_management.repository.EmployeeRepository;
import com.hart.employee_management.request.CreateEmployeeRequest;
import com.hart.employee_management.request.UpdateEmployeeRequest;
import com.hart.employee_management.services.job.JobService;
import com.hart.employee_management.services.organization.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService implements IEmployeeService {
    private final EmployeeRepository employeeRepository;
    private final JobService jobService;
    private final OrganizationService organizationService;

    @Override
    public List<Employee> getAllEmployees() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Organization organization = organizationService.findOrganizationByEmail(email)
                .orElseThrow(() -> new CustomException("Organization not found!"));
        return employeeRepository.findByOrganizationId(organization.getId());
    }

    @Override
    public Employee findEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new CustomException("Employee not found!"));
    }

    @Override
    public Employee getEmployeeByEmail(String email) {
        Employee employee = employeeRepository.findByEmail(email);

        if (employee == null) {
            throw new CustomException("Employee not found!");
        }
        return employee;
    }

    /*
    * Get the authenticated user
    * Check if email already exists
    * Check if Job already exists
    * Create and return Employee
    * */
    @Override
    public Employee createEmployee(CreateEmployeeRequest request, Long jobId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authentication.isAuthenticated()) {
            throw new CustomException("Authorization failed, please login and try again!");
        }
        Organization organization = organizationService.findOrganizationByEmail(authentication.getName())
                .orElseThrow(() -> new CustomException("Organization not found!"));


        var employee = employeeRepository.findByEmail(request.getEmail());
        if (employee != null) {
            throw new CustomException("Employee with same email already exist!");
        }

        var job = jobService.findJobById(jobId);
        if (job.getTitle() == null) {
            throw new CustomException("Job not found!");
        }
        Employee newEmployee = new Employee();
        newEmployee.setJobs(List.of(job)); // Set Job
        newEmployee.setOrganization(organization); // Set Organization
        newEmployee.setPhone(request.getPhone());
        newEmployee.setEmail(request.getEmail());
        newEmployee.setCurrency(request.getCurrency());
        newEmployee.setLastName(request.getLastName());
        newEmployee.setFirstName(request.getFirstName());
        newEmployee.setDateOfJoining(LocalDateTime.now());
        newEmployee.setEmployeeStatus(request.getEmployeeStatus());
        newEmployee.setSalary(new BigDecimal(String.valueOf(request.getSalary())));
        return employeeRepository.save(newEmployee);
    }

    @Override
    public Employee updateEmployee(
            UpdateEmployeeRequest request, Long employee_id, Long newJobId) {
        var employee = employeeRepository.findById(employee_id) // Find the employee
                .orElseThrow(() -> new CustomException("Employee not found!"));

        var job = jobService.findJobById(newJobId); // Find the new Job
        if (job.getTitle() == null) {
            throw new CustomException("Job not found!");
        }
        List<Job> updatedJobs = new ArrayList<>(employee.getJobs()); // Create a modifiable list
        updatedJobs.removeIf(j -> j.getId().equals(newJobId)); // Remove the old job

        // Add only if the job doesn't already exist
        if (updatedJobs.stream().noneMatch(j -> j.getTitle().equals(job.getTitle()))) {
            updatedJobs.add(job);
        }
        employee.setJobs(updatedJobs);
        employee.setPhone(request.getPhone());
        employee.setEmail(request.getEmail());
        employee.setLastName(request.getLastName());
        employee.setCurrency(request.getCurrency());
        employee.setFirstName(request.getFirstName());
        employee.setEmployeeStatus(request.getEmployeeStatus());
        employee.setSalary(new BigDecimal(String.valueOf(request.getSalary())));
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Long employee_id) {
        employeeRepository.findById(employee_id).orElseThrow(() -> new CustomException("Employee not found!"));
        employeeRepository.deleteById(employee_id);
    }

}
