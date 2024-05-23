INSERT INTO users (id, username, first_name, last_name, email, phone_number, birthday, gender, bio, password_hash, created_at, updated_at)
VALUES
(1, 'user1', null, null, null, null, null, null, null, '$2a$10$8kUWUuH/USEOxU4oeId.kuS3cwhuCxounNWn37TE9yC6/BPeDlGrO', '2024-01-01 12:34:56.789012', '2024-01-01 12:34:56.789012'),
(2, 'user2', null, null, null, null, null, null, null, '$2a$10$8kUWUuH/USEOxU4oeId.kuS3cwhuCxounNWn37TE9yC6/BPeDlGrO', '2024-01-01 12:34:56.789012', '2024-01-01 12:34:56.789012'),
(3, 'user3', 'Mohammad', null, null, null, null, null, null, '$2a$10$8kUWUuH/USEOxU4oeId.kuS3cwhuCxounNWn37TE9yC6/BPeDlGrO', '2024-01-15 10:45:23.456789', '2024-01-15 10:45:23.456789'),
(4, 'user4', 'Li', 'Wei', null, null, null, null, null, '$2a$10$8kUWUuH/USEOxU4oeId.kuS3cwhuCxounNWn37TE9yC6/BPeDlGrO', '2024-02-01 09:23:34.123456', '2024-02-01 09:23:34.123456'),
(5, 'user5', 'Maria', 'Garcia', 'mg@mg.com', null, null, null, null, '$2a$10$8kUWUuH/USEOxU4oeId.kuS3cwhuCxounNWn37TE9yC6/BPeDlGrO', '2024-02-20 11:12:45.987654', '2024-02-20 11:12:45.987654'),
(6, 'user6', 'John', 'Smith', 'js@js.com', '1234567890', null, null, null, '$2a$10$8kUWUuH/USEOxU4oeId.kuS3cwhuCxounNWn37TE9yC6/BPeDlGrO', '2024-03-10 14:34:12.654321', '2024-03-10 14:34:12.654321'),
(7, 'user7', 'Jose', 'Rodriguez', 'jr@jr.com', '2345678901', '1970-01-01', null, null, '$2a$10$8kUWUuH/USEOxU4oeId.kuS3cwhuCxounNWn37TE9yC6/BPeDlGrO', '2024-03-25 16:23:45.789123', '2024-03-25 16:23:45.789123'),
(8, 'user8', 'Ahmed', 'Hassan', 'ah@ah.com', '3456789012', '1980-02-02', null, null, '$2a$10$8kUWUuH/USEOxU4oeId.kuS3cwhuCxounNWn37TE9yC6/BPeDlGrO', '2024-04-05 18:12:34.321987', '2024-04-05 18:12:34.321987'),
(9, 'user9', 'Wei', 'Zhang', 'wz@wz.com', '4567890123', '1990-03-03', 'MAN', null, '$2a$10$8kUWUuH/USEOxU4oeId.kuS3cwhuCxounNWn37TE9yC6/BPeDlGrO', '2024-04-20 07:45:56.123789', '2024-04-20 07:45:56.123789'),
(10, 'user10', 'Fatima', 'Zahra', 'fz@fz.com', '5678901234', '2000-04-04', 'WOMAN', 'Hi, my name is Fatima Zahra.', '$2a$10$8kUWUuH/USEOxU4oeId.kuS3cwhuCxounNWn37TE9yC6/BPeDlGrO', '2024-05-15 12:34:56.987654', '2024-05-15 12:34:56.987654');

-- "password_hash" for all users is "password" hashed.
