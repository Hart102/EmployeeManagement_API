package com.hart.employee_management.services.employee;

import com.hart.employee_management.exception.CustomException;
import com.hart.employee_management.model.Employee;
import com.hart.employee_management.model.EmployeeAddress;
import com.hart.employee_management.model.Job;
import com.hart.employee_management.repository.EmployeeRepository;
import com.hart.employee_management.request.CreateEmployeeRequest;
import com.hart.employee_management.request.UpdateEmployeeRequest;
import com.hart.employee_management.services.job.JobService;
import lombok.RequiredArgsConstructor;
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

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
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

    @Override
    public Employee createEmployee(CreateEmployeeRequest request, Long jobId) {
        Employee newEmployee = new Employee();

        var employee = employeeRepository.findByEmail(request.getEmail());
        if (employee != null) {
            throw new CustomException("Employee with same email already exist!");
        }
        var job = jobService.findJobById(jobId);
        if (job.getTitle() == null) {
            throw new CustomException("Job not found!");
        }

        newEmployee.setJobs(List.of(job));
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
            UpdateEmployeeRequest request, Long employee_id, String newJobTitle, String oldJobTitle) {

        var employee = employeeRepository.findById(employee_id) // Find the employee
                .orElseThrow(() -> new CustomException("Employee not found!"));


        var job = jobService.findJobByTitle(newJobTitle); // Find the new Job
        if (job.getTitle() == null) {
            throw new CustomException("Job not found!");
        }
        List<Job> updatedJobs = new ArrayList<>(employee.getJobs()); // Create a modifiable list
        updatedJobs.removeIf(j -> j.getTitle().equals(oldJobTitle)); // Remove the old job

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
        employeeRepository.findById(employee_id)
                .orElseThrow(() -> new CustomException("Employee not found!"));
        employeeRepository.deleteById(employee_id);
    }

    @Override
    public EmployeeAddress createAddress(EmployeeAddress employeeAddress) {
        return null;
    }

    @Override
    public EmployeeAddress updateAddress(EmployeeAddress employeeAddress) {
        return null;
    }

    @Override
    public void deleteAddress(Long addressId) {

    }
}
