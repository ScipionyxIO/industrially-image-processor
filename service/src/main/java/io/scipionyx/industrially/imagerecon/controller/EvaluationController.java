package io.scipionyx.industrially.imagerecon.controller;

import io.scipionyx.industrially.imagerecon.model.Training;
import io.scipionyx.industrially.imagerecon.service.predict.PredictService;
import io.scipionyx.industrially.imagerecon.service.training.TrainingSerializerService;
import io.scipionyx.industrially.imagerecon.service.training.TrainingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.deeplearning4j.nn.api.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.IOException;

@RestController
@Slf4j
public class EvaluationController {

    private final PredictService predict;

    private final TrainingService trainingService;

    private final TrainingSerializerService serializerService;

    @Autowired
    public EvaluationController(TrainingService trainingService,
                                PredictService predict,
                                TrainingSerializerService serializerService) {
        this.trainingService = trainingService;
        this.predict = predict;
        this.serializerService = serializerService;
    }

    @PostMapping("/api/v1/evaluate/{trainingId}")
    public Flux<String> evaluate(@PathVariable Long trainingId,
                                 @RequestParam(name = "image") MultipartFile image) throws IOException {
        // write temp file
        File file = File.createTempFile("tmp", "tmp");
        FileUtils.copyInputStreamToFile(image.getInputStream(), file);
        Training training = trainingService.get(trainingId);
        Model model = serializerService.load(training);
        int[] result = predict.predict(model, image.getInputStream());
        return Flux.just("This is a " + training.getLabels().get(result[0]));
    }

}
