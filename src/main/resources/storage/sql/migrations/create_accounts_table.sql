CREATE TABLE IF NOT EXISTS accounts(

    id  INT AUTO_INCREMENT PRIMARY KEY,
    owner_id INT NOT NULL,
    owner_type VARCHAR(20) NOT NULL,
    balance INT NOT NULL
);

