package io.scipionyx.industrially.imagerecon.service.observation;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import io.scipionyx.industrially.imagerecon.configuration.TrainingConfiguration;
import io.scipionyx.industrially.imagerecon.model.Modeling;
import io.scipionyx.industrially.imagerecon.model.Observation;
import io.scipionyx.industrially.imagerecon.repository.ObservationRepository;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;

@Component
@ConditionalOnProperty(name = "scipionyx.useAws", havingValue = "true")
public class ObservationS3Service implements ObservationService {

    private final TrainingConfiguration configuration;

    private final ObservationRepository repository;

    private final AmazonS3 amazonS3;

    @Autowired
    public ObservationS3Service(TrainingConfiguration configuration,
                                ObservationRepository repository,
                                AmazonS3 amazonS3) {
        this.configuration = configuration;
        this.repository = repository;
        this.amazonS3 = amazonS3;
    }

    @Override
    public Observation save(Observation observation, InputStream inputStream) {
        // save file to S3

        amazonS3.putObject(new PutObjectRequest(
                calculateBucketName(observation),
                observation.getModeling().getId().toString(),
                inputStream,
                new ObjectMetadata()));
        // save observation
        return repository.save(observation);
    }

    @Override
    public File load(Modeling modeling) {
        return null;
    }

    @Override
    public void deleteAll(Modeling modeling) {
    }

    @Override
    public Try<Observation> delete(Observation observation) {
        return Try.of(() -> observation);
    }

    private String calculateBucketName(Observation observation) {
        return configuration.
                getObservationBucketName().
                concat("/").
                concat("modeling-").concat(observation.getModeling().getId().toString()).
                concat("/").
                concat("label-").concat(observation.getLabel().toLowerCase());
    }

}
