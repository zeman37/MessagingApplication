package com.messaging.application.service;

import com.messaging.application.models.Message;
import org.jooq.DSLContext;
import org.jooq.Record7;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.application.generated.db.Tables.MESSAGES;
import static com.application.generated.db.Tables.USERS;

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
        return dslContext.selectFrom(MESSAGES).fetch().into(Message.class);
    }

    public void postMessage(String username, String password, String message){
        dslContext.insertInto(MESSAGES, MESSAGES.USER_ID, MESSAGES.TEXT, MESSAGES.MESSAGE_DATE)
                .values(1, message, LocalDateTime.now()).execute();
    }

    public Map<String, Object> getStatistics(){
        Result<Record7<Integer, String, Integer, LocalDateTime, LocalDateTime, BigDecimal, String>> result =
                dslContext.select(USERS.ID, USERS.USERNAME,
                                DSL.count(),
                                DSL.min(MESSAGES.MESSAGE_DATE),
                                DSL.max(MESSAGES.MESSAGE_DATE),
                                DSL.avg(DSL.length(MESSAGES.TEXT)),
                                DSL.max(MESSAGES.TEXT))
                        .from(USERS)
                        .join(MESSAGES)
                        .on(USERS.ID.eq(MESSAGES.USER_ID))
                        .where(USERS.ID.eq(MESSAGES.USER_ID))
                        .groupBy(USERS.ID)
                        .fetch();

        Map<String, Object> statisticsMap = new HashMap<>();
        for (Record7<Integer, String, Integer, LocalDateTime, LocalDateTime, BigDecimal, String> recordFromDb : result) {
            statisticsMap.put("User Id", recordFromDb.value1());
            statisticsMap.put("Username", recordFromDb.value2());
            statisticsMap.put("Message Count", recordFromDb.value3());
            statisticsMap.put("First Message Time", recordFromDb.value4());
            statisticsMap.put("Last Message Time", recordFromDb.value5());
            statisticsMap.put("Average Message Length", recordFromDb.value6().intValue());
            statisticsMap.put("Last Message Text", recordFromDb.value7());

        }
        return statisticsMap;
    }

    public void createNewUser(String username, String password){
        dslContext.insertInto(USERS, USERS.USERNAME, USERS.PASSWORD, USERS.IS_ADMIN)
                .values(username, password, Boolean.FALSE).execute();
    }

}
