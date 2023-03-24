USE ipsen2_werkplaatsen;

-- Clear all the tables
SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM employee;
DELETE FROM employee_roles;
DELETE FROM work_room;
DELETE FROM work_room_reservation;
DELETE FROM meeting_room;
DELETE FROM meeting_reservation;
DELETE FROM location;


INSERT INTO location (id, address, max_capacity, name) VALUES
    (1, "Wagnerstraat 189", 50, "Venray"),
    (2, "Wateringse veld 45", 25, "Wateringen");

INSERT INTO employee (id, name, email, password) VALUES
    (1, "root", "root@gmail.com", "$2y$10$KQ./DcmX7u376BPZeWBu.ODkioyq7dOOA6nQV21vAXL1B5FHjU1X2"),
    (2, "Ben", "ben@gmail.com", "$2y$10$NYV5ynpiyHQuquqFhWhiIuSKnUfww5n0rzXvJXnnk92/tOO1eSrhS"),
    (3, "Karel", "karel@gmail.com", "$2y$10$Pk5dKcYHbksfWwYXYzuFc.BZzYkIBLlpn4Wm/KrKjrsHpmZ.iOhwC"),
    (4, "Nick", "nick@gmail.com", "$2y$10$0976M/AIxQNgVdCIvMw4l.gdHOkGQoIaCJQNujJ45mDMXB59M1idi"),
    (5, "Jack", "jack@gmail.com", "$2y$10$UmTqktT/kO9KW19./WIIT.Vwm.B1CJodsN3UixTX5e5FqwZLo89s.");

INSERT INTO employee_roles (employee_id, role_id) VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (2, 2),
    (2, 3),
    (3, 3),
    (4, 3),
    (5, 3);

INSERT INTO work_room (id, location_id, max_capacity) VALUES
    (1, 1, 2),
    (2, 1, 1),
    (3, 1, 3),
    (4, 1, 2),
    (5, 1, 3),
    (6, 2, 2),
    (7, 2, 1),
    (8, 2, 3),
    (9, 2, 2);

INSERT INTO work_room_reservation (id, workroom_id, employee_id, start_time, end_time, present) VALUES
    (1, 1, 2, "2022-11-16 13:00:00", "2022-08-16 16:00:00", true),
    (2, 4, 3, "2022-11-16 13:00:00", "2022-08-16 16:00:00", true);

INSERT INTO meeting_room (id, location_id, max_capacity) VALUES
    (1, 1, 6),
    (2, 1, 8),
    (3, 1, 12),
    (4, 1, 8),
    (5, 1, 18),
    (6, 2, 16),
    (7, 2, 12),
    (8, 2, 8),
    (9, 2, 12);

INSERT INTO meeting_reservation (id, meeting_room_id, employee_id, start_time, end_time, present) VALUES
    (1, 1, 4, "2022-11-16 13:00:00", "2022-08-16 16:00:00", true);
