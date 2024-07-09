--DROP TABLE IF EXISTS orderitem;
--DROP TABLE IF EXISTS orders;

CREATE TABLE IF NOT EXISTS Orders (
    id SERIAL PRIMARY KEY,
    billing_address VARCHAR(255),
    customer_id BIGINT,
    order_date DATE,
    order_status VARCHAR(50),
    payment_method VARCHAR(50),
    shipping_address VARCHAR(255),
    total_amount FLOAT
);

CREATE TABLE IF NOT EXISTS OrderItem (
    id SERIAL PRIMARY KEY,
    price FLOAT,
    product_id BIGINT,
    quantity INT,
    order_id BIGINT,
    CONSTRAINT fk_order
        FOREIGN KEY (order_id)
        REFERENCES Orders (id)
        ON DELETE CASCADE
);