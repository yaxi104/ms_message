package com.hexagonal.ms_message.infrastructure.output.sms;

import com.hexagonal.ms_message.domain.model.OrderReadyEvent;
import com.hexagonal.ms_message.domain.spi.ISmsSendPort;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsSenderAdapter implements ISmsSendPort {

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.senderNumber}")
    private String senderNumber;

    public void publishMessage(OrderReadyEvent orderReadyEvent) {
        Twilio.init(accountSid, authToken);

        Message message = Message.creator(
                new PhoneNumber(orderReadyEvent.getPhoneNumber()),
                new PhoneNumber(senderNumber),
                orderReadyEvent.getMessage()
        ).create();

        log.info("Mensaje enviado con SID: " + message.getSid());
    }
}
