package uz.ulugbek.awsdemo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Service;
import uz.ulugbek.awsdemo.entity.ImageMetadata;

@Service
@Slf4j
public class AwsSqsListener {

    private final ObjectMapper objectMapper;
    private final SubscriptionService subscriptionService;

    public AwsSqsListener(ObjectMapper objectMapper, SubscriptionService subscriptionService) {
        this.objectMapper = objectMapper;
        this.subscriptionService = subscriptionService;
    }

    @SqsListener(value = "module-8-uploads-notification-queue", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void receiveMessage(String message) {
        log.info(message);
        try {
            ImageMetadata imageMetadata = objectMapper.readValue(message, ImageMetadata.class);
            subscriptionService.sendEmail(imageMetadata);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
