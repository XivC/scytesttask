CREATE TABLE IF NOT EXISTS transactions_state_log (
    id  INT AUTO_INCREMENT PRIMARY KEY,
    FOREIGN KEY (account_id) REFERENCES accounts(id),
    FOREIGN KEY (transaction_id) REFERENCES transactions(id),
    state_from VARCHAR(20),
    state_to VARCHAR(20) NOT NULL,
    updated_at TIMESTAMP,
    account_id INT NOT NULL,
    transaction_id INT NOT NULL
);

