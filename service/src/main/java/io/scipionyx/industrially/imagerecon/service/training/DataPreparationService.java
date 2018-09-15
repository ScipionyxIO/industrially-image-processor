package io.scipionyx.industrially.imagerecon.service.training;

import io.scipionyx.industrially.imagerecon.model.Training;
import lombok.extern.slf4j.Slf4j;
import org.datavec.api.io.filters.BalancedPathFilter;
import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.io.labels.PathLabelGenerator;
import org.datavec.api.split.FileSplit;
import org.datavec.api.split.InputSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.transform.ColorConversionTransform;
import org.datavec.image.transform.FlipImageTransform;
import org.datavec.image.transform.ImageTransform;
import org.datavec.image.transform.WarpImageTransform;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.primitives.Pair;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.toIntExact;
import static org.bytedeco.javacpp.opencv_imgproc.COLOR_BGR2YCrCb;

@Component
@Slf4j
class DataPreparationService {

    Context prepare(Training training, File mainPath) {

        log.debug("Main path: {}", mainPath.toString());

        PathLabelGenerator labelGenerator = new ParentPathLabelGenerator();
        Random random = new Random(training.getSeed());

        // File Split
        FileSplit fileSplit = new FileSplit(mainPath, NativeImageLoader.ALLOWED_FORMATS, random);
        int examples = toIntExact(fileSplit.length());
        int labelsCount = Objects.requireNonNull(fileSplit.getRootDir().listFiles(File::isDirectory)).length;
        List<String> labels = Arrays.
                stream(Objects.requireNonNull(fileSplit.getRootDir().listFiles(File::isDirectory))).
                map(File::getName).
                collect(Collectors.toList());
        int maxPathsPerLabel = 18;
        BalancedPathFilter pathFilter = new BalancedPathFilter(random, labelGenerator, examples, labelsCount, maxPathsPerLabel);

        //
        InputSplit[] inputSplit = fileSplit.sample(pathFilter, training.getSplitTrainTest(), 1 - training.getSplitTrainTest());
        InputSplit trainData = inputSplit[0];
        InputSplit testData = inputSplit[(inputSplit.length > 1) ? 1 : 0];

        //
        List<Pair<ImageTransform, Double>> pipeline = new ArrayList<>();
        for (int i = 0; i < training.getNumberOfFlipTransformations(); i++) {
            pipeline.add(
                    new Pair<>(
                            new FlipImageTransform(new Random(new Random(1000).nextInt())),
                            new Random(1).nextDouble()));
        }
        for (int i = 0; i < training.getNumberOfWarpTransformations(); i++) {
            pipeline.add(
                    new Pair<>(
                            new WarpImageTransform(new Random(new Random(1000).nextInt()), 42),
                            new Random(1).nextDouble()));
        }
        for (int i = 0; i < training.getNumberOfColorConversionTransformations(); i++) {
            pipeline.add(
                    new Pair<>(
                            new ColorConversionTransform(new Random(training.getSeed()), COLOR_BGR2YCrCb),
                            new Random(1).nextDouble()));
        }

        log.info("Number of Transforms: {}", pipeline.size());

        return new Context(examples,
                null,
                labelsCount,
                labels,
                trainData,
                testData,
                labelGenerator,
                new ImagePreProcessingScaler(0, 1),
                pipeline);

    }


}
