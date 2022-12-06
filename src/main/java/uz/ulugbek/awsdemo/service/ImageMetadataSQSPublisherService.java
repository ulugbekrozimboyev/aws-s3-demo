package uz.ulugbek.awsdemo.service;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.core.QueueMessageChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import uz.ulugbek.awsdemo.entity.ImageMetadata;

@Service
@Slf4j
public class ImageMetadataSQSPublisherService {

    public static final String QUEUE_NAME = "https://sqs.us-east-1.amazonaws.com/608123326175/module-8-uploads-notification-queue";
    private final AmazonSQSAsync amazonSQSAsync;
    private final ObjectMapper objectMapper;

    public ImageMetadataSQSPublisherService(AmazonSQSAsync amazonSQSAsync, ObjectMapper objectMapper) {
        this.amazonSQSAsync = amazonSQSAsync;
        this.objectMapper = objectMapper;
    }

    public boolean publish(ImageMetadata imageMetadata) {

        try {
            MessageChannel messageChannel
                    = new QueueMessageChannel(amazonSQSAsync, QUEUE_NAME);

            String message = objectMapper.writeValueAsString(imageMetadata);


            Message<String> msg = MessageBuilder.withPayload(message)
                    .setHeader("sender", "app1")
                    .setHeaderIfAbsent("country", "AE")
                    .build();

            long waitTimeoutMillis = 5000;
            boolean sentStatus = messageChannel.send(msg, waitTimeoutMillis);
            log.info("message sent");
            return sentStatus;

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

}
