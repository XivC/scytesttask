CREATE TABLE IF NOT EXISTS users_clans (
    id INT PRIMARY KEY,
    user_id INT NOT NULL,
    clan_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (clan_id) REFERENCES clans(id)
);
