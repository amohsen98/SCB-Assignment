package com.scb.application.security;

import com.scb.application.entity.Employee;
import com.scb.application.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    public CustomUserDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Employee employee = employeeRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
            return new User(
                    employee.getEmail(),
                    employee.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority(employee.getRole().name()))
            );
        } catch (UsernameNotFoundException e) {
            log.warn("CustomUserDetailsService: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            log.warn("CustomUserDetailsService: Exception while loading user details: " + e.getMessage());
            throw e;
        }
    }
}
