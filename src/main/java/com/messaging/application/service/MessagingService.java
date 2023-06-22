package com.messaging.application.service;

import com.messaging.application.models.Message;
import com.messaging.application.models.User;
import org.jooq.DSLContext;
import org.jooq.Record;
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
import java.util.*;

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

    public void postMessage(String username, String message){
        dslContext.insertInto(MESSAGES, MESSAGES.USER_ID, MESSAGES.TEXT, MESSAGES.MESSAGE_DATE)
                .values(dslContext.select(USERS.ID).from(USERS).where(USERS.USERNAME.eq(username)).fetch().get(0).value1(),
                        message, LocalDateTime.now()).execute();
    }

    public Map<String, Object> getStatistics(){
        Result<Record7<Integer, String, Integer, LocalDateTime, LocalDateTime, BigDecimal, String>> result =
                dslContext.select(USERS.ID, USERS.USERNAME,
                                DSL.count(),
                                DSL.min(MESSAGES.MESSAGE_DATE),
                                DSL.max(MESSAGES.MESSAGE_DATE),
                                DSL.avg(DSL.length(MESSAGES.TEXT)),
                                DSL.field(dslContext.select(MESSAGES.TEXT).from(MESSAGES).where(USERS.ID.eq(MESSAGES.USER_ID)).orderBy(MESSAGES.MESSAGE_DATE.desc()).limit(1)))
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

    public void deleteUser(String username){
        dslContext.deleteFrom(USERS)
                .where(USERS.USERNAME.eq(username))
                .execute();
    }

    public Record queryUser(String username, String password){
        return dslContext
                .selectFrom(USERS)
                .where(USERS.USERNAME.eq(username)
                        .and(USERS.PASSWORD.eq(password)))
                .fetchOne();
    }

    public User queryUserByUsername(String username){
        return User.mapRecordToUser(Objects.requireNonNull(dslContext
                .selectFrom(USERS)
                .where(USERS.USERNAME.eq(username))
                .fetchOne()));
    }

    public boolean checkIfUserIsRegistered(String username, String password){
        return queryUser(username, password) != null;
    }

    public boolean isUserAdmin(String username, String password){
        Record userFromDb = queryUser(username, password);
        if(userFromDb != null){
            return userFromDb.get(USERS.IS_ADMIN);
        } else{
            return false;
        }
    }

    public boolean usernameTaken(String username){
        Integer result = dslContext.selectCount()
                .from(USERS)
                .where(USERS.USERNAME.eq(username))
                .fetchOne(0, Integer.class);
        return (result != null && result == 1);
    }

    public DSLContext getDslContext(){
        return this.dslContext;
    }

}
