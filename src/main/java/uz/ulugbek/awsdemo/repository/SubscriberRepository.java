package uz.ulugbek.awsdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.ulugbek.awsdemo.entity.Subscriber;

public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {
}
