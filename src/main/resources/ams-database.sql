-- CRUD - Create, Read, Update, Delete (soft/hard)
-- Sub-languages
	/*
	 * DDL - data definition language (structure/tables)
	 * 		- Keywords: create, alter, drop
	 * DML - data manipulation language (affecting the records in our tables)
	 * 		- Keywords: update, insert and delete (JDBC has ONE method for all 3 of these)
	 * DQL - data query language
	 * 		- Keywords: select
	 * TCL - Transaction Control Language
	 * 		- ***NOTE*** Often you relinquish this control to DBeaver & JDBC
	 * 		- Keywords: commit, rollback, savepoint
	 * DCL - Data Control Language
	 * 		- ***NOTE*** Rare to use, but can be helpful with security
	 * 		- Keywords: GRANT & REVOKE
	 */

-- shortcut: ctrl+enter will run a single SQL statement
-- drops
drop table flights;

-- CREATE - Keywords: create, insert, alter
create table flights(
	flight_number integer primary key check (flight_number > 1000 and flight_number < 999999),
	origin_airport varchar(3) not null, -- foreign keys, use alter
	destination_airport varchar(3) not null,
	time_departure timestamp,
	time_arrival timestamp,
	seat_count smallint not null check (seat_count > 0),
	pilot integer check (pilot > 100000 and pilot < 999999),
	airline integer
);


insert into 
	flights(flight_number, origin_airport, destination_airport, seat_count)
values (1234,'PHL','BOS',12);

insert into 
	flights(flight_number, origin_airport, destination_airport, seat_count)
values (123456,'PHL','BOS',12);

insert into 
	flights(flight_number, origin_airport, destination_airport, seat_count)
values (12345,'PHL','BOS',12);

-- READ
select * from flights;


create type member_enum as enum ('ADMIN', 'PILOT', 'PASSENGER');
-- New Table
create table members(
	member_id serial primary key, -- auto-incrementing number, also IDENTITY(1,10)
	first_name varchar(20),
	last_name varchar(40),
	email varchar(50) unique not null, -- candidate key - matches the constraint so fa primary key, but isn't the primary key
	member_type member_enum default 'PASSENGER',
	password varchar(64) not null -- should change to be encrypted via our API later
);

-- Establish the relationship between Flights & Members (PILOTS)
alter table flights
add constraint fk_pilot_member_id foreign key (pilot)
references members(member_id);


/*
 * MEMBER STATEMENTS BELOW
 */

insert into members
values (default, 'tommy', 'hoang', 'tommy@mail.com', default, 'superPass123');

insert into members(first_name, last_name, email, password)
values ('ruben', 'fitch', 'ruben@mail.com','superPass123!');

-- In steps database admin, adding a pilot & an admin

insert into members
values (123456, 'miguel', 'helguero', 'miguel@mail.com', 'PILOT', 'superPass123');

--delete from members where email = 'miguel@mail.com'; -- ctrl + / shortcut comment

select * from flights;

-- nested query or sub-query
update flights
set pilot = (
	select member_id from members
	where email = 'migeul@mail.com'
	and member_type = 'PILOT'
) where flight_number IN ( -- specifying a list, it will check it value
	select flight_number from flights
		where cast(flight_number as VARCHAR(6)) like '1234%' -- wildcard will select anything after
		and seat_count > 200
);

select flight_number from flights
where cast(flight_number as VARCHAR(6)) like '1234%' -- wildcard will select anything after
		and seat_count > 200;
	
select member_id from members
	where email = 'miguel@mail.com';

insert into members
values (default, 'admin', 'admin', 'admin3@mail.com', 'ADMIN', 'Pass123!');

select * from members;

/*
 * FLIGHT STATEMENTS BELOW
 */
insert into 
	flights(flight_number, origin_airport, destination_airport, seat_count)
values (1234,'PHL','BOS',1234);

insert into 
	flights(flight_number, origin_airport, destination_airport, seat_count)
values (123456,'PHL','BOS',14);

insert into 
	flights(flight_number, origin_airport, destination_airport, seat_count)
values (12345,'PHL','BOS',191);

-- multi-insert
insert into
	flights(flight_number, origin_airport, destination_airport, seat_count)
values (12346,'NYC','PHL',13),
	   (12347,'PHL','NYC',123),
	   (12348,'BOS','NYC',288),
	   (12349,'ATL','BOS',44),
	   (12340,'PHL','ATL',800)
;

insert into
	flights(flight_number, origin_airport, destination_airport, seat_count)
values (9346,'NYC','HOU',13),
	   (9347,'HOU','NYC',123),
	   (7348,'BOS','HOU',288),
	   (93849,'ATL','HOU',44),
	   (34340,'HOU','ATL',800)
;
-- READ
select * from flights; -- * acts as wildcard to select all columns
select flight_number, origin_airport, destination_airport, seat_count from flights;

select flight_number, origin_airport, destination_airport, seat_count 
from flights
where origin_airport = 'PHL';


-- UPDATE WHENVER you update make sure you have a WHERE clause
update flights
	set destination_airport= 'NYC'
	where flight_number = 1234;


select * from flights;

update flights
	set destination_airport= 'NWK'
	where cast(flight_number as VARCHAR(6)) like '12___' and origin_airport = 'PHL'; -- any number fits into _

update flights
	set destination_airport= 'HOU'
	where cast(flight_number as VARCHAR(6)) like '12___' 
		and origin_airport = 'PHL'
		and seat_count > 200;

update flights
	set destination_airport= 'DAL'
	where cast(flight_number as VARCHAR(6)) like '12___' 
		and origin_airport = 'PHL'
		and seat_count < 200;

select * from flights
where cast(flight_number as VARCHAR(6)) like '1234%' -- wildcard will select anything after
		and origin_airport = 'PHL'
		and seat_count < 200;
	

select * from flights
where cast(flight_number as VARCHAR(6)) like '%34%' -- wildcard will select anything after
		and seat_count > 200;
	
update flights
	set destination_airport= 'MIS'
	where cast(flight_number as VARCHAR(6)) like '123_'; -- any number fits into _

select * from flights;

select * from flights
	where seat_count  between 10 and 150;



-- DELETE ANYTIME you use a delete make sure you have a WHERE clause
delete from flights
	where flight_number = 123456;

select * from flights;

delete from flights
where seat_count between 10 and 150;

select * from flights;

-- specifically want to use TRUNCATE when we want to remove/delete all records in a table
-- truncate table flights;
-- select * from flights;

