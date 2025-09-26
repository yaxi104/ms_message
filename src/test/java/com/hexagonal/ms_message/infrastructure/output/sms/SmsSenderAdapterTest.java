package com.hexagonal.ms_message.infrastructure.output.sms;

import com.hexagonal.ms_message.domain.model.OrderReadyEvent;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SmsSenderAdapterTest {

    @InjectMocks
    private SmsSenderAdapter smsSenderAdapter;

    @Mock
    private MessageCreator messageCreator;

    @Mock
    private Message message;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(smsSenderAdapter, "accountSid", "testSid");
        ReflectionTestUtils.setField(smsSenderAdapter, "authToken", "testToken");
        ReflectionTestUtils.setField(smsSenderAdapter, "senderNumber", "+1234567890");
    }

    @Test
    void testPublishMessageSuccessTest() {
        OrderReadyEvent event = new OrderReadyEvent("+0987654321", "Tu pedido está listo");

        try (MockedStatic<Twilio> twilioMock = Mockito.mockStatic(Twilio.class);
             MockedStatic<Message> messageStaticMock = Mockito.mockStatic(Message.class)) {

            messageStaticMock.when(() -> Message.creator(
                    new PhoneNumber(event.getPhoneNumber()),
                    new PhoneNumber("+1234567890"),
                    event.getMessage()
            )).thenReturn(messageCreator);

            when(messageCreator.create()).thenReturn(message);
            when(message.getSid()).thenReturn("SM123456789");

            smsSenderAdapter.publishMessage(event);

            verify(messageCreator).create();
            verify(message).getSid();
        }
    }
}