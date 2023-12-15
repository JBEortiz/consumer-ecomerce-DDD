package com.queue.springboot.aws;

import com.amazonaws.services.sqs.model.Message;
import com.queue.springboot.aws.config.AwsSQSConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@SpringBootApplication
public class Application {
	@Autowired
	private AwsSQSConfig awsSQSConfig;
	@Value("${cloud.aws.end-point.uri}")
	private String endpoint;

	@SqsListener("java-queue")
	public void loadMessageFromSQS(String message){
		awsSQSConfig.consumeMessagesDirectly();
		log.info("Consumer message: "+message);
	}

	@GetMapping("consume")
	public List<Message> retrieveMessageSQS(){
		return awsSQSConfig.consumeMessagesDirectly();
	}



	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
