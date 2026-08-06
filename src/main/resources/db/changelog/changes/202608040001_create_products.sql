CREATE TABLE IF NOT EXISTS products (
    id            UUID PRIMARY KEY,
    name          VARCHAR(50) NOT NULL,
    price         NUMERIC(19, 2) NOT NULL,
    description   VARCHAR(500) NOT NULL,
    created_at    TIMESTAMP NOT NULL,
    updated_at    TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_products_name ON products(name);
