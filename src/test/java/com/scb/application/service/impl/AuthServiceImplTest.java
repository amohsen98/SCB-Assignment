package com.scb.application.service.impl;

import com.scb.application.dto.request.LoginRequest;
import com.scb.application.dto.response.AuthResponse;
import com.scb.application.entity.Employee;
import com.scb.application.enums.Role;
import com.scb.application.exception.ApiException;
import com.scb.application.repository.EmployeeRepository;
import com.scb.application.utils.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthServiceImpl authService;

    private LoginRequest loginRequest;
    private Employee employee;
    private String jwtToken;

    @BeforeEach
    void setUp() {
        // Setup common test data
        loginRequest = new LoginRequest();
        loginRequest.setEmail("john.doe@example.com");
        loginRequest.setPassword("password");

        employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setEmail("john.doe@example.com");
        employee.setPassword("encodedPassword");
        employee.setRole(Role.USER.name());

        jwtToken = "jwt.token.string";
    }

    @Test
    void login_Success() {
        
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(employeeRepository.findByEmail(anyString())).thenReturn(Optional.of(employee));
        when(jwtService.generateToken(any(User.class), anyString())).thenReturn(jwtToken);

   
        AuthResponse result = authService.login(loginRequest);


        assertNotNull(result);
        assertEquals(jwtToken, result.getToken());
        assertEquals(employee.getEmail(), result.getEmail());
        assertEquals(employee.getRole(), result.getRole());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(employeeRepository).findByEmail(loginRequest.getEmail());
        verify(jwtService).generateToken(any(User.class), eq(employee.getRole()));
    }

    @Test
    void login_AuthenticationFailed() {
        
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);


        ApiException exception = assertThrows(ApiException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("Authentication failed", exception.getMessage());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(employeeRepository, never()).findByEmail(anyString());
        verify(jwtService, never()).generateToken(any(), anyString());
    }

    @Test
    void login_BadCredentials() {
        
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));


        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("Bad credentials", exception.getMessage());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(employeeRepository, never()).findByEmail(anyString());
        verify(jwtService, never()).generateToken(any(), anyString());
    }

    @Test
    void login_UserNotFound() {
        
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(employeeRepository.findByEmail(anyString())).thenReturn(Optional.empty());


        ApiException exception = assertThrows(ApiException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("User not found with email: " + loginRequest.getEmail(), exception.getMessage());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(employeeRepository).findByEmail(loginRequest.getEmail());
        verify(jwtService, never()).generateToken(any(), anyString());
    }
}
