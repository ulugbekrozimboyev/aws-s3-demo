package uz.ulugbek.awsdemo.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.ulugbek.awsdemo.entity.Subscriber;
import uz.ulugbek.awsdemo.repository.SubscriberRepository;

import java.util.Optional;

@Service
@Slf4j
public class SubscriptionService {

    public static final String TOPIC_ARN = "arn:aws:sns:us-east-1:608123326175:module-8-uploads-notification-topic";
    private final AmazonSNS amazonSNS;
    private final SubscriberRepository subscriberRepository;

    public SubscriptionService(AmazonSNS amazonSNS, SubscriberRepository subscriberRepository) {
        this.amazonSNS = amazonSNS;
        this.subscriberRepository = subscriberRepository;
    }

    public void subscribe(String email) {
        SubscribeRequest subscribeRequest = new SubscribeRequest(TOPIC_ARN,
                                                                "email",
                                                                email);
        SubscribeResult subscribeResult = amazonSNS.subscribe(subscribeRequest);
        subscriberRepository.save(
                Subscriber.builder()
                        .arn(subscribeResult.getSubscriptionArn())
                        .email(email)
                        .build()
        );
    }

    public void unsubscribe(String email) {
        ListSubscriptionsResult listSubscriptions = amazonSNS.listSubscriptions();
        Optional<Subscription> optionalSubscription = listSubscriptions.getSubscriptions().stream().filter(item -> email.equals(item.getEndpoint())).findFirst();

        if(optionalSubscription.isPresent()) {
            amazonSNS.unsubscribe(optionalSubscription.get().getSubscriptionArn());
        }
    }


    public void sendEmail(String message) {

        PublishRequest publishRequest = new PublishRequest(TOPIC_ARN, message, "Email test");
        PublishResult result = amazonSNS.publish(publishRequest);
        log.info(result.getMessageId());

    }
}