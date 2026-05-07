CREATE TABLE ml_model (
    id UUID PRIMARY KEY,
    name VARCHAR(120) NOT NULL UNIQUE,
    description TEXT,
    problem_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE ml_model_version (
    id UUID PRIMARY KEY,
    model_id UUID NOT NULL REFERENCES ml_model(id),

    version VARCHAR(30) NOT NULL,
    dataset_type VARCHAR(50) NOT NULL,
    algorithm VARCHAR(80) NOT NULL,

    artifact_uri TEXT NOT NULL,
    artifact_hash VARCHAR(128) NOT NULL,

    target_column VARCHAR(100) NOT NULL,
    positive_class VARCHAR(50) NOT NULL,

    accuracy DOUBLE PRECISION,
    evaluation_summary TEXT,

    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    published_at TIMESTAMP,

    UNIQUE(model_id, version)
);

CREATE TABLE ml_feature_schema (
    id UUID PRIMARY KEY,
    model_version_id UUID NOT NULL REFERENCES ml_model_version(id),

    feature_name VARCHAR(150) NOT NULL,
    feature_type VARCHAR(30) NOT NULL,
    required BOOLEAN NOT NULL DEFAULT true,
    default_value VARCHAR(255),
    position_order INTEGER NOT NULL
);