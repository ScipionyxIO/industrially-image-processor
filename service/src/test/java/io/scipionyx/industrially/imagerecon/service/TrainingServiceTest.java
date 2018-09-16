package io.scipionyx.industrially.imagerecon.service;

import io.scipionyx.industrially.imagerecon.Application;
import io.scipionyx.industrially.imagerecon.model.ModelType;
import io.scipionyx.industrially.imagerecon.model.Modeling;
import io.scipionyx.industrially.imagerecon.model.Observation;
import io.scipionyx.industrially.imagerecon.model.Training;
import io.scipionyx.industrially.imagerecon.repository.ModelingRepository;
import io.scipionyx.industrially.imagerecon.repository.TrainingRepository;
import io.scipionyx.industrially.imagerecon.service.observation.ObservationService;
import io.scipionyx.industrially.imagerecon.service.predict.PredictService;
import io.scipionyx.industrially.imagerecon.service.training.OrchestratorService;
import io.scipionyx.industrially.imagerecon.service.training.TrainingSerializerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@Slf4j
@Rollback
@Transactional
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class TrainingServiceTest {

    @Autowired
    private ModelingRepository modelingRepository;

    @Autowired
    private ObservationService observationService;

    @Autowired
    private OrchestratorService orchestratorService;

    @Autowired
    private PredictService predictService;

    @Autowired
    private TrainingSerializerService trainingSerializerService;

    @Autowired
    private TrainingRepository trainingRepository;

    //@Test
    public void test_01() throws IOException {
        StopWatch stopWatch = new StopWatch();
        Modeling modeling = modelingRepository.
                save(Modeling.builder().name("Modeling Name-2").description(
                        "Modeling Description").build());
        stopWatch.start("Add Observation Bear");
        observationService.save(new Observation(modeling,
                "application/zip",
                "bear.zip",
                "bear"), this.getClass().getResourceAsStream("/animals/bear.zip"));
        stopWatch.stop();
        stopWatch.start("Add Observation Deer");
        observationService.save(new Observation(modeling,
                "application/zip",
                "deer.zip",
                "deer"), this.getClass().getResourceAsStream("/animals/deer.zip"));
        stopWatch.stop();
        stopWatch.start("Add Observation Duck");
        observationService.save(new Observation(modeling,
                "application/zip",
                "duck.zip",
                "duck"), this.getClass().getResourceAsStream("/animals/duck.zip"));
        stopWatch.stop();
        stopWatch.start("Add Observation Turtle");
        observationService.save(new Observation(modeling,
                "application/zip",
                "turtle.zip",
                "turtle"), this.getClass().getResourceAsStream("/animals/turtle.zip"));
        stopWatch.stop();
        stopWatch.start("Preparing Observation's Training Folder");
        File folder = observationService.load(modeling);
        stopWatch.stop();
        Training training = Training.
                builder().
                modeling(modeling).
                modelType(ModelType.LE_NET).
                seed(40).
                epochs(20).batchSize(20).
                splitTrainTest(.08d).
                useTransformations(true).
                shuffleTransformations(true).
                numberOfFlipTransformations(1).numberOfWarpTransformations(1).build();
        orchestratorService.train(training, folder);
        stopWatch.start("Cleaning up observations");
        observationService.deleteAll(modeling);
        orchestratorService.delete(training);
        stopWatch.stop();
    }

    @Test
    public void test_02() {
        Optional<Training> training = trainingRepository.findById(222L);
        Assert.isTrue(training.isPresent(), "Training must be found");
        //predictService.predict()
    }

}
