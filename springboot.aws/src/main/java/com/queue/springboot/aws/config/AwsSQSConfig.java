package com.queue.springboot.aws.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AwsSQSConfig {

    private AmazonSQS amazonSQS;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.end-point.uri}")
    private String endpoint;

    @PostConstruct
    public void init() {
        this.amazonSQS = AmazonSQSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .withRegion(Regions.fromName(region))
                .build();
    }

    // Método que puedes llamar para consumir mensajes
    public List<Message> consumeMessagesDirectly() {
        AmazonSQS sqs = this.amazonSQS;
        String queueUrl = this.amazonSQS.getQueueUrl("java-queue").getQueueUrl();
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(endpoint).withWaitTimeSeconds(10);
        List<com.amazonaws.services.sqs.model.Message> messages = this.amazonSQS.receiveMessage(receiveMessageRequest).getMessages();
        for (com.amazonaws.services.sqs.model.Message message : messages) {
            // Procesar mensaje
            System.out.println("Received message: " + message.getBody());
            // Al final, borrar mensaje
       //     String messageReceiptHandle = message.getReceiptHandle();
         //   amazonSQS.deleteMessage(new DeleteMessageRequest(queueUrl, messageReceiptHandle));
        }
        return messages;
    }
}

