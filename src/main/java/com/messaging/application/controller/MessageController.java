package com.messaging.application.controller;


import com.messaging.application.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class MessageController {
    @Autowired
    MessagingService messagingService;

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String USER_ALREADY_EXISTS = "User already exists.";
    @Secured("ROLE_ADMIN")
    @PostMapping("/getMessages")
    public ResponseEntity getAllMessages(@RequestBody Map<String, String> requestBody) {
        String username = requestBody.get(USERNAME);
        String password = requestBody.get(PASSWORD);
        boolean isAuthenticated = messagingService.checkIfUserIsRegistered(username, password);
        if(isAuthenticated){
            return ResponseEntity.ok(messagingService.getAllMessages());
        } else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong username and/or password.");
        }

    }

    @PostMapping("/postMessage")
    public ResponseEntity<String> postMessage(@RequestBody Map<String, String> requestBody) {
        String username = requestBody.get(USERNAME);
        String password = requestBody.get(PASSWORD);
        boolean isAuthenticated = messagingService.checkIfUserIsRegistered(username, password);
        if(isAuthenticated){
            String message = requestBody.get("message");
            messagingService.postMessage(username, message);
            return ResponseEntity.ok("Successfully inserted message.");
        } else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong username and/or password.");
        }
    }

    @PostMapping("/admin/getStatistics")
    public ResponseEntity getStatistics(@RequestBody Map<String, String> requestBody) {
        String username = requestBody.get(USERNAME);
        String password = requestBody.get(PASSWORD);
        boolean isAdmin = messagingService.isUserAdmin(username, password);
        if(isAdmin){
            return ResponseEntity.ok(messagingService.getStatistics());
        } else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

    }

    @PostMapping("/admin/createNewUser")
    public ResponseEntity<String> createNewUser(@RequestBody Map<String, String> requestBody) {
        String username = requestBody.get(USERNAME);
        String password = requestBody.get(PASSWORD);
        if (!messagingService.usernameTaken(username)) {
            messagingService.createNewUser(username, password);
            return ResponseEntity.ok("Successfully created new user.\nUsername: " + username + "\nPassword: " + password);
        } else{
            return ResponseEntity.status(HttpStatus.CONFLICT).body(USER_ALREADY_EXISTS);
        }
    }


    @PostMapping("/admin/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestBody Map<String, String> requestBody) {
        String username = requestBody.get(USERNAME);
        String password = requestBody.get(PASSWORD);
        String usernameToDelete = requestBody.get("usernameToDelete");
        boolean isAdmin = messagingService.isUserAdmin(username, password);
        if(isAdmin){
            if(messagingService.usernameTaken(username)){
                messagingService.deleteUser(usernameToDelete);
                return ResponseEntity.ok("Successfully deleted user.\nUsername: " + username);
            } else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with username" + username + " not found.");
            }
        } else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }
    }
}
