CREATE TABLE id_card (
    sno bigserial PRIMARY KEY,
    issued_date DATE NOT NULL,
    check_in_time TIMESTAMP NOT NULL,
    employee_name VARCHAR(255) NOT NULL,
    employee_id bigint NOT NULL,
    checkout_time TIMESTAMP,
    return_date DATE,
    id_issuer VARCHAR(255) NOT NULL
);