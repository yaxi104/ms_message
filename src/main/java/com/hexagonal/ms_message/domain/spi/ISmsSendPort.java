package com.hexagonal.ms_message.domain.spi;

import com.hexagonal.ms_message.domain.model.OrderReadyEvent;

public interface ISmsSendPort {
    void publishMessage(OrderReadyEvent orderReadyEvent);
}
