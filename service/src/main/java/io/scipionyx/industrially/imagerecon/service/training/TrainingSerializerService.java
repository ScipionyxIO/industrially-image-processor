package io.scipionyx.industrially.imagerecon.service.training;

import io.scipionyx.industrially.imagerecon.model.Training;
import org.deeplearning4j.nn.api.Model;

import java.io.IOException;

public interface TrainingSerializerService {

    Model load(Training training) throws IOException;

    void save(Model model, Training training) throws IOException;

    void delete(Training training) throws IOException;

}
