package com.messaging.application.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeletionForm {
    private String username;
    private String password;
    private String usernameToDelete;
}
