package br.com.gomesrocha.credit.trainer.domain.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ml_model")
public class ModelEntity extends PanacheEntityBase {

    @Id
    public UUID id;

    @Column(nullable = false, unique = true)
    public String name;

    @Column
    public String description;

    @Column(name = "problem_type", nullable = false)
    public String problemType;

    @Column(name = "created_at", nullable = false)
    public LocalDateTime createdAt;

    public static ModelEntity findOrCreate(String name, String problemType) {
        ModelEntity model = find("name", name).firstResult();

        if (model != null) {
            return model;
        }

        model = new ModelEntity();
        model.id = UUID.randomUUID();
        model.name = name;
        model.problemType = problemType;
        model.createdAt = LocalDateTime.now();
        model.persist();

        return model;
    }
}