package com.hart.employee_management.services.auth;

import com.hart.employee_management.dto.LoginDto;
import com.hart.employee_management.exception.CustomException;
import com.hart.employee_management.model.Organization;
import com.hart.employee_management.repository.OrganizationRepository;
import com.hart.employee_management.request.LoginRequest;
import com.hart.employee_management.request.OrganizationRequest;
import com.hart.employee_management.security.jwt.JwtUtil;
import com.hart.employee_management.services.MailService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private static final SecureRandom RANDOM = new SecureRandom();
    private final MailService mailService;

    private final ModelMapper modelMapper;


    @Override
    public Organization register(OrganizationRequest org) {
        if (organizationRepository.findByEmail(org.getEmail()).isPresent()) {
            throw new CustomException("Email already in use, please try another mail");
        }
        Organization organization = new Organization();
        organization.setName(org.getName());
        organization.setEmail(org.getEmail());
        organization.setPassword(passwordEncoder.encode(org.getPassword()));
        return organizationRepository.save(organization);
    }


    /*
    * LOGIN:
    * Find user with their email address (Return user/error message)
    * Compare password (Throw error/return user)
    * If login is successful then log user in
    * Generate access token
    * Return user and access token
    * */
    @Override
    public LoginDto login(LoginRequest request) {
        var org = organizationRepository
                .findByEmail(request.getEmail()).orElseThrow(() -> new CustomException("User not found!"));

        if (!passwordEncoder.matches(request.getPassword(), org.getPassword())) {
            throw new CustomException("Incorrect email or password");
        }
        authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(org.getEmail(), request.getPassword()));

        String token = jwtUtil.generateToke(org.getEmail(), org.getId());

        LoginDto loginResponse = modelMapper.map(org, LoginDto.class);
        loginResponse.setAccess_token(token);
        return loginResponse;
    }


    /*
     * RESET PASSWORD:
     * Verify Organization with their Id
     * Check if old password match the existing password
     * Hash and save new password
     * */
    @Transactional
    @Override
    public void resetPassword(String oldPassword, String newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        var organization = organizationRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("Organization not found!"));

        if (!passwordEncoder.matches(oldPassword, organization.getPassword())) {
            throw new CustomException("Incorrect password");
        }
        organization.setPassword(passwordEncoder.encode(newPassword));
        organizationRepository.save(organization);
    }


    /*
     * Verify Organization exist using their mail
     * Generate new password using their mail
     * send the new mail to them through mail
     * Has and save a copy of the password in the database
     * */
    @Override
    public void forgotPassword(String email) {
        var organization = organizationRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("Organization not found!"));

        Random random = new Random();
        String newPassword = String.format("%06d", random.nextInt(1000000));

        String content = """
                <b>New Password From Employee Manager</p>
                <p>Dear %s, below is your new password:</p>
                <h2>New password: %s</h4>
                """;
        String subject = "New Password From Employee Manager";
        String formattedContent = String.format(content, organization.getName(), newPassword);

        try {
            mailService.sendMail(email, subject, formattedContent);
            organization.setPassword(passwordEncoder.encode(newPassword));
            organizationRepository.save(organization);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new CustomException(e.toString());
        }
    }
}
