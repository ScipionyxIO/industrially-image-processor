package io.scipionyx.industrially.imagerecon.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "scipionyx.industrially.imagerecon.modeling")
@Data
@NoArgsConstructor
public class TrainingConfiguration {

    private String modelFolder;

    private Resource observationFolder;

    private String awsBucketName;

    private String observationBucketName;

    private Resource trainingFolder;

}