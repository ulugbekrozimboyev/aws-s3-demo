package uz.ulugbek.awsdemo.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.ulugbek.awsdemo.service.SubscriptionService;

@RestController
@RequestMapping("/api/subscription")
@Slf4j
@AllArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/subscribe/{email}")
    public ResponseEntity<Boolean> subscribe(@PathVariable String email) {
        log.debug("Send subscription");
        subscriptionService.subscribe(email);
        return ResponseEntity.ok().body(true);
    }

    @GetMapping("/unsubscribe/{email}")
    public ResponseEntity<Boolean> unsubscribe(@PathVariable String email) {
        log.debug("Send subscription");
        subscriptionService.unsubscribe(email);
        return ResponseEntity.ok().body(true);
    }

}
