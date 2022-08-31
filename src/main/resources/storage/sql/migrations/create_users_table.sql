CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    clan_id INT NOT NULL,
    account_id INT NOT NULL,
    FOREIGN KEY(account_id) REFERENCES accounts(id),
    FOREIGN KEY(clan_id) REFERENCES clans(id)
);

