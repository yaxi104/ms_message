package com.hexagonal.ms_message.domain.usecase;

import com.hexagonal.ms_message.domain.api.IMessageServicePort;
import com.hexagonal.ms_message.domain.model.OrderReadyEvent;
import com.hexagonal.ms_message.domain.model.SqsMessage;
import com.hexagonal.ms_message.domain.spi.ISmsSendPort;
import com.hexagonal.ms_message.domain.spi.ISqsServicePort;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MessageUseCase implements IMessageServicePort {

    private final ISqsServicePort sqsServicePort;
    private final ISmsSendPort snsPublisherPort;

    public MessageUseCase(ISqsServicePort sqsServicePort, ISmsSendPort snsPublisherPort) {
        this.sqsServicePort = sqsServicePort;
        this.snsPublisherPort = snsPublisherPort;
    }

    @Override
    public void sendMessages() {
        List<SqsMessage<OrderReadyEvent>> orderReadyEvents = sqsServicePort.receiveMessages();
        if (orderReadyEvents != null && !orderReadyEvents.isEmpty()) {
            orderReadyEvents.forEach(this::publishMessage);
        } else {
            log.info("No SQS messages received to process.");
        }
    }

    private void publishMessage(SqsMessage<OrderReadyEvent> event) {
        try {
            snsPublisherPort.publishMessage(event.getBody());
            sqsServicePort.deleteMessage(event.getReceiptHandle());
        } catch (Exception e) {
            log.error("Fail publish message {}", event.getReceiptHandle());
        }
    }
}
