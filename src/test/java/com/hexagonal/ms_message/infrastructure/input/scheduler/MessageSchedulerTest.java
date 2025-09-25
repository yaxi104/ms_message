package com.hexagonal.ms_message.infrastructure.input.scheduler;

import com.hexagonal.ms_message.application.handler.IMessageHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class MessageSchedulerTest {

    private IMessageHandler messageHandler;
    private MessageScheduler messageScheduler;

    @BeforeEach
    void setUp() {
        messageHandler = mock(IMessageHandler.class);
        messageScheduler = new MessageScheduler(messageHandler);
    }

    @Test
    void pollQueueShouldCallSendMessages() {
        messageScheduler.pollQueue();
        verify(messageHandler, times(1)).sendMessages();
    }
}