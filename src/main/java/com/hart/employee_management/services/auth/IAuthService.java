package com.hart.employee_management.services.auth;

import com.hart.employee_management.dto.LoginDto;
import com.hart.employee_management.model.Organization;
import com.hart.employee_management.request.LoginRequest;
import com.hart.employee_management.request.OrganizationRequest;

public interface IAuthService {
    Organization register(OrganizationRequest org);
    LoginDto login(LoginRequest request);
    void resetPassword(String oldPassword, String newPassword);
    void forgotPassword(String email);
}
