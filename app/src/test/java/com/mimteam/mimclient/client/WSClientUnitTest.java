package com.mimteam.mimclient.client;

import com.squareup.okhttp.OkHttpClient;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class WSClientUnitTest {

    private final static String url = "ws://localhost:8080/ws";
    private WSClient wsClient;

    @BeforeEach
    public void init() {
        UserInfo userInfo = new UserInfo(0);
        wsClient = new WSClient(userInfo);
    }

    @Test
    public void wsClientConnectTest() {
        StompClient stompClientMock = Mockito.mock(StompClient.class);
        Mockito.when(stompClientMock.lifecycle())
                .thenReturn(Stomp.over(Stomp.ConnectionProvider.OKHTTP, url).lifecycle());
        try (MockedStatic<WSClient> staticWSClient = Mockito.mockStatic(WSClient.class)) {
            staticWSClient.when(() -> WSClient.createStompClient(Mockito.anyString()))
                    .thenReturn(stompClientMock);

            wsClient.connect(url);

            staticWSClient.verify(() -> WSClient.createStompClient(Mockito.anyString()));
        }
        Mockito.verify(stompClientMock).lifecycle();
        Mockito.verify(stompClientMock).connect();
    }
}
