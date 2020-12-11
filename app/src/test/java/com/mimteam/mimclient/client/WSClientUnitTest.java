package com.mimteam.mimclient.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mimteam.mimclient.models.ws.MessageDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.LifecycleEvent;
import ua.naiksoftware.stomp.dto.StompMessage;

public class WSClientUnitTest {
    private final static String url = "ws://localhost:8080/ws";
    private final static Integer userId = 0;
    private final static Integer chatId = 0;
    private final static String content = "Test";
    private final static String messageDestination = "/app/chats/" + chatId + "/message";
    private final static String subscribePath = "/chats/" + chatId;
    private final static StompClient realClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)
            .withClientHeartbeat(1000)
            .withServerHeartbeat(1000);
    private final static Flowable<LifecycleEvent> lifecycleFlowable = realClient.lifecycle();
    private final static Flowable<StompMessage> stompMessageFlowable =
            realClient.topic(subscribePath);
    private final static Completable completable = new Completable() {
        @Override
        protected void subscribeActual(CompletableObserver observer) {
        }
    };
    private String messagePayload;
    private WSClient wsClient;
    private MessageDTO messageDTO;

    @BeforeEach
    public void init() {
        UserInfo userInfo = new UserInfo(userId);
        wsClient = new WSClient(userInfo);
        messageDTO = new MessageDTO();
        messageDTO.setUserId(userId);
        messageDTO.setChatId(chatId);
        messageDTO.setContent(content);
        try {
            messagePayload = new ObjectMapper().writeValueAsString(messageDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void wsClientConnectTest() {
        StompClient stompClientMock = Mockito.mock(StompClient.class);
        Mockito.when(stompClientMock.lifecycle())
                .thenReturn(lifecycleFlowable);
        try (MockedStatic<WSClient> staticWSClient = Mockito.mockStatic(WSClient.class)) {
            staticWSClient.when(() -> WSClient.createStompClient(Mockito.eq(url)))
                    .thenReturn(stompClientMock);
            wsClient.connect(url);
            staticWSClient.verify(() -> WSClient.createStompClient(Mockito.eq(url)));
        }
        Mockito.verify(stompClientMock).lifecycle();
        Mockito.verify(stompClientMock).connect();
    }

    @Test
    public void wsClientSendMessageConnectionFailTest() {
        StompClient stompClientMock = Mockito.mock(StompClient.class);
        MessageDTO message = Mockito.mock(MessageDTO.class);
        Mockito.when(message.getChatId()).thenReturn(0);
        try (MockedStatic<WSClient> staticWSClient = Mockito.mockStatic(WSClient.class)) {
            staticWSClient.when(() -> WSClient.createStompClient(Mockito.anyString()))
                    .thenReturn(stompClientMock);
            wsClient.sendMessage(message);
            staticWSClient.verify(Mockito.never(),
                    () -> WSClient.createStompClient(Mockito.anyString()));
        }
        Mockito.verify(message, Mockito.never()).getChatId();
        Mockito.verify(stompClientMock, Mockito.never())
                .send(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void wsClientSendMessageSuccessTest() {
        StompClient stompClientMock = Mockito.mock(StompClient.class);
        Mockito.when(stompClientMock.lifecycle())
                .thenReturn(lifecycleFlowable);
        Mockito.when(stompClientMock.isConnected())
                .thenReturn(true);
        Mockito.when(stompClientMock.send(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(completable);
        try (MockedStatic<WSClient> staticWSClient = Mockito.mockStatic(WSClient.class)) {
            staticWSClient.when(() -> WSClient.createStompClient(Mockito.anyString()))
                    .thenReturn(stompClientMock);
            wsClient.connect(url);
            wsClient.sendMessage(messageDTO);
            staticWSClient.verify(() -> WSClient.createStompClient(Mockito.eq(url)));
        }
        Mockito.verify(stompClientMock).connect();
        Mockito.verify(stompClientMock)
                .send(Mockito.eq(messageDestination), Mockito.eq(messagePayload));
    }

    @Test
    public void wsClientSubscribeFailTest() {
        StompClient stompClientMock = Mockito.mock(StompClient.class);
        try (MockedStatic<WSClient> staticWSClient = Mockito.mockStatic(WSClient.class)) {
            staticWSClient.when(() -> WSClient.createStompClient(Mockito.anyString()))
                    .thenReturn(stompClientMock);
            wsClient.subscribe(chatId);
            staticWSClient.verify(Mockito.never(),
                    () -> WSClient.createStompClient(Mockito.anyString()));
        }
        Mockito.verify(stompClientMock, Mockito.never()).topic(Mockito.anyString());
    }

    @Test
    public void wsClientSubscribeSuccessTest() {
        StompClient stompClientMock = Mockito.mock(StompClient.class);
        Mockito.when(stompClientMock.lifecycle())
                .thenReturn(lifecycleFlowable);
        Mockito.when(stompClientMock.isConnected())
                .thenReturn(true);
        Mockito.when(stompClientMock.topic(Mockito.anyString())).thenReturn(stompMessageFlowable);
        try (MockedStatic<WSClient> staticWSClient = Mockito.mockStatic(WSClient.class)) {
            staticWSClient.when(() -> WSClient.createStompClient(Mockito.anyString()))
                    .thenReturn(stompClientMock);
            wsClient.connect(url);
            wsClient.subscribe(chatId);
            staticWSClient.verify(() -> WSClient.createStompClient(Mockito.eq(url)));
        }
        Mockito.verify(stompClientMock).topic(Mockito.eq(subscribePath));
    }
}
