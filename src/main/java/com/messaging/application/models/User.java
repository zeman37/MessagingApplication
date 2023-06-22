package com.messaging.application.models;

import com.application.generated.db.tables.Users;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jooq.Record;

import static com.application.generated.db.tables.Users.USERS;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "is_admin")
    private boolean isAdmin;

    public static User mapRecordToUser(Record userRecord) {
        User user = new User();
        user.setId(userRecord.get(USERS.ID));
        user.setUsername(userRecord.get(USERS.USERNAME));
        user.setPassword(userRecord.get(USERS.PASSWORD));
        user.setAdmin(userRecord.get(USERS.IS_ADMIN));
        return user;
    }
}