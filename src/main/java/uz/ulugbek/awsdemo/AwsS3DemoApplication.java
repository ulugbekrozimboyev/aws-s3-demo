package uz.ulugbek.awsdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableSqs
public class AwsS3DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AwsS3DemoApplication.class, args);
    }

}
