CREATE TABLE IF NOT EXISTS Client(
    client_id UUID PRIMARY KEY,
    last_name VARCHAR(20) NOT NULL,
    first_name VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS Account(
    account_id UUID PRIMARY KEY,
    balance DECIMAL(4, 2) NOT NULL,
    pin_code VARCHAR(5) NOT NULL,
    client_id UUID NOT NULL,
    CONSTRAINT Account_client_id FOREIGN KEY (client_id) REFERENCES Client (client_id)
);

CREATE TABLE IF NOT EXISTS Transaction_History(
    transaction_id UUID PRIMARY KEY,
    time TIMESTAMP NOT NULL,
    operation_type VARCHAR(10) NOT NULL,
    amount DECIMAL(4, 2) NOT NULL,
    account_id UUID NOT NULL,
    target_account_id UUID,
    CONSTRAINT TransactionHistory_account_id FOREIGN KEY (account_id) REFERENCES Account (account_id),
    CONSTRAINT TransactionHistory_target_account_id FOREIGN KEY (target_account_id) REFERENCES Account (account_id)
)