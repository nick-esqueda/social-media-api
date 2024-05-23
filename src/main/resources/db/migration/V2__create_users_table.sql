CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(32) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(320) UNIQUE,
    phone_number VARCHAR(10) UNIQUE,
    birthday DATE,
    gender ENUM('MAN', 'WOMAN', 'NON_BINARY', 'PREFER_NOT_TO_STATE'),
    bio VARCHAR(255),
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6),
    PRIMARY KEY (id)
);