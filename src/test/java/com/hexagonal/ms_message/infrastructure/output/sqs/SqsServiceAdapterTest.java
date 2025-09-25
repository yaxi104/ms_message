package com.hexagonal.ms_message.infrastructure.output.sqs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexagonal.ms_message.domain.model.OrderReadyEvent;
import com.hexagonal.ms_message.domain.model.SqsMessage;
import com.hexagonal.ms_message.infrastructure.configuration.aws.AwsProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SqsServiceAdapterTest {

    @Mock
    private SqsClient sqsClient;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AwsProperties awsProperties;

    @Mock
    private AwsProperties.Sqs sqsProperties;

    @InjectMocks
    private SqsServiceAdapter sqsServiceAdapter;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        when(awsProperties.getSqs()).thenReturn(sqsProperties);
        when(sqsProperties.getQueueUrl()).thenReturn("https://sqs.test.queue");
    }

    @Test
    void receiveMessagesTest() throws JsonProcessingException {
        String messageBody = "{\"phoneNumber\":\"+1234567890\",\"message\":\"Pedido listo\"}";
        String receiptHandle = "abc123";

        Message awsMessage = Message.builder()
                .body(messageBody)
                .receiptHandle(receiptHandle)
                .build();

        ReceiveMessageResponse response = ReceiveMessageResponse.builder()
                .messages(awsMessage)
                .build();

        OrderReadyEvent event = new OrderReadyEvent("+1234567890", "Pedido listo");

        when(sqsClient.receiveMessage(any(ReceiveMessageRequest.class))).thenReturn(response);
        when(objectMapper.readValue(messageBody, OrderReadyEvent.class)).thenReturn(event);

        List<SqsMessage<OrderReadyEvent>> result = sqsServiceAdapter.receiveMessages();

        assertEquals(1, result.size());
        assertEquals(receiptHandle, result.get(0).getReceiptHandle());
    }

    @Test
    void receiveMessagesNullMessageTest() throws JsonProcessingException {
        String badJson = "invalid-json";
        Message awsMessage = Message.builder()
                .body(badJson)
                .receiptHandle("badHandle")
                .build();

        ReceiveMessageResponse response = ReceiveMessageResponse.builder()
                .messages(awsMessage)
                .build();

        when(sqsClient.receiveMessage(any(ReceiveMessageRequest.class))).thenReturn(response);
        when(objectMapper.readValue(badJson, OrderReadyEvent.class)).thenThrow(JsonProcessingException.class);

        List<SqsMessage<OrderReadyEvent>> result = sqsServiceAdapter.receiveMessages();

        assertEquals(1, result.size());
        assertNull(result.get(0));
    }

    @Test
    void deleteMessage_withCaptor() {
        String receiptHandle = "delete123";

        sqsServiceAdapter.deleteMessage(receiptHandle);

        ArgumentCaptor<DeleteMessageRequest> captor = ArgumentCaptor.forClass(DeleteMessageRequest.class);
        verify(sqsClient).deleteMessage(captor.capture());

        DeleteMessageRequest actual = captor.getValue();
        assertEquals("https://sqs.test.queue", actual.queueUrl());
        assertEquals(receiptHandle, actual.receiptHandle());
    }
}