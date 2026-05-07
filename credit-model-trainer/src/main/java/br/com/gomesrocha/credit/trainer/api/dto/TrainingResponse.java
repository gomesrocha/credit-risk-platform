package br.com.gomesrocha.credit.trainer.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TrainingResponse(
        UUID modelVersionId,
        String modelName,
        String version,
        String status,
        String artifactUri,
        String artifactHash,
        Double accuracy,
        String evaluationSummary,
        LocalDateTime createdAt
) {
}