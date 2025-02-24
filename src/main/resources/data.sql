merge into MPA (ID,NAME) KEY(name) VALUES (1, 'G' );
merge into MPA (ID,NAME) KEY(name) values (2, 'PG' );
merge into MPA (ID,NAME) KEY(name) values (3, 'PG-13' );
merge into MPA (ID,NAME) KEY(name) values (4, 'R' );
merge into MPA (ID,NAME) KEY(name) values (5, 'NC-17' );

merge into GENRES (ID,NAME) KEY(NAME) values (1, 'Комедия' ) ;
merge into GENRES (ID,name) KEY(name) values (2, 'Драма' ) ;
merge into GENRES (ID,name) KEY(name) values (3, 'Мультфильм' ) ;
merge into GENRES (ID,name) KEY(name) values (4, 'Триллер' ) ;
merge into GENRES (id, name) key(name) values ( 5, 'Документальный');
merge into GENRES (id, name) key(name) values ( 6, 'Боевик');