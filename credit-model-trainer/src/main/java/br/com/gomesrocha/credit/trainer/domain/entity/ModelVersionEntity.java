package br.com.gomesrocha.credit.trainer.domain.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ml_model_version")
public class ModelVersionEntity extends PanacheEntityBase {

    @Id
    public UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    public ModelEntity model;

    @Column(nullable = false)
    public String version;

    @Column(name = "dataset_type", nullable = false)
    public String datasetType;

    @Column(nullable = false)
    public String algorithm;

    @Column(name = "artifact_uri", nullable = false)
    public String artifactUri;

    @Column(name = "artifact_hash", nullable = false)
    public String artifactHash;

    @Column(name = "target_column", nullable = false)
    public String targetColumn;

    @Column(name = "positive_class", nullable = false)
    public String positiveClass;

    public Double accuracy;

    @Column(name = "evaluation_summary", columnDefinition = "TEXT")
    public String evaluationSummary;

    @Column(nullable = false)
    public String status;

    @Column(name = "created_at", nullable = false)
    public LocalDateTime createdAt;

    @Column(name = "published_at")
    public LocalDateTime publishedAt;
}