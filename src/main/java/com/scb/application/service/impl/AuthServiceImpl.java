package com.scb.application.service.impl;

import com.scb.application.dto.request.LoginRequest;
import com.scb.application.dto.response.AuthResponse;
import com.scb.application.entity.Employee;
import com.scb.application.exception.ApiException;
import com.scb.application.exception.ErrorCode;
import com.scb.application.repository.EmployeeRepository;
import com.scb.application.utils.JwtService;
import com.scb.application.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmployeeRepository employeeRepository;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
            log.info("Login attempt for user with email: {}", loginRequest.getEmail());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            if (authentication.isAuthenticated()) {
                log.info("Authentication successful for user: {}", loginRequest.getEmail());

                Employee employee = employeeRepository.findByEmail(loginRequest.getEmail())
                        .orElseThrow(() -> new ApiException("User not found with email: " + loginRequest.getEmail(), ErrorCode.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND.value()));

                log.info("Generating JWT token for user: {}", employee.getEmail());
                String token = jwtService.generateToken(
                        User.builder()
                                .username(employee.getEmail())
                                .password(employee.getPassword())
                                .authorities(employee.getRole().name())
                                .build(),
                        employee.getRole().name()
                );
                log.info("JWT token generated successfully for user: {}", employee.getEmail());

                AuthResponse response = AuthResponse.builder()
                        .token(token)
                        .email(employee.getEmail())
                        .role(employee.getRole().name())
                        .build();

                log.info("Login successful for user: {} with role: {}", employee.getEmail(), employee.getRole().name());
                return response;
            } else {
                log.info("Authentication failed for user: {}", loginRequest.getEmail());
                throw new ApiException("Authentication failed", ErrorCode.AUTHENTICATION_FAILED, HttpStatus.UNAUTHORIZED.value());
            }
    }
}
