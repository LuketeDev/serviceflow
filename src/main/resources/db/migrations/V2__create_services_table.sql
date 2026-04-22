CREATE TABLE services (
    id UUID PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    description VARCHAR(500),
    duration_in_minutes INTEGER NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    user_id UUID NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_services_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT
);

CREATE INDEX idx_services_user_id ON services (user_id);