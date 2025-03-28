package com.hart.employee_management.services.employee;

import com.hart.employee_management.model.Employee;
import com.hart.employee_management.model.EmployeeAddress;
import com.hart.employee_management.request.CreateEmployeeRequest;
import com.hart.employee_management.request.UpdateEmployeeRequest;

import java.util.List;

public interface IEmployeeService {
    List<Employee> getAllEmployees();
    Employee findEmployeeById(Long id);
    Employee getEmployeeByEmail (String email);
    Employee createEmployee(CreateEmployeeRequest request, Long JobId);
    Employee updateEmployee(UpdateEmployeeRequest employee, Long employee_id, String newJobTitle, String oldJobTitle);
    void deleteEmployee(Long employee_id);

    EmployeeAddress createAddress(EmployeeAddress employeeAddress);
    EmployeeAddress updateAddress(EmployeeAddress employeeAddress);
    void deleteAddress (Long addressId);
}
