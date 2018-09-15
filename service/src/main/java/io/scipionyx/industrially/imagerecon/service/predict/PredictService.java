package io.scipionyx.industrially.imagerecon.service.predict;

import lombok.extern.slf4j.Slf4j;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.api.Model;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
@Slf4j
public class PredictService {

    public int[] predict(Model model,
                         InputStream inputStream) throws IOException {
        INDArray indArray = new NativeImageLoader(
                224,
                224,
                3).asMatrix(inputStream);
        return predict(model, indArray);
    }

    private int[] predict(Model model,
                          INDArray array) {
        if (model instanceof MultiLayerNetwork) {
            return ((MultiLayerNetwork) model).predict(array);
        } else {
            //((ComputationGraph) model).predict(dataSetIterator);
            return null;
        }
    }
}
