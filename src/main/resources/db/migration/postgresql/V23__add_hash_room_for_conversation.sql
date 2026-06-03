ALTER TABLE conversations ADD COLUMN room_hash VARCHAR(100);

CREATE UNIQUE INDEX uq_private_conversations_room_hash
ON conversations (room_hash)
WHERE type = 'PRIVATE';