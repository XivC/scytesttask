CREATE TABLE IF NOT EXISTS accounts(

    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    owner_id INT NOT NULL,
    owner_type VARCHAR(20) NOT NULL
);

