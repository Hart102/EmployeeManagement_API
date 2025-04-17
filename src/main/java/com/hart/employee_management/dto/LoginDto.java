package com.hart.employee_management.dto;

import lombok.Data;

@Data
public class LoginDto {
    private Long id;
    private String name;
    private String email;
    private String access_token;
}
