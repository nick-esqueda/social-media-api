INSERT INTO comments (id, user_id, post_id, content, created_at, updated_at)
VALUES
(1, 1, 2, "I'm user1 and I'm commenting on post #2.", '2024-01-02 12:00:00.000000', '2024-01-02 12:00:00.000000'),
(2, 2, 1, "I'm user2 and I'm commenting on post #1.", '2024-01-03 12:00:00.000000', '2024-01-03 12:00:00.000000'),
(3, 2, 1, "I'm user2 and I'm commenting on post #1 again.", '2024-01-03 13:00:00.000000', '2024-01-03 13:00:00.000000'),
(4, 3, 2, "I'm user3 and I'm commenting on post #2.", '2024-01-16 15:00:00.000000', '2024-01-16 15:00:00.000000'),
(5, 3, 3, "I'm user3 and I'm commenting on post #3.", '2024-01-17 17:00:00.000000', '2024-01-17 17:00:00.000000'),
(6, 3, 4, "I'm user3 and I'm commenting on post #4.", '2024-01-18 19:00:00.000000', '2024-01-18 19:00:00.000000'),
(7, 5, 5, "I'm user5 and I'm commenting on post #5.", '2024-02-21 14:00:00.000000', '2024-02-21 14:00:00.000000'),
(8, 6, 6, "I'm user6 and I'm commenting on post #6.", '2024-03-11 10:00:00.000000', '2024-03-11 10:00:00.000000'),
(9, 6, 7, "I'm user6 and I'm commenting on post #7.", '2024-03-12 12:00:00.000000', '2024-03-12 12:00:00.000000'),
(10, 7, 8, "I'm user7 and I'm commenting on post #8.", '2024-03-26 11:00:00.000000', '2024-03-26 11:00:00.000000'),
(11, 7, 8, "I'm user7 and I'm commenting on post #8 again.", '2024-03-26 12:00:00.000000', '2024-03-26 12:00:00.000000'),
(12, 7, 9, "I'm user7 and I'm commenting on post #9.", '2024-03-27 13:00:00.000000', '2024-03-27 13:00:00.000000'),
(13, 9, 12, "I'm user9 and I'm commenting on post #12.", '2024-04-21 11:00:00.000000', '2024-04-21 11:00:00.000000'),
(14, 9, 12, "I'm user9 and I'm commenting on post #12 again.", '2024-04-21 12:00:00.000000', '2024-04-21 12:00:00.000000'),
(15, 9, 13, "I'm user9 and I'm commenting on post #13.", '2024-04-21 13:00:00.000000', '2024-04-21 13:00:00.000000'),
(16, 10, 14, "I'm user10 and I'm commenting on post #14.", '2024-05-16 14:00:00.000000', '2024-05-16 14:00:00.000000'),
(17, 10, 15, "I'm user10 and I'm commenting on post #15.", '2024-05-17 15:00:00.000000', '2024-05-17 15:00:00.000000'),
(18, 10, 15, "I'm user10 and I'm commenting on post #15 again.", '2024-05-17 16:00:00.000000', '2024-05-17 16:00:00.000000');

-- User 1: 1 comment
-- User 2: 2 comments (on the same post)
-- User 3: 3 comments (on different posts)
-- User 5: 1 comment
-- User 6: 2 comments (on different posts)
-- User 7: 3 comments (on different posts)
-- User 8: 0 comments
-- User 9: 3 comments (with two on the same post)
-- User 10: 3 comments (with two on the same post)
