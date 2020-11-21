import com.google.common.base.Optional;
import com.mimteam.mimclient.client.HTTPWrapper;
import com.mimteam.mimclient.client.UserInfo;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(OrderAnnotation.class)
public class HttpGetPostTests {
    private final String url = "http://127.0.0.1:8080";
    private final String name = "LocalTestUser";
    private final String login = "LocalTestUser";
    private final String password = "local";
    private final String chatName = "LocalTestChat";
    private UserInfo userInfo = new UserInfo(1);
    private Integer chatId = 2;
    private HTTPWrapper httpWrapper = new HTTPWrapper(userInfo, url);

    @Test
    @Order(1)
    public void httpWrapper_local_signUp() {
        Optional<String> result = httpWrapper.signUp(name, login, password);
        assertTrue(result.isPresent());
    }

    @Test
    @Order(2)
    public void httpWrapper_local_login() {
        Optional<Integer> id = httpWrapper.login(login, password);
        assertTrue(id.isPresent());
        userInfo.setId(id.get());
        System.out.println(id.get());
    }

    @Test
    @Order(3)
    public void httpWrapper_local_chatList() {
        Optional<List<Integer>> chatList = httpWrapper.getChatsList();
        assertTrue(chatList.isPresent());
        System.out.println(chatList);
    }

    @Test
    @Order(4)
    public void httpWrapper_local_create() {
        Optional<Integer> chatId = httpWrapper.createChat(chatName);
        assertTrue(chatId.isPresent());
        this.chatId = chatId.get();
        System.out.println(this.chatId);
    }

    @Test
    @Order(5)
    public void httpWrapper_local_join() {
        Optional<String> result = httpWrapper.joinChat(chatId);
        assertTrue(result.isPresent());
    }

    @Test
    @Order(6)
    public void httpWrapper_local_userList() {
        Optional<List<Integer>> userList = httpWrapper.getUserList(chatId);
        assertTrue(userList.isPresent());
        System.out.println(userList);
    }

    @Test
    @Order(7)
    public void httpWrapper_local_leave() {
        Optional<String> result = httpWrapper.leaveChat(chatId);
        assertTrue(result.isPresent());
    }

}

