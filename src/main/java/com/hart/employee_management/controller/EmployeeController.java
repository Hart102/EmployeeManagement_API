package com.hart.employee_management.controller;

import com.hart.employee_management.request.CreateEmployeeRequest;
import com.hart.employee_management.request.UpdateEmployeeRequest;
import com.hart.employee_management.response.ApiResponse;
import com.hart.employee_management.services.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;


    @GetMapping("/")
    public ResponseEntity<ApiResponse> getAllEmployees () {
        try {
            var employees = employeeService.getAllEmployees();
            return ResponseEntity.ok(new ApiResponse(false, "Employees retrieved", employees));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse(true, "Something went wrong, please try again", null));
        }
    }

    @GetMapping("/{employee_id}")
    public ResponseEntity<ApiResponse> findEmployeeById (@PathVariable Long employee_id) {
        try {
            var employees = employeeService.findEmployeeById(employee_id);
            return ResponseEntity.ok(new ApiResponse(false, "Employee retrieved", employees));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse(true, e.getMessage(), null));
        }
    }

    @GetMapping("/by-email/{email}")
    public ResponseEntity<ApiResponse> findEmployeeByEmail (@PathVariable String email) {
        try {
            var employee = employeeService.getEmployeeByEmail(email);
            return ResponseEntity.ok(new ApiResponse(false, "Employee retrieved", employee));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse(true, e.getMessage(), null));
        }
    }


    @PostMapping("/create/jobId/{jobId}")
    public ResponseEntity<ApiResponse> createEmployee (
            @RequestBody CreateEmployeeRequest request,
            @PathVariable Long jobId) {
        try {
            var employee = employeeService.createEmployee(request, jobId);
            return ResponseEntity.ok(new ApiResponse(false, "Employee created successfully", employee));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse(true, e.getMessage(), null));
        }
    }

    @PutMapping("/update/employeeId/{employeeId}/newJobTitle/{newJobTitle}/oldJobTitle/{oldJobTitle}")
    public ResponseEntity<ApiResponse> updateEmployee (
            @RequestBody UpdateEmployeeRequest request,
            @PathVariable Long employeeId,
            @PathVariable String newJobTitle,
            @PathVariable String oldJobTitle
    ) {
        try {
            var employee = employeeService.updateEmployee(
                    request, employeeId, newJobTitle, oldJobTitle
            );
            return ResponseEntity.ok(new ApiResponse(false, "Employee record updated successfully", employee));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse(true, e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> updateEmployee (@PathVariable Long id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.ok(new ApiResponse(false, "Employee record delete successfully", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse(true, e.getMessage(), null));
        }
    }
}
