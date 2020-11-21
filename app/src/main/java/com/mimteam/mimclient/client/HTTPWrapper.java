package com.mimteam.mimclient.client;
import java.util.HashMap;

public class HTTPWrapper {

    private final HTTPClient httpClient;

    public HTTPWrapper(UserInfo userInfo, String url) {
        this.httpClient = new HTTPClient(userInfo, url);
    }

    public String createChat(String chatName) {
        HashMap<String, String> params = new HashMap<String, String>() {{
            put("chatName", chatName);
        }};
        return httpClient.post("/chats/create", new HashMap<>(), params);
    }

    public String joinChat(Integer chatId) {
        HashMap<String, String> params = new HashMap<String, String>() {{
            put("userId", httpClient.getUserInfo().getId().toString());
        }};
        return httpClient.post("/chats/" + chatId + "/join", new HashMap<>(), params);
    }

    public String leaveChat(Integer chatId) {
        HashMap<String, String> params = new HashMap<String, String>() {{
            put("userId", httpClient.getUserInfo().getId().toString());
        }};
        return httpClient.post("/chats/" + chatId + "/leave", new HashMap<>(), params);
    }

    public String getUserList(Integer chatId) {
        return httpClient.get("/chats/" + chatId + "/userlist",
                new HashMap<>(), new HashMap<>());
    }

    public String signUp(String name, String login, String password) {
        HashMap<String, String> params = new HashMap<String, String>() {{
            put("name", name);
            put("login", login);
            put("password", password);
        }};
        return httpClient.post("/users/signup", new HashMap<>(), params, false);
    }

    public String login(String login, String password) {
        HashMap<String, String> params = new HashMap<String, String>() {{
            put("login", login);
            put("password", password);
        }};
        return httpClient.post("/users/login", new HashMap<>(), params, false);
    }

    public String getChatsList() {
        Integer userId = httpClient.getUserInfo().getId();
        return httpClient.get("/users/" + userId + "/chatlist",
                new HashMap<>(), new HashMap<>());

    }
}
