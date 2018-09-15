package io.scipionyx.industrially.imagerecon.service.training;

import io.scipionyx.industrially.imagerecon.model.Training;
import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.nn.api.Model;
import org.deeplearning4j.nn.conf.CacheMode;
import org.deeplearning4j.nn.conf.WorkspaceMode;
import org.deeplearning4j.nn.conf.inputs.InvalidInputTypeException;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.optimize.listeners.PerformanceListener;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.stats.StatsListener;
import org.deeplearning4j.ui.storage.InMemoryStatsStorage;
import org.deeplearning4j.zoo.ZooModel;
import org.deeplearning4j.zoo.model.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
class ZooModelFactoryService {

    private UIServer uiServer;

    @PostConstruct
    void init() {
        uiServer = UIServer.getInstance();
    }

    void attach(Model model) {
        StatsStorage statsStorage = new InMemoryStatsStorage();
        uiServer.attach(statsStorage);
        model.setListeners(new StatsListener(statsStorage),
                new ScoreIterationListener(1),
                new PerformanceListener(1, true));
    }

    ZooModel instance(Training training, Context context) {
        ZooModel model;
        switch (training.getModelType()) {
            case LeNet:
                model = LeNet.builder().
                        numClasses(context.getLabelsCount()).
                        seed(training.getSeed()).
                        cacheMode(CacheMode.DEVICE).
                        workspaceMode(WorkspaceMode.ENABLED).
                        cudnnAlgoMode(ConvolutionLayer.AlgoMode.PREFER_FASTEST).
                        build();
                break;
            case AlexNet:
                model = AlexNet.builder().
                        numClasses(context.getLabelsCount()).
                        seed(training.getSeed()).
                        cacheMode(CacheMode.DEVICE).
                        workspaceMode(WorkspaceMode.ENABLED).
                        cudnnAlgoMode(ConvolutionLayer.AlgoMode.PREFER_FASTEST).
                        build();
                break;
            case VGG19:
                model = VGG19.builder().
                        numClasses(context.getLabelsCount()).
                        seed(training.getSeed()).
                        build();
                break;
            case Darknet19:
                model = Darknet19.builder().
                        numClasses(context.getLabelsCount()).
                        seed(training.getSeed()).
                        build();
                break;
            case TinyYOLO:
                model = TinyYOLO.builder().
                        numClasses(context.getLabelsCount()).
                        seed(training.getSeed()).
                        build();
                break;
            case YOLO2:
                model = YOLO2.builder().
                        numClasses(context.getLabelsCount()).
                        seed(training.getSeed()).
                        build();
                break;
            default:
                throw new InvalidInputTypeException("Incorrect model provided.");
        }
        return model;
    }

}
