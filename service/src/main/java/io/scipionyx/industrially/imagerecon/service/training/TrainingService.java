package io.scipionyx.industrially.imagerecon.service.training;

import io.scipionyx.industrially.imagerecon.model.Training;
import io.scipionyx.industrially.imagerecon.repository.TrainingRepository;
import lombok.extern.slf4j.Slf4j;
import org.datavec.image.recordreader.ImageRecordReader;
import org.datavec.image.transform.ResizeImageTransform;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.Model;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.primitives.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Random;

@Component
@Slf4j
public class TrainingService {

    private final TrainingRepository repository;

    @Autowired
    private TrainingService(TrainingRepository repository) {
        this.repository = repository;
    }

    public Training get(Long id) {
        return repository.findById(id).orElse(null);
    }

    void train(
            Training training,
            Context trainingContext,
            Model model) throws IOException {
        fit(model, training,
                createDataSetIterator(
                        training,
                        trainingContext));
        Evaluation eval;
        if (model instanceof ComputationGraph) {
            eval = ((ComputationGraph) model).
                    evaluate(
                            createDataSetIterator(training, trainingContext));
        } else {
            eval = ((MultiLayerNetwork) model).
                    evaluate(
                            createDataSetIterator(training, trainingContext));
        }
        training.setLabels(eval.getLabelsList());
        log.info(eval.stats(true));
    }

    private ImageRecordReader createRecordReader(Context context) throws IOException {
        ImageRecordReader recordReader = new ImageRecordReader(
                context.getShapes()[1],
                context.getShapes()[2],
                context.getShapes()[0],
                context.getLabelGenerator());
        recordReader.initialize(context.getTrainData(), null);
        return recordReader;
    }

    private DataSetIterator createDataSetIterator(Training training,
                                                  Context trainingContext) throws IOException {
        trainingContext.getPipeline().add(
                new Pair<>(
                        new ResizeImageTransform(trainingContext.getShapes()[1], trainingContext.getShapes()[2]),
                        new Random(1).nextDouble()));
        DataSetIterator dataSetIterator = new RecordReaderDataSetIterator(
                createRecordReader(trainingContext
                ),
                training.getBatchSize());
        Assert.notNull(trainingContext.getDataNormalizer(), "Data Normalizer is not set");
        trainingContext.getDataNormalizer().fit(dataSetIterator);
        dataSetIterator.setPreProcessor(trainingContext.getDataNormalizer());
        return dataSetIterator;
    }

    private void fit(Model model,
                     Training training,
                     DataSetIterator dataSetIterator) {
        if (model instanceof ComputationGraph) {
            ((ComputationGraph) model).fit(dataSetIterator, training.getEpochs());
        } else {
            ((MultiLayerNetwork) model).fit(dataSetIterator, training.getEpochs());
        }
    }

}
