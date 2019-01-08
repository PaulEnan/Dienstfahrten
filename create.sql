PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE names (id integer primary key, name text, last_name text);

CREATE TABLE adress (id integer primary key, street text, number integer, place text, postalcode integer);

CREATE TABLE costs(id integer primary key, amount integer, sleep_costs integer, food_costs integer, trip_extra_sosts integer);

CREATE TABLE destination (id integer primary key, destination text, adress_id integer,
foreign key(adress_id) references adress(id));

CREATE TABLE main(id integer primary key, ocassion text, dest_id integer, duration integer, date date, costs_id integer,foreign key (dest_id) references destination(id),
foreign key (costs_id) references costs(id));

CREATE TABLE dest_name_match (dest_id integer, name_id integer, foreign key(dest_id) references destination(id), foreign key (name_id) references names(id));

COMMIT;

