package com.mimteam.mimclient.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import java.util.List;

public class HTTPWrapper {

    private final HTTPClient httpClient;

    public HTTPWrapper(HTTPClient httpClient) {
        this.httpClient = httpClient;
    }

    public Optional<Integer> createChat(String chatName) {
        ImmutableMap<String, String> params = ImmutableMap.of("chatName", chatName);
        Optional<String> response = httpClient.post("/chats/create", params);
        return parseResponse(response.orNull(), new TypeReference<Integer>() {});
    }

    public Optional<String> joinChat(Integer chatId) {
        ImmutableMap<String, String> params = ImmutableMap.of("userId",
                httpClient.getUserInfo().getId().toString());
        return httpClient.post("/chats/" + chatId + "/join", params);
    }

    public Optional<String> leaveChat(Integer chatId) {
        ImmutableMap<String, String> params = ImmutableMap.of("userId",
                httpClient.getUserInfo().getId().toString());
        return httpClient.post("/chats/" + chatId + "/leave", params);
    }

    public Optional<List<Integer>> getUserList(Integer chatId) {
        Optional<String> response = httpClient.get("/chats/" + chatId + "/userlist");
        return parseResponse(response.orNull(), new TypeReference<List<Integer>>() {});
    }

    public Optional<String> signUp(String name, String login, String password) {
        ImmutableMap<String, String> params = ImmutableMap.of(
                "name", name, "login", login, "password", password);
        return httpClient.post("/users/signup", params);
    }

    public Optional<Integer> login(String login, String password) {
        ImmutableMap<String, String> params = ImmutableMap.of(
                "login", login, "password", password);
        Optional<String> response = httpClient.post("/users/login", params);
        return parseResponse(response.orNull(), new TypeReference<Integer>() {});
    }

    public Optional<List<Integer>> getChatsList() {
        Integer userId = httpClient.getUserInfo().getId();
        Optional<String> response = httpClient.get("/users/" + userId + "/chatlist");
        return parseResponse(response.orNull(), new TypeReference<List<Integer>>() {});
    }

    private static <T> Optional<T> parseResponse(String response, TypeReference<T> outputType) {
        if (response == null) {
            return Optional.absent();
        }
        ObjectMapper mapper = new ObjectMapper();
        T result = null;
        try {
            result = mapper.readValue(response, outputType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Optional.fromNullable(result);
    }
}