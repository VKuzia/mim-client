import com.mimteam.mimclient.client.HTTPWrapper;
import com.mimteam.mimclient.client.UserInfo;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import static org.junit.Assert.assertNotNull;

@TestMethodOrder(OrderAnnotation.class)
public class HttpGetPostTests {
    private final String url = "http://127.0.0.1:8080";
    private final String name = "LocalTestUser";
    private final String login = "LocalTestUser";
    private final String password = "local";
    private final String chatName = "LocalTestChat";
    private UserInfo userInfo;
    private final Integer chatId = 2;
    private HTTPWrapper httpWrapper;

    private void assertString(String string) {
        assertNotNull(string);
        System.out.println(string);
    }

    @BeforeEach
    public void initialize() {
        this.userInfo = new UserInfo(1);
        httpWrapper = new HTTPWrapper(userInfo, url);
    }

    @Test
    @Order(1)
    public void httpWrapper_local_signUp() {
        String result = httpWrapper.signUp(name, login, password);
        assertString(result);
    }

    @Test
    @Order(2)
    public void httpWrapper_local_login() {
        String result = httpWrapper.login(login, password);
        assertString(result);
    }

    @Test
    @Order(3)
    public void httpWrapper_local_chatList() {
        String result = httpWrapper.getChatsList();
        assertString(result);
    }

    @Test
    @Order(4)
    public void httpWrapper_local_create() {
        String result = httpWrapper.createChat(chatName);
        assertString(result);
    }

    @Test
    @Order(5)
    public void httpWrapper_local_join() {
        String result = httpWrapper.joinChat(chatId);
        assertString(result);
    }

    @Test
    @Order(6)
    public void httpWrapper_local_userList() {
        String result = httpWrapper.getUserList(chatId);
        assertString(result);
    }

    @Test
    @Order(7)
    public void httpWrapper_local_leave() {
        String result = httpWrapper.leaveChat(chatId);
        assertString(result);
    }

}

