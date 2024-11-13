CREATE TABLE Book (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    price INT CHECK (price >= 0),
    published BOOLEAN NOT NULL,
    author_id INT,
    CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES Author(id)
);