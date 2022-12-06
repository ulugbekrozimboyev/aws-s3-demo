package uz.ulugbek.awsdemo.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.ulugbek.awsdemo.entity.ImageMetadata;

import java.util.Optional;

@Service
@Slf4j
public class SubscriptionService {

    public static final String TOPIC_ARN = "arn:aws:sns:us-east-1:608123326175:module-8-uploads-notification-topic";
    private final AmazonSNS amazonSNS;

    public SubscriptionService(AmazonSNS amazonSNS) {
        this.amazonSNS = amazonSNS;
    }

    public void subscribe(String email) {
        SubscribeRequest subscribeRequest = new SubscribeRequest(TOPIC_ARN,
                                                                "email",
                                                                email);
        amazonSNS.subscribe(subscribeRequest);
    }

    public void unsubscribe(String email) {
        ListSubscriptionsResult listSubscriptions = amazonSNS.listSubscriptions();
        Optional<Subscription> optionalSubscription = listSubscriptions.getSubscriptions().stream().filter(item -> email.equals(item.getEndpoint())).findFirst();

        optionalSubscription.ifPresent(subscription ->
                amazonSNS.unsubscribe(subscription.getSubscriptionArn())
        );
    }


    public void sendEmail(ImageMetadata imageMetadata) {
        String link = String.format("http://%s:%d/api/file/download/%s",
                "ec2-52-91-2-97.compute-1.amazonaws.com",
                8080,
                imageMetadata.getId()
        );
        String message = String.format("New Image was added. Parameters like this:\nname= %s\ncontent type: %s\nfile size: %d\n download link: %s",
                imageMetadata.getOriginalName(),
                imageMetadata.getContentType(),
                imageMetadata.getFileSize(),
                link
        );
        sendEmail(message);
    }

    public void sendEmail(String message) {

        PublishRequest publishRequest = new PublishRequest(TOPIC_ARN, message, "Email test");
        PublishResult result = amazonSNS.publish(publishRequest);
        log.info(result.getMessageId());

    }
}
