package com.messaging.application

import com.messaging.application.service.MessagingService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import static com.application.generated.db.Tables.USERS


@SpringBootTest
class MessagingServiceTests {

    @Autowired
    MessagingService messagingService;

    def adminId = 1
    def adminUsername = "admin"
    def adminPassword = "probablyCh@ngeME"

    def randomUserId = 2
    def randomUserName = "usern"
    def randomUserPass = "passw"

    @Test
    void 'shouldGetUser'(){
        assert messagingService.queryUser(adminUsername, adminPassword).get(USERS.ID) == adminId
    }

    @Test
    void 'shouldCreateUser'(){
        messagingService.getDslContext()
                .insertInto(USERS, USERS.USERNAME, USERS.PASSWORD, USERS.IS_ADMIN)
                .values(randomUserName, randomUserPass, false).execute()
        assert messagingService.queryUser(randomUserName, randomUserPass).get(USERS.ID) == randomUserId
    }

    @Test
    void 'shouldInsertMessage'(){
        messagingService.postMessage(adminUsername, "Hello from test.")
        assert messagingService.getAllMessages().size() == 1
    }

    @Test
    void 'shouldGetNotEmptyStatistics'(){
        assert !messagingService.getStatistics().isEmpty()
    }

    @Test
    void 'shouldNotUserRegistered'(){
        assert !messagingService.checkIfUserIsRegistered("randomUsrNm", "randomPsw")
    }

    @Test
    void 'shouldUsernameTaken'(){
        assert messagingService.usernameTaken(adminUsername)
    }

    @Test
    void 'shouldAdmin'(){
        assert messagingService.isUserAdmin(adminUsername, adminPassword)
    }

    @Test
    void 'shouldDeleteUser'(){
        messagingService.getDslContext()
                .insertInto(USERS, USERS.USERNAME, USERS.PASSWORD, USERS.IS_ADMIN)
                .values(randomUserName, randomUserPass, false).execute()
        messagingService.deleteUser(randomUserName)
        assert !messagingService.usernameTaken(randomUserName)
    }
}
