MERGE INTO mpa (id, name) KEY (name) VALUES (1, 'G');
MERGE INTO mpa (id, name) KEY (name) VALUES (2, 'PG');
MERGE INTO mpa (id, name) KEY (name) VALUES (3, 'PG-13');
MERGE INTO mpa (id, name) KEY (name) VALUES (4, 'R');
MERGE INTO mpa (id, name) KEY (name) VALUES (5, 'NC-17');

MERGE INTO genres (id, name) KEY (name) VALUES (1, 'Комедия');
MERGE INTO genres (id, name) KEY (name) VALUES (2, 'Драма');
MERGE INTO genres (id, name) KEY (name) VALUES (3, 'Мультфильм');
MERGE INTO genres (id, name) KEY (name) VALUES (4, 'Триллер');
MERGE INTO genres (id, name) KEY (name) VALUES (5, 'Документальный');
MERGE INTO genres (id, name) KEY (name) VALUES (6, 'Боевик');

INSERT INTO users (email, login, name, birthday)
VALUES ('john.doe@email.com', 'johndoe', 'John Doe', '1990-05-15'),
       ('jane.smith@email.com', 'janesmith', 'Jane Smith', '1985-08-22'),
       ('bob.jones@email.com', 'bobjones', 'Bob Jones', '1995-12-03'),
       ('alice.wilson@email.com', 'alicew', 'Alice Wilson', '1988-03-10');

INSERT INTO films (name, description, release_date, duration, mpa_id)
VALUES ('The Funny Adventure', 'A hilarious journey of friends', '2023-06-15 00:00:00', 120, 2),
       ('Dark Secrets', 'A dramatic tale of mystery', '2022-11-20 00:00:00', 135, 4),
       ('Space Wars', 'Epic space battles', '2024-01-10 00:00:00', 150, 3),
       ('Night Fears', 'Terrifying night experiences', '2023-03-05 00:00:00', 105, 4);

INSERT INTO films_genres (film_id, genre_id)
VALUES (1, 1), (2, 2), (3, 5), (3, 3), (4, 4);

INSERT INTO films_likes (film_id, user_id)
VALUES (1, 1), (1, 2), (1, 4), (2, 3), (3, 1), (4, 4), (4, 2);

INSERT INTO friendships (user_id, friend_id)
VALUES (1, 3), (2, 1);