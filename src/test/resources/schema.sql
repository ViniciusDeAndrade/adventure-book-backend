CREATE TABLE IF NOT EXISTS book (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    author VARCHAR(255),
    difficulty VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS book_categories (
    book_id BIGINT NOT NULL,
    categories VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS section (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    section_id INT,
    text CLOB,
    type VARCHAR(50),
    book_id BIGINT
);

CREATE TABLE IF NOT EXISTS section_option (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255),
    goto_section_id INT,
    section_id BIGINT,
    type VARCHAR(50),
    consequence_value INT,
    text VARCHAR(1000)
);

CREATE TABLE IF NOT EXISTS game_session (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id BIGINT,
    current_section_id BIGINT,
    health INT
);
