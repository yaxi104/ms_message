package com.hexagonal.ms_message.infrastructure.configuration.aws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class AwsConfig {

    @Bean
    public SqsClient sqsClient(AwsProperties awsProperties) {
        return SqsClient.builder()
                .region(Region.of(awsProperties.getRegion()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(
                                        awsProperties.getAccessKey(),
                                        awsProperties.getSecretKey()
                                )
                        )
                )
                .build();
    }

//    @Bean
//    public SnsClient snsClient(AwsProperties awsProperties) {
//        return SnsClient.builder()
//                .region(Region.of(awsProperties.getRegion()))
//                .credentialsProvider(
//                        StaticCredentialsProvider.create(
//                                AwsBasicCredentials.create(
//                                        awsProperties.getAccessKey(),
//                                        awsProperties.getSecretKey()
//                                )
//                        )
//                )
//                .build();
//    }
}