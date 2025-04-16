package com.hart.employee_management.request;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class OrganizationRequest {
    private String name;
    private String email;
    private String password;
}
