package com.mimteam.mimclient.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;

import java.util.HashMap;
import java.util.List;

public class HTTPWrapper {

    private final HTTPClient httpClient;

    public HTTPWrapper(HTTPClient httpClient) {
        this.httpClient = httpClient;
    }

    public Optional<Integer> createChat(String chatName) {
        HashMap<String, String> params = new HashMap<String, String>() {{
            put("chatName", chatName);
        }};
        Optional<String> response = httpClient.post("/chats/create", params);
        return toOptional(response.orNull(), new TypeReference<Integer>() {
        });
    }

    public Optional<String> joinChat(Integer chatId) {
        HashMap<String, String> params = new HashMap<String, String>() {{
            put("userId", httpClient.getUserInfo().getId().toString());
        }};
        return httpClient.post("/chats/" + chatId + "/join", params);
    }

    public Optional<String> leaveChat(Integer chatId) {
        HashMap<String, String> params = new HashMap<String, String>() {{
            put("userId", httpClient.getUserInfo().getId().toString());
        }};
        return httpClient.post("/chats/" + chatId + "/leave", params);
    }

    public Optional<List<Integer>> getUserList(Integer chatId) {
        Optional<String> response = httpClient.get("/chats/" + chatId + "/userlist", new HashMap<>());
        return toOptional(response.orNull(), new TypeReference<List<Integer>>() {
        });
    }

    public Optional<String> signUp(String name, String login, String password) {
        HashMap<String, String> params = new HashMap<String, String>() {{
            put("name", name);
            put("login", login);
            put("password", password);
        }};
        return httpClient.post("/users/signup", params);
    }

    public Optional<Integer> login(String login, String password) {
        HashMap<String, String> params = new HashMap<String, String>() {{
            put("login", login);
            put("password", password);
        }};
        Optional<String> response =
                httpClient.post("/users/login", params);
        return toOptional(response.orNull(), new TypeReference<Integer>() {
        });
    }

    public Optional<List<Integer>> getChatsList() {
        Integer userId = httpClient.getUserInfo().getId();
        Optional<String> response = httpClient.get("/users/" + userId + "/chatlist", new HashMap<>());
        return toOptional(response.orNull(), new TypeReference<List<Integer>>() {
        });
    }

    private static <T> Optional<T> toOptional(String data, TypeReference<T> type) {
        if (data != null) {
            ObjectMapper mapper = new ObjectMapper();
            T result = null;
            try {
                result = mapper.readValue(data, type);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return Optional.fromNullable(result);
        }
        return Optional.absent();
    }
}
