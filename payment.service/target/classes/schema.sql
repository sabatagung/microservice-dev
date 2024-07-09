--DROP TABLE IF EXISTS payment
CREATE TABLE IF NOT EXISTS payment (
    id SERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    balance FLOAT NOT NULL
);

CREATE TABLE IF NOT EXISTS TransactionDetails (
    id SERIAL PRIMARY KEY,
    amount FLOAT NOT NULL,
    order_id BIGINT,
    payment_date DATE,
    mode VARCHAR(255),
    status VARCHAR(255),
    reference_number VARCHAR(255)
);

