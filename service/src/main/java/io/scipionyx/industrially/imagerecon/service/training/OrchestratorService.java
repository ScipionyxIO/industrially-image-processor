package io.scipionyx.industrially.imagerecon.service.training;

import io.scipionyx.industrially.imagerecon.model.Training;
import io.scipionyx.industrially.imagerecon.repository.TrainingRepository;
import lombok.extern.slf4j.Slf4j;
import org.deeplearning4j.nn.api.Model;
import org.deeplearning4j.zoo.ZooModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

import java.io.File;
import java.io.IOException;

@Component
@Slf4j
public class OrchestratorService {

    private final TrainingRepository repository;

    private final DataPreparationService dataPreparationService;

    private final ZooModelFactoryService factoryService;

    private final TrainingService trainingService;

    private final TrainingSerializerService serializerService;

    @Autowired
    private OrchestratorService(TrainingRepository repository, DataPreparationService dataPreparationService,
                                ZooModelFactoryService factoryService, TrainingService trainingService,
                                TrainingSerializerService serializerService) {
        this.repository = repository;
        this.dataPreparationService = dataPreparationService;
        this.factoryService = factoryService;
        this.trainingService = trainingService;
        this.serializerService = serializerService;
    }

    @SuppressWarnings("deprecation")
    public void train(Training training, File folder) throws IOException {
        Assert.isTrue(folder.isDirectory(), "The provided information must be a folder");
        StopWatch stopWatch = new StopWatch("Full Training");
        log.debug("Training - Start Prepare");
        stopWatch.start("Prepare");
        Context context = dataPreparationService.prepare(
                training,
                folder);
        stopWatch.stop();
        log.debug("Training - Start Network");
        stopWatch.start("Model");
        ZooModel zooModel = factoryService.instance(training, context);
        Model model = zooModel.init();
        model.conf().setMiniBatch(true);
        factoryService.attach(model);
        stopWatch.stop();
        log.debug("Training - Start Training");
        stopWatch.start("Training");
        context.setShapes(zooModel.
                metaData().
                getInputShape()[0]);
        trainingService.train(training,
                context,
                model);
        stopWatch.stop();
        log.debug("Training - Save");
        stopWatch.start("Save");
        repository.save(training);
        serializerService.save(model, training);
    }

    public void delete(Training training) throws IOException {
        serializerService.delete(training);
        repository.delete(training);
    }
}
