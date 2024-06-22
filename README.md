# java-filmorate
Template repository for Filmorate project.
![The database scheme](bd_scheme.png)
Основные таблицы: films и users.\
films связана отношением many-to-many с таблицей genre посредством join-таблицы films_genre\
C users таблица films связана отношением many-to-many посредством join-таблицы films_users\
Друзья пользователей хранятся в таблица friends. Она связана с users отношением one-to-many