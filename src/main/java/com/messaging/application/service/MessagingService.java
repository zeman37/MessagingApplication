package com.messaging.application.service;

import com.application.generated.db.Tables;
import com.messaging.application.models.Message;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class MessagingService {

    private DSLContext dslContext;

    @Autowired
    public MessagingService(Environment environment) throws SQLException {
        this.dslContext = DSL.using(
                DriverManager.getConnection(
                        environment.getProperty("spring.datasource.url"),
                        environment.getProperty("spring.datasource.username"),
                        Optional.ofNullable(environment.getProperty("spring.datasource.password")).orElse("")));
    }

    public List<Message> getAllMessages() {
        return dslContext.selectFrom(Tables.MESSAGES).fetch().into(Message.class);
    }

}
