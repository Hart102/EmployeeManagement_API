package com.hart.employee_management.controller;

import com.hart.employee_management.exception.CustomException;
import com.hart.employee_management.model.Organization;
import com.hart.employee_management.request.OrganizationRequest;
import com.hart.employee_management.response.ApiResponse;
import com.hart.employee_management.services.organization.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/organizations")
public class OrganizationController {
    private final OrganizationService organizationService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAuthenticatedOrganization() {
        try {
            Optional<Organization> organization = organizationService.getOrganization();
            return ResponseEntity.ok(new ApiResponse(false, "Record retrieved", organization));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(true, e.getMessage(), null));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateOrganization (@RequestBody OrganizationRequest request) {
        try {
            Organization organization = organizationService.updateOrganization(request);
            return ResponseEntity.ok(new ApiResponse(false, "Record updated", organization));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(true, e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteAccount () {
        try {
            organizationService.deleteAccount();
            return ResponseEntity.ok(new ApiResponse(false, "Account deleted", null));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(true, e.getMessage(), null));
        }
    }
}
