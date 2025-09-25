package com.hexagonal.ms_message.infrastructure.output.sqs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexagonal.ms_message.domain.model.OrderReadyEvent;
import com.hexagonal.ms_message.domain.model.SqsMessage;
import com.hexagonal.ms_message.domain.spi.ISqsServicePort;
import com.hexagonal.ms_message.infrastructure.configuration.aws.AwsProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SqsServiceAdapter implements ISqsServicePort {

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;
    private final AwsProperties awsProperties;

    @Override
    public List<SqsMessage<OrderReadyEvent>> receiveMessages() {
        ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                .queueUrl(awsProperties.getSqs().getQueueUrl())
                .maxNumberOfMessages(10)
                .waitTimeSeconds(10)
                .build();

        ReceiveMessageResponse response = sqsClient.receiveMessage(request);
        return response.messages().stream()
                .map(msg -> {
                    try {
                        OrderReadyEvent event = objectMapper.readValue(msg.body(), OrderReadyEvent.class);
                        return new SqsMessage<>(event, msg.receiptHandle());
                    } catch (JsonProcessingException e) {
                        log.error("Error deserializando mensaje de SQS: {}", msg.body(), e);
                        return null;
                    }
                })
                .toList();
    }

    @Override
    public void deleteMessage(String receiptHandle) {
        DeleteMessageRequest deleteRequest = DeleteMessageRequest.builder()
                .queueUrl(awsProperties.getSqs().getQueueUrl())
                .receiptHandle(receiptHandle)
                .build();

        sqsClient.deleteMessage(deleteRequest);
    }
}