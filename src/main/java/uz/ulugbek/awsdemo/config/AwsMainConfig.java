package uz.ulugbek.awsdemo.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class AwsMainConfig {

    @Value("${aws.access-key}")
    private String accessKey;
    @Value("${aws.secret-key}")
    private String secretKey;
    @Value("${aws.region-name}")
    private String regionName;


    public AWSCredentials credentials() {
        return new BasicAWSCredentials(accessKey, secretKey);
    }

}
