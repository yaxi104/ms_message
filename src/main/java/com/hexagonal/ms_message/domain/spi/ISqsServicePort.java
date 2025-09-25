package com.hexagonal.ms_message.domain.spi;

import com.hexagonal.ms_message.domain.model.OrderReadyEvent;
import com.hexagonal.ms_message.domain.model.SqsMessage;

import java.util.List;

public interface ISqsServicePort {

    List<SqsMessage<OrderReadyEvent>> receiveMessages();

    void deleteMessage(String receiptHandle);
}
