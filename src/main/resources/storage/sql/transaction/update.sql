UPDATE transactions SET
    account_from_id = ?,
    account_to_id = ?,
    amount = ?,
    created_at = ?,
    updated_at = ?,
    state = ?,
    type = ?
WHERE id = ?;
