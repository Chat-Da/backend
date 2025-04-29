insert into school(school_id, name) values
(1, 'oo 고등학교');

insert into grade(school_id, level) values
(1, 1),
(1, 2),
(1, 3);

insert into classes(school_id, level, class_number) values
(1, 1, 1),
(1, 1, 2),
(1, 1, 3),
(1, 2, 1),
(1, 2, 2),
(1, 2, 3),
(1, 3, 1),
(1, 3, 2),
(1, 3, 3);

insert into member(member_id, uuid, name, role, school_id, level, class_number, created_at, modified_at, is_deleted) values
                                                                                                                         (1, '5a1e826e-2a44-4fea-98b2-bb96887b9737', '선생', 'ROLE_TEACHER', 1, 3, 1, current_time, current_time, false),
                                                                                                                         (2, '78954910-7440-4241-bd9d-ca3abc44d291', '학생1', 'ROLE_STUDENT', 1, 3, 1, current_time, current_time, false),
                                                                                                                         (3, 'df83a11e-fb42-48a2-b1c7-57c767b61efc', '학생2', 'ROLE_STUDENT', 1, 3, 1, current_time, current_time, false),
                                                                                                                         (4, '54449b31-c04d-4c15-9136-c7e2a618564e', '학생3', 'ROLE_STUDENT', 1, 3, 1, current_time, current_time, false),
                                                                                                                         (10001, 'efbbf7e9-ada3-46ab-9c93-cc5fa23a5596', '다른 반 선생', 'ROLE_TEACHER', 1, 3, 2, current_time, current_time, false),
                                                                                                                         (10002, '94faed17-2889-419f-bdb4-2d4c00ce5fda', '다른 반 학생', 'ROLE_STUDENT', 1, 3, 2, current_time, current_time, false);

insert into teacher(member_id) values
                                   (1),
                                   (10001);

insert into student(member_id, student_number, src) values
                                                        (2, 1, 'image'),
                                                        (3, 2, 'image'),
                                                        (4, 3, 'image'),
                                                        (10002, 1, 'image');

insert into counsel(counsel_id, school_id, level, student_id, teacher_id, step, is_deleted, created_at, modified_at) values
                                                                                                                         (1, 1, 1, 2, 10001, 'COMPLETED', false, '2023-03-05 00:00:00', '2023-03-06 00:00:00'),
                                                                                                                         (2, 1, 1, 2, 10001, 'COMPLETED', false, '2023-09-06 00:00:00', '2023-09-07 00:00:00'),
                                                                                                                         (3, 1, 2, 2, 1, 'COMPLETED', false, '2024-03-11 00:00:00', '2024-03-12 00:00:00'),
                                                                                                                         (4, 1, 2, 2, 1, 'COMPLETED', false, '2024-09-03 00:00:00', '2024-09-05 00:00:00'),
                                                                                                                         (5, 1, 3, 3, 1, 'RESULT_WAITING', false, current_time, current_time);

