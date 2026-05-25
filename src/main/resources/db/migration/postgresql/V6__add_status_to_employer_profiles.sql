ALTER TABLE employer_profiles
ADD COLUMN status VARCHAR(20) DEFAULT 'PENDING' NOT NULL;

UPDATE employer_profiles
SET status = 'APPROVED'
WHERE status IS NULL OR status = 'PENDING';