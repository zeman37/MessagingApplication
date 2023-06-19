package com.messaging.application.controller;


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MessageController {

    private static final String NOT_IMPLEMENTED = "Not implemented yet.";
    @GetMapping("/getMessages")
    public String getAllMessages() {
        return NOT_IMPLEMENTED;
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
