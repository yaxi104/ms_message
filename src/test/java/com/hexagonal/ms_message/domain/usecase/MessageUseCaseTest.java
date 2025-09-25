package com.hexagonal.ms_message.domain.usecase;

import com.hexagonal.ms_message.domain.model.OrderReadyEvent;
import com.hexagonal.ms_message.domain.model.SqsMessage;
import com.hexagonal.ms_message.domain.spi.ISmsSendPort;
import com.hexagonal.ms_message.domain.spi.ISqsServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class MessageUseCaseTest {

    private ISqsServicePort sqsServicePort;
    private ISmsSendPort snsPublisherPort;
    private MessageUseCase messageUseCase;

    @BeforeEach
    void setUp() {
        sqsServicePort = mock(ISqsServicePort.class);
        snsPublisherPort = mock(ISmsSendPort.class);
        messageUseCase = new MessageUseCase(sqsServicePort, snsPublisherPort);
    }

    @Test
    void sendMessagesShouldPublishAndDeleteEachMessage() {
        OrderReadyEvent event = new OrderReadyEvent();
        SqsMessage<OrderReadyEvent> sqsMessage = new SqsMessage<>(event, "receipt-handle-123");

        List<SqsMessage<OrderReadyEvent>> messages = List.of(sqsMessage);
        when(sqsServicePort.receiveMessages()).thenReturn(messages);

        messageUseCase.sendMessages();

        verify(snsPublisherPort).publishMessage(event);
        verify(sqsServicePort).deleteMessage("receipt-handle-123");
    }

    @Test
    void sendMessagesShouldDoNothingWhenListIsEmpty() {
        when(sqsServicePort.receiveMessages()).thenReturn(Collections.emptyList());

        messageUseCase.sendMessages();

        verifyNoInteractions(snsPublisherPort);
        verify(sqsServicePort, never()).deleteMessage(any());
    }

    @Test
    void sendMessagesShouldDoNothingWhenListIsNull() {
        when(sqsServicePort.receiveMessages()).thenReturn(null);

        messageUseCase.sendMessages();

        verifyNoInteractions(snsPublisherPort);
        verify(sqsServicePort, never()).deleteMessage(any());
    }

    @Test
    void publishMessageHandlesExceptionGracefully() {
        OrderReadyEvent event = new OrderReadyEvent();
        SqsMessage<OrderReadyEvent> message = new SqsMessage<>(event, "receipt-handle-456");

        when(sqsServicePort.receiveMessages()).thenReturn(List.of(message));
        doThrow(new RuntimeException("Simulated failure")).when(snsPublisherPort).publishMessage(event);

        messageUseCase.sendMessages();

        verify(snsPublisherPort).publishMessage(event);
        verify(sqsServicePort, never()).deleteMessage("receipt-handle-456");
    }
}