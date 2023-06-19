package com.messaging.application.controller;


import com.messaging.application.models.Message;
import com.messaging.application.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MessageController {

    @Autowired
    MessagingService messagingService;
    @GetMapping("/getMessages")
    public List<Message> getAllMessages() {
        return messagingService.getAllMessages();
    }

    @PostMapping("/postMessage")
    public String postMessage(@RequestBody Map<String, String> requestBody) {
        String username = requestBody.get("username");
        String password = requestBody.get("password");
        String message = requestBody.get("message");
        messagingService.postMessage(username, password, message);
        return "Successfully inserted message.";
    }

    @GetMapping("/admin/getStatistics")
    public Map<String, Object> getStatistics() {
        return messagingService.getStatistics();
    }

    @PostMapping("/admin/createNewUser")
    public String createNewUser(@RequestBody Map<String, String> requestBody) {
        String username = requestBody.get("username");
        String password = requestBody.get("password");
        messagingService.createNewUser(username, password);
        return "Successfully created new user.\nUsername: " + username + "\nPassword: " + password;
    }
}
