package io.scipionyx.industrially.imagerecon.configuration;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Lazy
@Configuration
@ConditionalOnProperty(name = "scipionyx.useAws", havingValue = "true")
class AmazonConfiguration {

    @Bean
    AmazonS3 amazonS3(AWSCredentialsProvider awsCredentialsProvider) {
        return AmazonS3ClientBuilder.
                standard().
                withCredentials(awsCredentialsProvider).
                withRegion(Regions.US_EAST_2).
                build();
    }
}
