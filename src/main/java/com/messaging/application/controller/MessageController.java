package com.messaging.application.controller;


import com.messaging.application.models.Message;
import com.messaging.application.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MessageController {

    @Autowired
    MessagingService messagingService;
    private static final String NOT_IMPLEMENTED = "Not implemented yet.";
    @GetMapping("/getMessages")
    public List<Message> getAllMessages() {
        return messagingService.getAllMessages();
    }

    @PostMapping("/postMessage")
    public String postMessage() {
        return NOT_IMPLEMENTED;
    }

    @GetMapping("/admin/getStatistics")
    public String getStatistics() {
        return NOT_IMPLEMENTED;
    }
}
