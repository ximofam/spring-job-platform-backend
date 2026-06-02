CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE INDEX idx_jobs_title_trgm ON jobs USING GIN (title gin_trgm_ops);
