CREATE TABLE socks (
    id UUID PRIMARY KEY,
    color VARCHAR(255) NOT NULL,
    cotton_percentage INT NOT NULL,
    quantity BIGINT NOT NULL
);

CREATE INDEX idx_socks_color ON socks (color);
CREATE INDEX idx_socks_cotton_percentage ON socks (cotton_percentage);