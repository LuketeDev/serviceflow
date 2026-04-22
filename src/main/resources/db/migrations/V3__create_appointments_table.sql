CREATE TABLE appointments (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    service_id UUID NOT NULL,
    customer_name VARCHAR(150) NOT NULL,
    customer_phone VARCHAR(30),
    appointment_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    status VARCHAR(20) NOT NULL,
    notes VARCHAR(500),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_appointments_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT,
    CONSTRAINT fk_appointments_service FOREIGN KEY (service_id) REFERENCES services (id) ON DELETE RESTRICT,
    CONSTRAINT chk_time_valid CHECK (start_time < end_time)
);

CREATE INDEX idx_appointments_user_date ON appointments (user_id, appointment_date);

CREATE INDEX idx_appointments_service_id ON appointments (service_id);