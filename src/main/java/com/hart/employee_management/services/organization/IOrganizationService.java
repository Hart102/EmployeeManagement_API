package com.hart.employee_management.services.organization;

import com.hart.employee_management.model.Organization;

public interface IOrganizationService {
    Organization findOrganizationByEmail(String email);
    Organization createOrganization(Organization org);
    Organization updateOrganization(Organization request, Long org_id);
    String resetPassword(String oldPassword, String newPassword, Long org_id);
    String forgotPassword(String email);
}
