-- create_schema.sql
-- Creates all core tables for the gym management project.

BEGIN;

CREATE TABLE IF NOT EXISTS users (
    user_id          SERIAL PRIMARY KEY,
    username         VARCHAR(50)  NOT NULL UNIQUE,
    password_hash    TEXT         NOT NULL,
    email            VARCHAR(255) NOT NULL UNIQUE,
    phone_number     VARCHAR(30),
    address          TEXT,
    role             VARCHAR(20)  NOT NULL CHECK (role IN ('ADMIN', 'TRAINER', 'MEMBER')),
    created_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS memberships (
    membership_id          SERIAL PRIMARY KEY,
    membership_type        VARCHAR(50)   NOT NULL,
    membership_description TEXT,
    membership_cost        NUMERIC(10,2) NOT NULL CHECK (membership_cost >= 0),
    member_id              INT           NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    start_date             DATE          NOT NULL DEFAULT CURRENT_DATE,
    end_date               DATE          NOT NULL,
    created_at             TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CHECK (end_date >= start_date)
);

CREATE TABLE IF NOT EXISTS workout_classes (
    workout_class_id         SERIAL PRIMARY KEY,
    workout_class_type       VARCHAR(100)  NOT NULL,
    workout_class_description TEXT,
    trainer_id               INT           NOT NULL REFERENCES users(user_id) ON DELETE RESTRICT,
    class_datetime           TIMESTAMP,
    duration_minutes         INT           CHECK (duration_minutes > 0),
    capacity                 INT           NOT NULL DEFAULT 20 CHECK (capacity > 0)
);

CREATE TABLE IF NOT EXISTS gym_merch (
    merch_id           SERIAL PRIMARY KEY,
    merch_name         VARCHAR(120)  NOT NULL,
    merch_type         VARCHAR(80)   NOT NULL,
    merch_price        NUMERIC(10,2) NOT NULL CHECK (merch_price >= 0),
    quantity_in_stock  INT           NOT NULL DEFAULT 0 CHECK (quantity_in_stock >= 0)
);

COMMIT;
