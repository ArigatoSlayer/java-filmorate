MERGE INTO mpa_ratings (mpa_id, mpa_name) VALUES (1, 'G');
MERGE INTO mpa_ratings (mpa_id, mpa_name) VALUES (2, 'PG');
MERGE INTO mpa_ratings (mpa_id, mpa_name) VALUES (3, 'PG-13');
MERGE INTO mpa_ratings (mpa_id, mpa_name) VALUES (4, 'R');
MERGE INTO mpa_ratings (mpa_id, mpa_name) VALUES (5, 'NC-17');

MERGE INTO GENRE (genre_id, name) VALUES (1, 'Комедия');
MERGE INTO GENRE (genre_id, name) VALUES (2, 'Драма');
MERGE INTO GENRE (genre_id, name) VALUES (3, 'Мультфильм');
MERGE INTO GENRE (genre_id, name) VALUES (4, 'Триллер');
MERGE INTO GENRE (genre_id, name) VALUES (5, 'Документальный');
MERGE INTO GENRE (genre_id, name) VALUES (6, 'Боевик');

MERGE INTO event_type (type_id, type_name) VALUES (1, 'LIKE');
MERGE INTO event_type (type_id, type_name) VALUES (2, 'REVIEW');
MERGE INTO event_type (type_id, type_name) VALUES (3, 'FRIEND');

MERGE INTO type_operation (operation_id, operation_name) VALUES (1, 'REMOVE');
MERGE INTO type_operation (operation_id, operation_name) VALUES (2, 'ADD');
MERGE INTO type_operation (operation_id, operation_name) VALUES (3, 'UPDATE');