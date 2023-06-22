package com.messaging.application.controller;

import com.messaging.application.models.LoginDto;
import com.messaging.application.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class AuthController {

    private AuthService authService;

    // Build Login REST API
    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody LoginDto loginDto){
        String token = authService.login(loginDto);

        return ResponseEntity.ok("Token: " + token);
    }
}
