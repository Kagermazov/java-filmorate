--populate a films table
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

--populate an users table
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

--populate an genre table
INSERT INTO genre (genre_name)
VALUES ('drama'),
('comedy');

--populate an join table films_genre
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

--populate an join table films_users
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

--populate an join table friends
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