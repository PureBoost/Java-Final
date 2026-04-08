-- seed_data.sql
-- Inserts sample records for development/testing.

BEGIN;

-- BCrypt hash below is a sample hash for password "password".
INSERT INTO users (username, password_hash, email, phone_number, address, role)
VALUES
    ('admin1',   '$2a$10$7EqJtq98hPqEX7fNZaFWoO5P6fO5Kb/6VQwWi4KFOeFHrgb3R04k6', 'admin1@gym.local',   '555-0001', '1 Admin Way',   'ADMIN'),
    ('trainer1', '$2a$10$7EqJtq98hPqEX7fNZaFWoO5P6fO5Kb/6VQwWi4KFOeFHrgb3R04k6', 'trainer1@gym.local', '555-0002', '2 Trainer Ave', 'TRAINER'),
    ('member1',  '$2a$10$7EqJtq98hPqEX7fNZaFWoO5P6fO5Kb/6VQwWi4KFOeFHrgb3R04k6', 'member1@gym.local',  '555-0003', '3 Member St',   'MEMBER')
ON CONFLICT (username) DO NOTHING;

INSERT INTO memberships (membership_type, membership_description, membership_cost, member_id, start_date, end_date)
SELECT 'Monthly', 'Standard monthly plan', 45.00, u.user_id, CURRENT_DATE, CURRENT_DATE + 30
FROM users u
WHERE u.username = 'member1'
    AND NOT EXISTS (
            SELECT 1
            FROM memberships m
            WHERE m.member_id = u.user_id
                AND m.membership_type = 'Monthly'
                AND m.start_date = CURRENT_DATE
    );

INSERT INTO memberships (membership_type, membership_description, membership_cost, member_id, start_date, end_date)
SELECT 'Monthly', 'Trainer self-membership', 45.00, u.user_id, CURRENT_DATE, CURRENT_DATE + 30
FROM users u
WHERE u.username = 'trainer1'
    AND NOT EXISTS (
            SELECT 1
            FROM memberships m
            WHERE m.member_id = u.user_id
                AND m.membership_type = 'Monthly'
                AND m.start_date = CURRENT_DATE
    );

INSERT INTO workout_classes (workout_class_type, workout_class_description, trainer_id, class_datetime, duration_minutes, capacity)
SELECT 'HIIT', 'High-intensity interval training', u.user_id, CURRENT_TIMESTAMP + INTERVAL '1 day', 60, 25
FROM users u
WHERE u.username = 'trainer1'
    AND NOT EXISTS (
            SELECT 1
            FROM workout_classes wc
            WHERE wc.trainer_id = u.user_id
                AND wc.workout_class_type = 'HIIT'
    );

INSERT INTO workout_classes (workout_class_type, workout_class_description, trainer_id, class_datetime, duration_minutes, capacity)
SELECT 'Yoga', 'Beginner-friendly yoga class', u.user_id, CURRENT_TIMESTAMP + INTERVAL '2 days', 50, 20
FROM users u
WHERE u.username = 'trainer1'
    AND NOT EXISTS (
            SELECT 1
            FROM workout_classes wc
            WHERE wc.trainer_id = u.user_id
                AND wc.workout_class_type = 'Yoga'
    );

INSERT INTO gym_merch (merch_name, merch_type, merch_price, quantity_in_stock)
SELECT 'Protein Shake', 'Drink', 6.50, 40
WHERE NOT EXISTS (SELECT 1 FROM gym_merch gm WHERE gm.merch_name = 'Protein Shake');

INSERT INTO gym_merch (merch_name, merch_type, merch_price, quantity_in_stock)
SELECT 'Resistance Band', 'Workout Gear', 12.99, 25
WHERE NOT EXISTS (SELECT 1 FROM gym_merch gm WHERE gm.merch_name = 'Resistance Band');

INSERT INTO gym_merch (merch_name, merch_type, merch_price, quantity_in_stock)
SELECT 'Energy Bar', 'Food', 2.99, 60
WHERE NOT EXISTS (SELECT 1 FROM gym_merch gm WHERE gm.merch_name = 'Energy Bar');

COMMIT;
