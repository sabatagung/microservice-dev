--DROP TABLE IF EXISTS product

CREATE TABLE IF NOT EXISTS product (
    id SERIAL PRIMARY KEY ,
    name VARCHAR(255) NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    category VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    description TEXT,
    img_url VARCHAR(255),
    stock_quantity INTEGER,
    updated_at TIMESTAMP
);

