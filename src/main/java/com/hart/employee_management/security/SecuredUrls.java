package com.hart.employee_management.security;

public class SecuredUrls {
    public static final String[] SECURED_URLS = {
            "/api/auth/reset-password/**", "/api/employees/**",
            "/api/jobs/*", "/api/jobs/create", "/api/jobs/update/*",
            "/api/jobs/delete/*", "/api/organizations/**", "/api/addresses/**"
    };

    // Optional getter method if direct access is not preferred
    public static String[] getPublicUrls() {
        return SECURED_URLS;
    }
}
