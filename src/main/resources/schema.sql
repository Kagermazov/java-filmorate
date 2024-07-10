--create a film table
create table IF NOT EXISTS films (id bigint GENERATED always AS IDENTITY PRIMARY KEY,
film_name 		varchar NOT NULL,
rating 			varchar,
description 	varchar NOT NULL,
release_date 	date NOT NULL,
duration 		integer NOT NULL);

--create a user table
create table IF NOT EXISTS users (id bigint GENERATED always AS IDENTITY PRIMARY KEY,
email varchar NOT NULL UNIQUE,
user_name varchar,
login varchar NOT NULL,
birthday date NOT NULL);

--create a genre table
create table IF NOT EXISTS genre (id int GENERATED always AS IDENTITY PRIMARY KEY,
genre_name varchar);

--create an join table films_genre
create table films_genre (films_genre_key int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
film_id bigint REFERENCES films("id"),
genre_id integer REFERENCES genre("id"),
CONSTRAINT film_genre_unique unique (film_id, genre_id)
);

--create an join table films_users
create table IF NOT EXISTS films_users (films_users_key int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
film_id bigint REFERENCES films("id"),
user_id bigint REFERENCES users("id"),
CONSTRAINT one_user_one_like unique (film_id, user_id))
;

--create a friendship table
create table IF NOT EXISTS friends (friends_key int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
user_id bigint REFERENCES users(id),
friend_id bigint REFERENCES users(id),
constraint friendship_unique unique (user_id, friend_id));