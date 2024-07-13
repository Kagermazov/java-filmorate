--DROP TABLE IF EXISTS FILMS_GENRE;
--DROP TABLE IF EXISTS FILMS_USERS;
--DROP TABLE IF EXISTS FILMS_MPA;
--DROP TABLE IF EXISTS FRIENDS;
--DROP TABLE IF EXISTS FILMS;
--DROP TABLE IF EXISTS MPA;
--DROP TABLE IF EXISTS USERS;
--DROP TABLE IF EXISTS GENRE;

--create an mpa table
CREATE TABLE IF NOT EXISTS MPA (id bigint PRIMARY KEY,
mpa_name varchar);

--create a films table
CREATE TABLE IF NOT EXISTS FILMS (id bigint generated always AS IDENTITY PRIMARY KEY,
film_name varchar NOT NULL,
rating bigint REFERENCES mpa(id),
description varchar NOT NULL,
release_date date NOT NULL,
duration integer NOT NULL);

--create a users table
CREATE TABLE IF NOT EXISTS USERS (id bigint generated always AS IDENTITY PRIMARY KEY,
email varchar NOT NULL UNIQUE,
user_name varchar,
login varchar NOT NULL,
birthday date NOT NULL);

--create a genre table
CREATE TABLE IF NOT EXISTS GENRE (id bigint PRIMARY KEY,
genre_name varchar);

--create an join table films_genre
CREATE TABLE IF NOT EXISTS FILMS_GENRE (films_genre_key bigint generated always AS IDENTITY PRIMARY KEY,
film_id bigint REFERENCES films(id),
genre_id integer REFERENCES genre(id),
CONSTRAINT film_genre_unique UNIQUE (film_id, genre_id));

--create an join table films_mpa
CREATE TABLE IF NOT EXISTS FILMS_MPA (films_mpa_key bigint generated always AS IDENTITY PRIMARY KEY,
film_id bigint REFERENCES films(id),
mpa_id integer REFERENCES mpa(id),
CONSTRAINT film_mpa_unique UNIQUE (film_id, mpa_id));

--create an join table films_users
CREATE TABLE IF NOT EXISTS FILMS_USERS (FILMS_USERS_key bigint generated always AS IDENTITY PRIMARY KEY,
film_id bigint REFERENCES films(id),
user_id bigint REFERENCES users(id),
CONSTRAINT one_user_one_like UNIQUE (film_id, user_id)) ;

--create a friendship table
CREATE TABLE IF NOT EXISTS FRIENDS (friends_key bigint generated always AS IDENTITY PRIMARY KEY,
user_id bigint REFERENCES users(id),
friend_id bigint REFERENCES users(id),
CONSTRAINT friendship_unique UNIQUE (user_id, friend_id));