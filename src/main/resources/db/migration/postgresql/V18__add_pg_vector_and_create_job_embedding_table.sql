DROP TRIGGER IF EXISTS trg_jobs_search_vector_update ON jobs;
DROP FUNCTION IF EXISTS update_jobs_search_vector();
DROP INDEX IF EXISTS jobs_search_vector_idx;
ALTER TABLE jobs DROP COLUMN IF EXISTS search_vector;

CREATE EXTENSION IF NOT EXISTS vector;

ALTER TABLE jobs
ADD COLUMN embedding VECTOR(1024);

CREATE INDEX idx_jobs_embedding ON jobs USING hnsw (embedding vector_cosine_ops);