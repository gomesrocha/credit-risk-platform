package br.com.gomesrocha.credit.trainer.domain.service;

import br.com.gomesrocha.credit.trainer.api.dto.TrainingRequest;
import br.com.gomesrocha.credit.trainer.api.dto.TrainingResponse;
import br.com.gomesrocha.credit.trainer.domain.entity.ModelEntity;
import br.com.gomesrocha.credit.trainer.domain.entity.ModelVersionEntity;
import br.com.gomesrocha.credit.trainer.infra.storage.ModelArtifactStorage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.tribuo.Model;
import org.tribuo.MutableDataset;
import org.tribuo.Trainer;
import org.tribuo.classification.Label;
import org.tribuo.classification.LabelFactory;
import org.tribuo.classification.evaluation.LabelEvaluator;
import org.tribuo.classification.sgd.linear.LogisticRegressionTrainer;
import org.tribuo.data.csv.CSVLoader;
import org.tribuo.evaluation.TrainTestSplitter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;

@ApplicationScoped
public class TrainingService {

    @Inject
    ModelArtifactStorage artifactStorage;

    @Inject
    FileHashService fileHashService;

    @ConfigProperty(name = "app.models.local-dir")
    String localModelDir;

    @Transactional
public TrainingResponse train(TrainingRequest request) {
    try {
        validateVersionDoesNotExist(request.modelName(), request.version());

        Path datasetPath = Path.of(request.datasetPath());

        if (!Files.exists(datasetPath)) {
            throw new IllegalArgumentException("Dataset não encontrado: " + datasetPath);
        }

        Files.createDirectories(Path.of(localModelDir));

        var labelFactory = new LabelFactory();
        var csvLoader = new CSVLoader<>(labelFactory);

        var source = csvLoader.loadDataSource(
                datasetPath,
                request.targetColumn()
        );

        double trainRatio = request.trainRatio() != null ? request.trainRatio() : 0.7;

        var splitter = new TrainTestSplitter<>(
                source,
                trainRatio,
                42L
        );

        var trainingDataset = new MutableDataset<>(splitter.getTrain());
        var testingDataset = new MutableDataset<>(splitter.getTest());

        Trainer<Label> trainer = new LogisticRegressionTrainer();

        Model<Label> model = trainer.train(trainingDataset);

        var evaluator = new LabelEvaluator();
        var evaluation = evaluator.evaluate(model, testingDataset);

        Path modelOutputPath = Path.of(
                localModelDir,
                request.modelName() + "-" + request.version() + ".pb"
        );

        model.serializeToFile(modelOutputPath);

        String artifactHash = fileHashService.sha256(modelOutputPath);

        String artifactUri = artifactStorage.uploadModel(
                modelOutputPath,
                request.modelName(),
                request.version()
        );

        ModelEntity modelEntity = ModelEntity.findOrCreate(
                request.modelName(),
                "CLASSIFICATION"
        );

        ModelVersionEntity versionEntity = new ModelVersionEntity();
        versionEntity.id = UUID.randomUUID();
        versionEntity.model = modelEntity;
        versionEntity.version = request.version();
        versionEntity.datasetType = request.datasetType();
        versionEntity.algorithm = "LOGISTIC_REGRESSION";
        versionEntity.artifactUri = artifactUri;
        versionEntity.artifactHash = artifactHash;
        versionEntity.targetColumn = request.targetColumn();
        versionEntity.positiveClass = request.positiveClass();
        versionEntity.accuracy = evaluation.accuracy();
        versionEntity.evaluationSummary = evaluation.toString();
        versionEntity.status = "PUBLISHED";
        versionEntity.createdAt = LocalDateTime.now();
        versionEntity.publishedAt = LocalDateTime.now();
        versionEntity.persist();

        return new TrainingResponse(
                versionEntity.id,
                request.modelName(),
                request.version(),
                versionEntity.status,
                artifactUri,
                artifactHash,
                versionEntity.accuracy,
                versionEntity.evaluationSummary,
                versionEntity.createdAt
        );

    } catch (IllegalArgumentException e) {
        throw e;
    } catch (Exception e) {
        throw new IllegalStateException("Erro ao treinar modelo com Tribuo", e);
    }
}
@Transactional
public TrainingResponse train(TrainingRequest request) {
    try {
        validateVersionDoesNotExist(request.modelName(), request.version());

        Path datasetPath = Path.of(request.datasetPath());

        if (!Files.exists(datasetPath)) {
            throw new IllegalArgumentException("Dataset não encontrado: " + datasetPath);
        }

        Files.createDirectories(Path.of(localModelDir));

        var labelFactory = new LabelFactory();
        var csvLoader = new CSVLoader<>(labelFactory);

        var source = csvLoader.loadDataSource(
                datasetPath,
                request.targetColumn()
        );

        double trainRatio = request.trainRatio() != null ? request.trainRatio() : 0.7;

        var splitter = new TrainTestSplitter<>(
                source,
                trainRatio,
                42L
        );

        var trainingDataset = new MutableDataset<>(splitter.getTrain());
        var testingDataset = new MutableDataset<>(splitter.getTest());

        Trainer<Label> trainer = new LogisticRegressionTrainer();

        Model<Label> model = trainer.train(trainingDataset);

        var evaluator = new LabelEvaluator();
        var evaluation = evaluator.evaluate(model, testingDataset);

        Path modelOutputPath = Path.of(
                localModelDir,
                request.modelName() + "-" + request.version() + ".pb"
        );

        model.serializeToFile(modelOutputPath);

        String artifactHash = fileHashService.sha256(modelOutputPath);

        String artifactUri = artifactStorage.uploadModel(
                modelOutputPath,
                request.modelName(),
                request.version()
        );

        ModelEntity modelEntity = ModelEntity.findOrCreate(
                request.modelName(),
                "CLASSIFICATION"
        );

        ModelVersionEntity versionEntity = new ModelVersionEntity();
        versionEntity.id = UUID.randomUUID();
        versionEntity.model = modelEntity;
        versionEntity.version = request.version();
        versionEntity.datasetType = request.datasetType();
        versionEntity.algorithm = "LOGISTIC_REGRESSION";
        versionEntity.artifactUri = artifactUri;
        versionEntity.artifactHash = artifactHash;
        versionEntity.targetColumn = request.targetColumn();
        versionEntity.positiveClass = request.positiveClass();
        versionEntity.accuracy = evaluation.accuracy();
        versionEntity.evaluationSummary = evaluation.toString();
        versionEntity.status = "PUBLISHED";
        versionEntity.createdAt = LocalDateTime.now();
        versionEntity.publishedAt = LocalDateTime.now();
        versionEntity.persist();

        return new TrainingResponse(
                versionEntity.id,
                request.modelName(),
                request.version(),
                versionEntity.status,
                artifactUri,
                artifactHash,
                versionEntity.accuracy,
                versionEntity.evaluationSummary,
                versionEntity.createdAt
        );

    } catch (IllegalArgumentException e) {
        throw e;
    } catch (Exception e) {
        throw new IllegalStateException("Erro ao treinar modelo com Tribuo", e);
    }
}

}