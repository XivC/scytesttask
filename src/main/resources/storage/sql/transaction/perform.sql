SET @amount=?;
SET @acc_from_id = ?;
SET @acc_to_id = ?;

UPDATE accounts
    SET balance = CASE id
                  WHEN @acc_from_id THEN balance - @amount
                  WHEN @acc_to_id THEN balance + @amount
                  END
    WHERE id IN (@acc_from_id, @acc_to_id );

