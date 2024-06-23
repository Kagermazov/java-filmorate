DROP SCHEMA PUBLIC CASCADE;
CREATE SCHEMA PUBLIC;

--создать таблицу с фильмами

CREATE TABLE films (id bigint GENERATED always AS IDENTITY PRIMARY KEY,
film_name 		varchar NOT NULL,
rating 			varchar,
description 	varchar NOT NULL,
release_date 	date NOT NULL,
duration 		integer NOT NULL);

--добавить фильмы в таблицу films

INSERT INTO films
(
    film_name,
    rating,
    description,
    release_date,
    duration
)
VALUES
('name1', 'raiting1', 'description1', '1111-11-11', 1),
('name2', 'raiting2', 'description2', '2222-02-22', 2),
('name3', 'raiting3', 'description3', '3333-03-03', 3),
('name4', 'raiting4', 'description4', '4444-04-04', 4),
('name5', 'raiting5', 'description5', '5555-05-05', 5),
('name6', 'raiting6', 'description6', '6666-06-06', 6),
('name7', 'raiting7', 'description7', '7777-07-07', 7),
('name8', 'raiting8', 'description8', '8888-08-08', 8),
('name9', 'raiting9', 'description9', '9999-09-09', 9),
('name10', 'raiting10', 'description10', '1000-10-10', 10),
('name11', 'raiting11', 'description11', '1100-11-11', 11);

--создать таблицу с пользователями

CREATE TABLE users (id bigint GENERATED always AS IDENTITY PRIMARY KEY,
email varchar NOT NULL UNIQUE,
user_name varchar,
login varchar NOT NULL,
birthday date NOT NULL);

--добавить пользователей в таблицу users

INSERT INTO users
(
    email,
    user_name,
    birthday,
    login
)
VALUES
('email1', 'name1', '1111-11-11', 'login1'),
('email2', 'name2', '2222-02-22', 'login2'),
('email3', 'name3', '3333-03-03', 'login3'),
('email4', 'name4', '4444-04-04', 'login4');

--создать таблицу с жанрами

CREATE TABLE genre (id int GENERATED always AS IDENTITY PRIMARY KEY,
genre_name varchar);

--добавить жанры в таблицу genre

INSERT INTO genre (genre_name)
VALUES ('drama'),
('comedy');

--создать join-таблицу фильмов и жанров

CREATE TABLE films_genre (films_genre_key int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
film_id bigint REFERENCES films("id"),
genre_id integer REFERENCES genre("id"),
CONSTRAINT film_genre_unique unique (film_id, genre_id)
);

--заполнить join-таблицу фильмов и жанров

INSERT INTO films_genre
(
    film_id,
    genre_id
)
VALUES
(1, 2),
(2, 1),
(3, 2),
(4, 1),
(5, 2),
(6, 1),
(7, 2),
(8, 1),
(9, 2),
(10, 1),
(11, 2);

--создать join-таблицу фильмов и пользователей

CREATE TABLE films_users (films_users_key int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
film_id bigint REFERENCES films("id"),
user_id bigint REFERENCES users("id"),
CONSTRAINT one_user_one_like unique (film_id, user_id))
;

--заполнить join-таблицу фильмов и пользователей

INSERT INTO films_users
(
    film_id,
    user_id
)
VALUES
(1, 1),
(1, 2),
(2, 3),
(2, 4),
(3, 1),
(3, 2),
(4, 3),
(4, 4),
(5, 1),
(5, 2),
(6, 1),
(7, 2),
(8, 3),
(9, 4),
(10, 1),
(11, 2);

--создать таблицу отношений дружбы пользователей

CREATE TABLE friends (friends_key INT GENERATED ALWAYS AS IDENTITY,
user_id bigint REFERENCES users(id),
friend_id bigint REFERENCES users(id),
is_friendship_accepted boolean not null default false,
constraint f unique (user_id, friend_id));

drop table friends;

--заполнить таблицу отношений дружбы пользователей

INSERT INTO friends (user_id, friend_id, is_friendship_accepted)
values
(1,4, true),
(4,1, true),
(1,2, true),
(2,1, true),
(2,3, true),
(3,2, true),
(3,1, true),
(1,3, true),
(4,2, true),
(2,4, true);

--получить список всех фильмов с жанром и лайками

SELECT films.*,
genre.genre_name,
fu.user_id
FROM films
LEFT JOIN films_genre ON films.id=films_genre.film_id
left JOIN genre ON films_genre.genre_id=genre.id
left JOIN films_users fu ON films.id =fu.film_id ;

--получить список всех юзеров и их состояние дружбы

SELECT users.id,
users.user_name,
users.email,
users.birthday,
f.friend_id,
f.isfriendshipaccepted
FROM users
left JOIN friends f ON users.id = f.user_id ;

--получить список 5 популярных фильмов

SELECT film_id,
COUNT(user_id) AS like_number
FROM films_users
GROUP BY film_id
ORDER BY like_number DESC,
film_id
LIMIT 5 ;

--получить список общих друзей с другим пользователем

SELECT
f.friend_id AS common_friend
FROM friends f
JOIN friends f2 ON f.friend_id = f2.friend_id
WHERE f.is_friendship_accepted and f2.is_friendship_accepted and f.user_id = 1
AND f2.user_id = 3 ;

--получить список всех друзей пользователя
select friend_id
from friends f
where f.user_id = 1
;