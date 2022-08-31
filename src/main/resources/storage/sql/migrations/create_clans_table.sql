CREATE TABLE IF NOT EXISTS clans(

    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    account_id INT NOT NULL,
    FOREIGN KEY(account_id) REFERENCES accounts(id)

);

