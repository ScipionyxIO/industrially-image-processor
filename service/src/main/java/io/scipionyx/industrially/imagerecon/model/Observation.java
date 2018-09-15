package io.scipionyx.industrially.imagerecon.model;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.File;

@Entity
@Table(name = "de_observation")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Observation {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @NonNull
    @ManyToOne
    private Modeling modeling;

    private String location;

    private boolean compressed;

    @NonNull
    private String contentType;

    @NonNull
    private String originalFileName;

    @NonNull
    private String label;

    @Transient
    private File file;

}
