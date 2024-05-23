INSERT INTO posts (id, user_id, content, created_at, updated_at)
VALUES
(1, 1, "Hi, I'm user1. This is my first post.", '2024-01-02 09:15:23.123456', '2024-01-02 09:15:23.123456'),
(2, 2, "Hi, I'm user2. This is my first post.", '2024-01-03 10:45:12.654321', '2024-01-03 10:45:12.654321'),
(3, 2, "Hi, I'm user2. This is my second post.", '2024-01-04 11:23:34.789012', '2024-01-04 11:23:34.789012'),
(4, 3, "Hi, I'm user3. This is my first post.", '2024-01-16 14:20:45.123789', '2024-01-16 14:20:45.123789'),
(5, 3, "Hi, I'm user3. This is my second post.", '2024-01-17 16:12:34.987654', '2024-01-17 16:12:34.987654'),
(6, 3, "Hi, I'm user3. This is my third post.", '2024-01-18 18:34:56.321987', '2024-01-18 18:34:56.321987'),
(7, 5, "Hi, I'm user5. This is my first post.", '2024-02-21 13:45:23.456789', '2024-02-21 13:45:23.456789'),
(8, 6, "Hi, I'm user6. This is my first post.", '2024-03-11 09:34:12.789012', '2024-03-11 09:34:12.789012'),
(9, 6, "Hi, I'm user6. This is my second post.", '2024-03-12 11:23:34.123456', '2024-03-12 11:23:34.123456'),
(10, 7, "Hi, I'm user7. This is my first post.", '2024-03-26 10:45:23.654321', '2024-03-26 10:45:23.654321'),
(11, 7, "Hi, I'm user7. This is my second post.", '2024-03-27 12:34:56.789123', '2024-03-27 12:34:56.789123'),
(12, 7, "Hi, I'm user7. This is my third post.", '2024-03-28 14:23:45.987654', '2024-03-28 14:23:45.987654'),
(13, 9, "Hi, I'm user9. This is my first post.", '2024-04-21 10:15:34.321987', '2024-04-21 10:15:34.321987'),
(14, 10, "Hi, I'm user10. This is my first post.", '2024-05-16 11:34:56.987654', '2024-05-16 11:34:56.987654'),
(15, 10, "Hi, I'm user10. This is my second post.", '2024-05-17 13:23:45.123456', '2024-05-17 13:23:45.123456');

-- Users with 0 posts: User 4, User 8
-- Users with 1 post: User 1, User 5, User 9
-- Users with 2 posts: User 2, User 6, User 10
-- Users with 3 posts: User 3, User 7
