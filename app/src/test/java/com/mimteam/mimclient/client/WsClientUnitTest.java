package com.mimteam.mimclient.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mimteam.mimclient.models.dto.MessageDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.LifecycleEvent;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;

public class WsClientUnitTest {
    private final static String url = "ws://10.0.2.2:8080/ws";
    private final static Integer userId = 0;
    private final static Integer chatId = 0;
    private final static String content = "Test";
    private final static String messageDestination = "/app/chats/" + chatId + "/message";
    private final static String subscribePath = "/chats/" + chatId;
    private final static StompClient realClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)
            .withClientHeartbeat(1000)
            .withServerHeartbeat(1000);
    private final static Flowable<LifecycleEvent> lifecycleFlowable = realClient.lifecycle();
    private final static List<StompHeader> realHeaders = Collections.singletonList(new StompHeader("Authorization", "Bearer "));
    private final static Flowable<StompMessage> stompMessageFlowable =
            realClient.topic(subscribePath, realHeaders);
    private final static Completable completable = new Completable() {
        @Override
        protected void subscribeActual(CompletableObserver observer) {
        }
    };
    private String messagePayload;
    private WsClient wsClient;
    private MessageDto messageDto;

    @BeforeEach
    public void init() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userId);
        wsClient = new WsClient(userInfo);
        messageDto = new MessageDto();
        messageDto.setUserId(userId);
        messageDto.setChatId(chatId);
        messageDto.setContent(content);
        try {
            messagePayload = new ObjectMapper().writeValueAsString(messageDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void wsClientConnectTest() {
        StompClient stompClientMock = Mockito.mock(StompClient.class);
        Mockito.when(stompClientMock.lifecycle())
                .thenReturn(lifecycleFlowable);
        try (MockedStatic<WsClient> staticWsClient = Mockito.mockStatic(WsClient.class)) {
            staticWsClient.when(() -> WsClient.createStompClient(Mockito.eq(url), Mockito.any()))
                    .thenReturn(stompClientMock);
            wsClient.connect(url);
            staticWsClient.verify(() -> WsClient.createStompClient(Mockito.eq(url), Mockito.any()));
        }
        Mockito.verify(stompClientMock).lifecycle();
        Mockito.verify(stompClientMock).connect(Mockito.any());
    }

    @Test
    public void wsClientSendMessageConnectionFailTest() {
        StompClient stompClientMock = Mockito.mock(StompClient.class);
        MessageDto message = Mockito.mock(MessageDto.class);
        Mockito.when(message.getChatId()).thenReturn(0);
        try (MockedStatic<WsClient> staticWsClient = Mockito.mockStatic(WsClient.class)) {
            staticWsClient.when(() -> WsClient.createStompClient(Mockito.anyString()))
                    .thenReturn(stompClientMock);
            wsClient.sendMessage(message);
            staticWsClient.verify(Mockito.never(),
                    () -> WsClient.createStompClient(Mockito.anyString()));
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
        Mockito.when(stompClientMock.send(Mockito.any(StompMessage.class)))
                .thenReturn(completable);
        try (MockedStatic<WsClient> staticWsClient = Mockito.mockStatic(WsClient.class)) {
            staticWsClient.when(() -> WsClient.createStompClient(Mockito.eq(url), Mockito.any()))
                    .thenReturn(stompClientMock);
            wsClient.connect(url);
            wsClient.sendMessage(messageDto);
            staticWsClient.verify(() -> WsClient.createStompClient(Mockito.eq(url), Mockito.any()));
        }
        Mockito.verify(stompClientMock).connect(Mockito.any());
        Mockito.verify(stompClientMock).send(Mockito.any(StompMessage.class));
    }

    @Test
    public void wsClientSubscribeFailTest() {
        StompClient stompClientMock = Mockito.mock(StompClient.class);
        try (MockedStatic<WsClient> staticWsClient = Mockito.mockStatic(WsClient.class)) {
            staticWsClient.when(() -> WsClient.createStompClient(Mockito.anyString()))
                    .thenReturn(stompClientMock);
            wsClient.subscribe(chatId);
            staticWsClient.verify(Mockito.never(),
                    () -> WsClient.createStompClient(Mockito.anyString()));
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
        Mockito.when(stompClientMock.topic(Mockito.anyString(), Mockito.any()))
                .thenReturn(stompMessageFlowable);
        try (MockedStatic<WsClient> staticWsClient = Mockito.mockStatic(WsClient.class)) {
            staticWsClient.when(() -> WsClient.createStompClient(Mockito.eq(url), Mockito.any()))
                    .thenReturn(stompClientMock);
            wsClient.connect(url);
            wsClient.subscribe(chatId);
            staticWsClient.verify(() -> WsClient.createStompClient(Mockito.eq(url), Mockito.any()));
        }
        Mockito.verify(stompClientMock).topic(Mockito.eq(subscribePath), Mockito.any());
    }
}
