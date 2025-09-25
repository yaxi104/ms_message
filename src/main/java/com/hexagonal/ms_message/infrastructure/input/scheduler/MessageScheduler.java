package com.hexagonal.ms_message.infrastructure.input.scheduler;

import com.hexagonal.ms_message.application.handler.IMessageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageScheduler {

    private final IMessageHandler messageHandler;

    @Scheduled(fixedDelayString = "${app.sqs.polling-interval-ms:5000}")
    public void pollQueue() {
        messageHandler.sendMessages();
    }

}
