package io.scipionyx.industrially.imagerecon;

import io.scipionyx.industrially.imagerecon.configuration.TrainingConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@EnableConfigurationProperties({TrainingConfiguration.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Configuration
    @Profile("local")
    @ComponentScan(lazyInit = true)
    static class LocalConfig {
    }

}