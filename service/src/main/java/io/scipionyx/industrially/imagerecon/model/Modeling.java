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
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The Model high level, defines name and description.
 * https://www.grammarly.com/blog/modeling-or-modelling/
 *
 * @author Renato Mendes
 */
@Entity
@Table(name = "de_modeling")
@EntityListeners(AuditingEntityListener.class)
@Data
@EqualsAndHashCode
@Builder
public class Modeling implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(length = 4000)
    private String description;

    @CreatedDate
    private LocalDateTime created;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime modified;

    @LastModifiedBy
    private String modifiedBy;

}
