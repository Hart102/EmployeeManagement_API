package com.hart.employee_management.services.employee;

import com.hart.employee_management.model.Employee;
import com.hart.employee_management.request.CreateEmployeeRequest;
import com.hart.employee_management.request.UpdateEmployeeRequest;

import java.util.List;

public interface IEmployeeService {
    List<Employee> getAllEmployees();
    Employee findEmployeeById(Long id);
    Employee getEmployeeByEmail (String email);
    Employee createEmployee(CreateEmployeeRequest request, Long JobId);
    Employee updateEmployee(UpdateEmployeeRequest request, Long employeeId, Long newJobId);
    void deleteEmployee(Long employee_id);
}
