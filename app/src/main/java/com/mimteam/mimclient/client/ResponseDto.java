package com.mimteam.mimclient.client;

public class ResponseDto {
    private ResponseType responseType;
    private String responseMessage;

    public enum ResponseType {
        OK,
        CHAT_ALREADY_EXISTS, CHAT_NOT_EXISTS,
        USER_ALREADY_EXISTS, USER_NOT_EXISTS,
        USER_ALREADY_IN_CHAT, USER_NOT_IN_CHAT,
        INCORRECT_PASSWORD
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage != null ? responseMessage : "";
    }
}
