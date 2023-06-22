package com.messaging.application.controller;


import com.messaging.application.models.DeletionForm;
import com.messaging.application.models.LoginForm;
import com.messaging.application.models.PostMessageForm;
import com.messaging.application.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MessageController {
    @Autowired
    MessagingService messagingService;

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String USER_ALREADY_EXISTS = "User already exists.";
    @PostMapping("/getMessages")
    public ResponseEntity getAllMessages(@RequestBody LoginForm loginForm) {
        boolean isAuthenticated = messagingService.checkIfUserIsRegistered(loginForm.getUsername(), loginForm.getPassword());
        if(isAuthenticated){
            return ResponseEntity.ok(messagingService.getAllMessages());
        } else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong username and/or password.");
        }

    }

    @PostMapping("/postMessage")
    public ResponseEntity<String> postMessage(@RequestBody PostMessageForm postMessageForm) {
        String username = postMessageForm.getUsername();
        String password = postMessageForm.getPassword();
        boolean isAuthenticated = messagingService.checkIfUserIsRegistered(username, password);
        if(isAuthenticated){
            String message = postMessageForm.getMessageToPost();
            messagingService.postMessage(username, message);
            return ResponseEntity.ok("Successfully inserted message.");
        } else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong username and/or password.");
        }
    }

    @PostMapping("/admin/getStatistics")
    public ResponseEntity getStatistics(@RequestBody LoginForm loginForm) {
        boolean isAdmin = messagingService.isUserAdmin(loginForm.getUsername(), loginForm.getPassword());
        if(isAdmin){
            return ResponseEntity.ok(messagingService.getStatistics());
        } else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

    }

    @PostMapping("/admin/createNewUser")
    public ResponseEntity<String> createNewUser(@RequestBody LoginForm loginForm) {
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();
        if (!messagingService.usernameTaken(loginForm.getUsername())) {
            messagingService.createNewUser(username, password);
            return ResponseEntity.ok("Successfully created new user.\nUsername: " + username + "\nPassword: " + password);
        } else{
            return ResponseEntity.status(HttpStatus.CONFLICT).body(USER_ALREADY_EXISTS);
        }
    }


    @PostMapping("/admin/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestBody DeletionForm deletionForm) {
        String username = deletionForm.getUsername();
        String password = deletionForm.getPassword();
        String usernameToDelete = deletionForm.getUsernameToDelete();
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
