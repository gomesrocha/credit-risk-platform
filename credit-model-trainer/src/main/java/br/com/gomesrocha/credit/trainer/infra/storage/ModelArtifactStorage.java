package br.com.gomesrocha.credit.trainer.infra.storage;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Path;

@ApplicationScoped
public class ModelArtifactStorage {

    @Inject
    S3Client s3Client;

    @Inject
    StorageConfig storageConfig;

    public String uploadModel(Path modelPath, String modelName, String version) {
        String key = "models/%s/%s/model.pb".formatted(modelName, version);

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(storageConfig.s3().bucket())
                .key(key)
                .contentType("application/octet-stream")
                .build();

        s3Client.putObject(request, RequestBody.fromFile(modelPath));

        return "s3://%s/%s".formatted(storageConfig.s3().bucket(), key);
    }
}