package com.hexagonal.ms_message.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexagonal.ms_message.domain.api.IMessageServicePort;
import com.hexagonal.ms_message.domain.spi.ISmsSendPort;
import com.hexagonal.ms_message.domain.spi.ISqsServicePort;
import com.hexagonal.ms_message.domain.usecase.MessageUseCase;
import com.hexagonal.ms_message.infrastructure.configuration.aws.AwsProperties;
import com.hexagonal.ms_message.infrastructure.output.sns.SmsSenderAdapter;
import com.hexagonal.ms_message.infrastructure.output.sqs.SqsServiceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {
    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;
    private final AwsProperties awsProperties;

    @Bean
    public ISqsServicePort sqsServicePort() {
        return new SqsServiceAdapter(sqsClient, objectMapper, awsProperties);
    }

    @Bean
    public ISmsSendPort snsPublisherPort() {
        return new SmsSenderAdapter();
    }


    @Bean
    public IMessageServicePort messageServicePort() {
        return new MessageUseCase(sqsServicePort(), snsPublisherPort());
    }

}
