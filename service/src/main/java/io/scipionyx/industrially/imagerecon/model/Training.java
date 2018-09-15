package io.scipionyx.industrially.imagerecon.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "de_training")
@EntityListeners(AuditingEntityListener.class)
@Data
@EqualsAndHashCode
@Builder
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @ManyToOne
    private Modeling modeling;

    @Enumerated(EnumType.STRING)
    private ModelType modelType;

    private long seed;

    private int epochs;

    private int batchSize;

    private double splitTrainTest;

    private boolean useTransformations;

    private boolean shuffleTransformations;

    private int numberOfFlipTransformations;

    private int numberOfWarpTransformations;

    private int numberOfColorConversionTransformations;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "de_training_labels")
    private List<String> labels;

    @CreatedDate
    private LocalDateTime created;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime modified;

    @LastModifiedBy
    private String modifiedBy;

}
