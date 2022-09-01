CREATE TABLE IF NOT EXISTS account_transactions_log
(
    id INT AUTO_INCREMENT PRIMARY KEY ,
    FOREIGN KEY (account_id) REFERENCES accounts (id),
    FOREIGN KEY (transaction_id) REFERENCES transactions(id),
    balance_before INT NOT NULL,
    balance_after  INT,
    account_id int not null,
    transaction_id INT NOT NULL
);
