package com.hexagonal.ms_message.infrastructure.output.sns;

//import com.hexagonal.ms_message.domain.model.OrderReadyEvent;
//import com.hexagonal.ms_message.domain.spi.ISnsPublisherPort;
//import com.hexagonal.ms_message.infrastructure.configuration.aws.AwsProperties;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import software.amazon.awssdk.services.sns.SnsClient;
//import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
//import software.amazon.awssdk.services.sns.model.PublishRequest;
//import software.amazon.awssdk.services.sns.model.PublishResponse;
//
//import java.util.Map;
//
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class SnsAdapter implements ISnsPublisherPort {
//
//    private final SnsClient snsClient;
//    private final AwsProperties awsProperties;
//    private static final String STRING_TYPE = "String";
//
//    public void publishMessage(OrderReadyEvent orderReadyEvent) {
//        try {
//            log.info("Publicando mensaje SMS para número: {}", orderReadyEvent.getPhoneNumber());
//
//            PublishRequest request = PublishRequest.builder()
//                    .phoneNumber(orderReadyEvent.getPhoneNumber())
//                    .message(orderReadyEvent.getMessage())
//                    .messageAttributes(Map.of(
//                            "AWS.SNS.SMS.SMSType", MessageAttributeValue.builder()
//                                    .stringValue("Transactional")
//                                    .dataType(STRING_TYPE)
//                                    .build(),
//                            "AWS.SNS.SMS.SenderID", MessageAttributeValue.builder()
//                                    .stringValue("Plazoleta")
//                                    .dataType(STRING_TYPE)
//                                    .build()
//                    ))
//                    .build();
//
//            PublishResponse response = snsClient.publish(request);
//            log.info("Mensaje publicado exitosamente con ID: {}", response.messageId());
//        } catch (Exception e) {
//            log.error("Error al publicar mensaje SNS: {}", e.getMessage(), e);
//            throw e;
//        }
//    }
//}
