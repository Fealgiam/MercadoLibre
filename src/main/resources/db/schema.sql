CREATE TABLE IF NOT EXISTS redeemed_products
(
    id_product VARCHAR(255) PRIMARY KEY,
    id_country VARCHAR(255) NOT NULL,
    number_redeemed BIGINT DEFAULT 1,
    update_date TIMESTAMP NOT NULL
);

CREATE INDEX idx_id_country ON redeemed_products (id_country);

CREATE TRIGGER increment_value_insert
    BEFORE INSERT ON redeemed_products
    FOR EACH ROW
    CALL "com.mercadolibre.coupon.infrastructure.outputpoint.jpa.repository.TriggerUpdateNumberRedeemed";

CREATE TRIGGER increment_value_update
    BEFORE UPDATE ON redeemed_products
    FOR EACH ROW
    CALL "com.mercadolibre.coupon.infrastructure.outputpoint.jpa.repository.TriggerUpdateNumberRedeemed";
