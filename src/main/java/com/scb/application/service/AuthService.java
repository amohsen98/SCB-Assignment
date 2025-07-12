package com.scb.application.service;

import com.scb.application.dto.request.LoginRequest;
import com.scb.application.dto.response.AuthResponse;


public interface AuthService {

    AuthResponse login(LoginRequest loginRequest);
}