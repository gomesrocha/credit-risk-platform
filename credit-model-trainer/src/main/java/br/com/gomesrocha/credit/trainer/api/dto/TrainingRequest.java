package br.com.gomesrocha.credit.trainer.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TrainingRequest(
        @NotBlank
        String modelName,

        @NotBlank
        String version,

        @NotBlank
        String datasetType,

        @NotBlank
        String datasetPath,

        @NotBlank
        String targetColumn,

        @NotBlank
        String positiveClass,

        @NotNull
        Double trainRatio
) {
}