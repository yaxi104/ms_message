package com.hexagonal.ms_message.application.handler.impl;

import com.hexagonal.ms_message.domain.api.IMessageServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class MessageHandlerTest {

    private IMessageServicePort messageServicePort;
    private MessageHandler messageHandler;

    @BeforeEach
    void setUp() {
        messageServicePort = mock(IMessageServicePort.class);
        messageHandler = new MessageHandler(messageServicePort);
    }

    @Test
    void sendMessagesDelegatesToServicePort() {
        messageHandler.sendMessages();

        verify(messageServicePort, times(1)).sendMessages();
    }
}