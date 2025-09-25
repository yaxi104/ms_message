package com.hexagonal.ms_message.application.handler.impl;

import com.hexagonal.ms_message.application.handler.IMessageHandler;
import com.hexagonal.ms_message.domain.api.IMessageServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageHandler implements IMessageHandler {

    private final IMessageServicePort messageServicePort;
    @Override
    public void sendMessages() {
        messageServicePort.sendMessages();
    }
}
