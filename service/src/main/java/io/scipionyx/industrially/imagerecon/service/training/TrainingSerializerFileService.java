package io.scipionyx.industrially.imagerecon.service.training;

import io.scipionyx.industrially.imagerecon.configuration.TrainingConfiguration;
import io.scipionyx.industrially.imagerecon.model.Training;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.deeplearning4j.nn.api.Model;
import org.deeplearning4j.util.ModelSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Component
@ConditionalOnProperty(name = "scipionyx.useAws", havingValue = "false")
@Slf4j
class TrainingSerializerFileService implements TrainingSerializerService {

    private final TrainingConfiguration configuration;

    @Autowired
    public TrainingSerializerFileService(TrainingConfiguration configuration) {
        this.configuration = configuration;
    }

    @PostConstruct
    public void init() {
        Assert.notNull(configuration.getModelFolder(), "THe Model Folder configuration is required.");
    }

    @Cacheable(value = "model", key = "#modeling.id")
    public Model load(Training training) throws IOException {
        log.info("Loading Training: {}", training.getId());
        return ModelSerializer.restoreMultiLayerNetwork(
                FilenameUtils.concat(configuration.getModelFolder(),
                        training.getId() + ".model"));
    }

    @Override
    public void save(Model model, Training training) throws IOException {
        ModelSerializer.writeModel(model,
                new File(FilenameUtils.concat(configuration.getModelFolder(), training.getId() + ".model")),
                true);
    }

    public void delete(Training training) throws IOException {
        FileUtils.forceDelete(new File(FilenameUtils.
                concat(configuration.getModelFolder(), training.getId() + ".model")));
    }
}
