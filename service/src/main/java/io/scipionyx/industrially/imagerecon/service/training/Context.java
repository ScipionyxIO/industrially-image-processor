package io.scipionyx.industrially.imagerecon.service.training;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.datavec.api.io.labels.PathLabelGenerator;
import org.datavec.api.split.InputSplit;
import org.datavec.image.transform.ImageTransform;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.primitives.Pair;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
class Context implements Serializable {

    private int examples;

    private int[] shapes;

    private int labelsCount;

    private List<String> labels;

    @JsonIgnore
    @Transient
    private transient InputSplit trainData;

    @JsonIgnore
    @Transient
    private transient InputSplit testData;

    @JsonIgnore
    @Transient
    private transient PathLabelGenerator labelGenerator;

    @JsonIgnore
    @Transient
    private transient DataNormalization dataNormalizer;

    @JsonIgnore
    @Transient
    private transient List<Pair<ImageTransform, Double>> pipeline;

}
