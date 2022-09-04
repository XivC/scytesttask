SET @amount=?;
SET @acc_from_id = ?;
SET @acc_to_id = ?;
set @trans_id = ?;


WITH tmp1(balance_from)
AS (
    SELECT balance AS balance_from FROM accounts WHERE id = @acc_from_id
    ),
tmp2(balance_to)
AS (
    SELECT balance AS balance_to FROM accounts WHERE id = @acc_to_id
)
INSERT INTO account_transactions_log
    (balance_before, balance_after, account_id, transaction_id)
VALUES
    ((SELECT balance_from from tmp1), (SELECT balance_from from tmp1) - @amount, @acc_from_id, @trans_id),
    ((SELECT balance_to from tmp2),  (SELECT balance_to from tmp2) + @amount, @acc_to_id, @trans_id);



UPDATE accounts
    SET balance = CASE id
                  WHEN @acc_from_id THEN balance - @amount
                  WHEN @acc_to_id THEN balance + @amount
                  END
    WHERE id IN (@acc_from_id, @acc_to_id );





