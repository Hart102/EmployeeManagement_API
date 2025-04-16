package com.hart.employee_management.controller;

import com.hart.employee_management.exception.CustomException;
import com.hart.employee_management.model.Organization;
import com.hart.employee_management.request.LoginRequest;
import com.hart.employee_management.request.OrganizationRequest;
import com.hart.employee_management.response.ApiResponse;
import com.hart.employee_management.security.jwt.JwtUtil;
import com.hart.employee_management.services.auth.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowCredentials = "true")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @Value("${auth.token.jwtSecret}")
    private String JWT_SECRET_KEY;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register (@RequestBody OrganizationRequest request) {
        try {
            Organization org = authService.register(request);
            return ResponseEntity.ok(new ApiResponse(false, "Account created successfully", org));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ApiResponse(true, e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login (@RequestBody LoginRequest request) {
        try {
            var token = authService.login(request);
            return ResponseEntity.ok(new ApiResponse(false, "Login successfully", token));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(true, e.getMessage(), null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout (HttpServletResponse response) {
        try {
            SecurityContextHolder.clearContext();
            response.setHeader("Set-Cookie", "Authorization=; Max-Age=0; Path=/; HttpOnly; Secure");
            return ResponseEntity.ok(new ApiResponse(false, "Logout successful", null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                    new ApiResponse(true,
                    "Something went wrong, please try again", null));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(
            @RequestParam String oldPassword, @RequestParam String newPassword) {
        try {
            authService.resetPassword(oldPassword, newPassword);
            return ResponseEntity.ok(new ApiResponse(false, "Password updated", null));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(true, e.getMessage(), null));
        }
    }


    @PutMapping("/forgot-password/{email}")
    public ResponseEntity<ApiResponse> forgotPassword (@PathVariable String email) {

        try {
            authService.forgotPassword(email);
            return ResponseEntity.ok(new ApiResponse(false, "New password sent to: " + email, null));
        } catch (CustomException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                    new ApiResponse(true, e.getMessage(), null));
        }
    }
}
