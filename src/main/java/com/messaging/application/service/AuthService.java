package com.messaging.application.service;

import com.messaging.application.models.LoginDto;

public interface AuthService {
    String login(LoginDto loginDto);
}