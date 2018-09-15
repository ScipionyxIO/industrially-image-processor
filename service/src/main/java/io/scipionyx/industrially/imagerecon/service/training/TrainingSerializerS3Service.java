package io.scipionyx.industrially.imagerecon.service.training;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import io.scipionyx.industrially.imagerecon.configuration.TrainingConfiguration;
import io.scipionyx.industrially.imagerecon.model.Training;
import lombok.extern.slf4j.Slf4j;
import org.deeplearning4j.nn.api.Model;
import org.deeplearning4j.util.ModelSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;

@Component
@Slf4j
@ConditionalOnProperty(name = "scipionyx.useAws", havingValue = "true")
class TrainingSerializerS3Service implements TrainingSerializerService {

    private final TrainingConfiguration configuration;

    private final AmazonS3 amazonS3;

    @Autowired
    private TrainingSerializerS3Service(TrainingConfiguration configuration, AmazonS3 amazonS3) {
        this.configuration = configuration;
        this.amazonS3 = amazonS3;
        Assert.notNull(configuration.getAwsBucketName(),
                "Property scipionyx.industrially.imagerecon.modeling.awsBucketName is required");
    }

    @Override
    public void save(Model model, Training training) throws IOException {
        File temp = File.createTempFile("training.", "-serialization");
        ModelSerializer.writeModel(model, temp, true);
        amazonS3.putObject(new PutObjectRequest(configuration.getAwsBucketName(),
                training.getId().toString(),
                temp));
        temp.deleteOnExit();
    }

    @Override
    @Cacheable(value = "training", key = "#training.id")
    public Model load(Training training) throws IOException {
        return ModelSerializer.
                restoreMultiLayerNetwork(
                        amazonS3.getObject(
                                configuration.getAwsBucketName(),
                                training.getId().toString()).getObjectContent());
    }

    @Override
    public void delete(Training training) {
        amazonS3.deleteObject(configuration.getAwsBucketName(), training.getId().toString());
    }

}
