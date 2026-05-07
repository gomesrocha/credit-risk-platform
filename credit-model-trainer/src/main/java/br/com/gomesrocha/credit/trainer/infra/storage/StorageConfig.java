package br.com.gomesrocha.credit.trainer.infra.storage;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "app.storage")
public interface StorageConfig {

    S3 s3();

    interface S3 {
        String endpoint();
        String accessKey();
        String secretKey();
        String bucket();
        String region();
    }
}