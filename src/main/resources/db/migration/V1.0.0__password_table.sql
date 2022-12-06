CREATE TABLE IF NOT EXISTS password(
     id VARCHAR(64) PRIMARY KEY,
     name VARCHAR(255) NOT NULL,
     secret_key VARCHAR(255) NOT NULL,
     created_at TIMESTAMP NOT NULL
);