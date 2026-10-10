CREATE TABLE test_entity
(
    test_id UUID PRIMARY KEY,
    message VARCHAR(255),
    total_request INTEGER
);
INSERT INTO test_entity (test_id, message, total_request) VALUES
(gen_random_uuid(), 'Premier message', 0);