package com.messaging.application.controller;


import com.messaging.application.models.Message;
import com.messaging.application.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MessageController {
    @Autowired
    MessagingService messagingService;

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    @GetMapping("/getMessages")
    public List<Message> getAllMessages() {
        return messagingService.getAllMessages();
    }

    @PostMapping("/postMessage")
    public ResponseEntity<String> postMessage(@RequestBody Map<String, String> requestBody) {
        boolean isAuthenticated = messagingService.checkUser(requestBody);
        if(isAuthenticated){
            String username = requestBody.get(USERNAME);
            String message = requestBody.get("message");
            messagingService.postMessage(username, message);
            return ResponseEntity.ok("Successfully inserted message.");
        } else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/admin/getStatistics")
    public Map<String, Object> getStatistics() {
        return messagingService.getStatistics();
    }

    @PostMapping("/admin/createNewUser")
    public String createNewUser(@RequestBody Map<String, String> requestBody) {
        String username = requestBody.get(USERNAME);
        String password = requestBody.get(PASSWORD);
        messagingService.createNewUser(username, password);
        return "Successfully created new user.\nUsername: " + username + "\nPassword: " + password;
    }

    @PostMapping("/admin/deleteUser")
    public String deleteUser(@RequestBody Map<String, String> requestBody) {
        String username = requestBody.get(USERNAME);
        messagingService.deleteUser(username);
        return "Successfully deleted user.\nUsername: " + username;
    }
}
