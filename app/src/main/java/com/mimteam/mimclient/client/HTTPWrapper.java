package com.mimteam.mimclient.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.mimteam.mimclient.models.dto.ChatDTO;
import com.mimteam.mimclient.models.dto.MessageDTO;
import com.mimteam.mimclient.models.dto.UserDTO;

import java.util.List;

public class HTTPWrapper {

    private final HTTPClient httpClient;

    public HTTPWrapper(HTTPClient httpClient) {
        this.httpClient = httpClient;
    }

    public Optional<ChatDTO> createChat(String chatName) {
        ImmutableMap<String, String> params = ImmutableMap.of("chatName", chatName);
        Optional<String> response = httpClient.post("/chats/create", params);
        return parseResponse(response.orNull(), new TypeReference<ChatDTO>() {});
    }

    public Optional<ChatDTO> joinChat(String key) {
        ImmutableMap<String, String> params = ImmutableMap.of("invitationKey", key);
        Optional<String> response = httpClient.post("/chats/join", params);
        return parseResponse(response.orNull(), new TypeReference<ChatDTO>() {});
    }

    public Optional<String> leaveChat(Integer chatId) {
        ImmutableMap<String, String> params = ImmutableMap.of("userId",
                httpClient.getUserInfo().getId().toString());
        return httpClient.post("/chats/" + chatId + "/leave", params);
    }

    public Optional<List<UserDTO>> getUserList(Integer chatId) {
        Optional<String> response = httpClient.get("/chats/" + chatId + "/userlist");
        return parseResponse(response.orNull(), new TypeReference<List<UserDTO>>() {});
    }

    public Optional<String> getInvitationKey(Integer chatId) {
        Optional<String> response = httpClient.get("/chats/" + chatId + "/invitation");
        return parseResponse(response.orNull(), new TypeReference<String>() {});
    }

    public Optional<String> signUp(String name, String login, String password) {
        ImmutableMap<String, String> params = ImmutableMap.of(
                "name", name, "login", login, "password", password);
        return httpClient.post("/users/signup", params);
    }

    public Optional<String> login(String login, String password) {
        ImmutableMap<String, String> params = ImmutableMap.of(
                "login", login, "password", password);
        return  httpClient.post("/users/login", params);
    }

    public Optional<List<ChatDTO>> getChatsList() {
        Optional<String> response = httpClient.get("/users/chatlist");
        return parseResponse(response.orNull(), new TypeReference<List<ChatDTO>>() {});
    }

    public Optional<List<MessageDTO>> getChatMessages(Integer chatId) {
        Optional<String> response = httpClient.get("/chats/" + chatId + "/messages");
        return parseResponse(response.orNull(), new TypeReference<List<MessageDTO>>() {});
    }

    public Optional<Integer> getUserId() {
        Optional<String> response = httpClient.get("/users/getid");
        return parseResponse(response.orNull(), new TypeReference<Integer>() {});
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
