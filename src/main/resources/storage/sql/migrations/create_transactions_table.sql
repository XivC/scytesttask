CREATE TABLE IF NOT EXISTS transactions (
    id  INT AUTO_INCREMENT PRIMARY KEY,
    account_from_id INT NOT NULL,
    account_to_id INT NOT NULL,
    amount INT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    state VARCHAR(20),
    type VARCHAR(20),
    info JSON

);

