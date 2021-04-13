INSERT INTO certificate (id, name, description, price, duration, create_date, last_update_date)
VALUES (1, 'certificate number 1', 'description number 2', 33.33, 5, '2020-05-05 05:55:55', '2021-01-01 01:11:11'),
       (2, 'certificate number 2', 'description number 3', 44.44, 5, '2020-01-01 01:11:11', '2021-02-02 02:22:22'),
       (3, 'certificate number 3', 'description number 4', 55.55, 10, '2020-02-02 02:22:22', '2021-03-03 03:33:33'),
       (4, 'certificate number 4', 'description number 5', 11.11, 10, '2020-03-03 03:33:33', '2021-04-04 04:44:44'),
       (5, 'certificate number 5', 'description number 1', 22.22, 15, '2020-04-04 04:44:44', '2021-05-05 05:55:55');


INSERT INTO tag (id, name)
VALUES (1, 'tag1'),
       (2, 'tag2'),
       (3, 'tag3'),
       (4, 'tag4'),
       (5, 'tag5');


INSERT INTO certificate_tag (certificate_id, tag_id)
VALUES (1, 1),
       (1, 2),
       (2, 2);
