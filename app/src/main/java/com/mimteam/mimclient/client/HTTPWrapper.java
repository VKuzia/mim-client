package com.mimteam.mimclient.client;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;

import java.util.HashMap;
import java.util.List;

public class HTTPWrapper {

    private final HTTPClient httpClient;

    public HTTPWrapper(UserInfo userInfo, String url) {
        this.httpClient = new HTTPClient(userInfo, url);
    }

    public Optional<Integer> createChat(String chatName) {
        HashMap<String, String> params = new HashMap<String, String>() {{
            put("chatName", chatName);
        }};
        Optional<String> response = httpClient.post("/chats/create", new HashMap<>(), params);
        if (response.isPresent()) {
            return Optional.of(Integer.valueOf(response.get()));
        }
        return Optional.absent();
    }

    public Optional<Integer> joinChat(Integer chatId) {
        HashMap<String, String> params = new HashMap<String, String>() {{
            put("userId", httpClient.getUserInfo().getId().toString());
        }};
        Optional<String> response = httpClient.post("/chats/" + chatId + "/join", new HashMap<>(), params);
        if (response.isPresent()) {
            return Optional.of(Integer.valueOf(response.get()));
        }
        return Optional.absent();
    }

    public Optional<String> leaveChat(Integer chatId) {
        HashMap<String, String> params = new HashMap<String, String>() {{
            put("userId", httpClient.getUserInfo().getId().toString());
        }};
        return httpClient.post("/chats/" + chatId + "/leave", new HashMap<>(), params);
    }

    public Optional<List<Integer>> getUserList(Integer chatId) {
        Optional<String> response = httpClient.get("/chats/" + chatId + "/userlist",
                new HashMap<>(), new HashMap<>());
        if (!response.isPresent()) {
            return Optional.absent();
        }
        ObjectMapper mapper = new ObjectMapper();
        List<Integer> list = mapper.convertValue(response.get(), new TypeReference<List<Integer>>() {});
        return Optional.of(list);
    }

    public Optional<String> signUp(String name, String login, String password) {
        HashMap<String, String> params = new HashMap<String, String>() {{
            put("name", name);
            put("login", login);
            put("password", password);
        }};
        return httpClient.post("/users/signup", new HashMap<>(), params, false);
    }

    public Optional<Integer> login(String login, String password) {
        HashMap<String, String> params = new HashMap<String, String>() {{
            put("login", login);
            put("password", password);
        }};
        Optional<String> response =
                httpClient.post("/users/login", new HashMap<>(), params, false);
        if (response.isPresent()) {
            return Optional.of(Integer.valueOf(response.get()));
        }
        return Optional.absent();
    }

    public Optional<List<Integer>> getChatsList() {
        Integer userId = httpClient.getUserInfo().getId();
        Optional<String> response = httpClient.get("/users/" + userId + "/chatlist",
                new HashMap<>(), new HashMap<>());
        if (!response.isPresent()) {
            return Optional.absent();
        }
        ObjectMapper mapper = new ObjectMapper();
        List<Integer> list = mapper.convertValue(response.get(), new TypeReference<List<Integer>>() {});
        return Optional.of(list);
    }


}
