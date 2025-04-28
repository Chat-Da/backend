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
(3, 'df83a11e-fb42-48a2-b1c7-57c767b61efc', '학생2', 'ROLE_STUDENT', 1, 3, 1, current_time, current_time, false);

insert into teacher(member_id) values
(1);

insert into student(member_id, student_number, src) values
(2, 1, 'image'),
(3, 2, 'image');