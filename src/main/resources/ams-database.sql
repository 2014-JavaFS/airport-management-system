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
drop table members;
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

create type member_enum as enum ('ADMIN', 'PILOT', 'PASSENGER');
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

insert into members(first_name, last_name, email, member_type, password)
values ('ruben', 'fitch', 'ruben@mail.com','PILOT', 'superPass123!');

-- In steps database admin, adding a pilot & an admin

insert into members
values (123456, 'miguel', 'helguero', 'miguel@mail.com', 'PILOT', 'superPass123');

--delete from members where email = 'miguel@mail.com'; -- ctrl + / shortcut comment
select * from members;
update members
set first_name = 'BLARGH', last_name = 'HGRALB', email = 'blargh@mail.com', password = 'blargh!'
where member_id = 43; 

-- nested query or sub-query
update flights
set pilot = (
	select member_id from members
	where email = 'miguel@mail.com'
	and member_type = 'PILOT'
) where flight_number IN ( -- specifying a list, it will check it value
	select flight_number from flights
		where seat_count > 200
);
delete from members 
where email = 'tester@mail.com';
select member_id from members
	where email = 'miguel@mail.com'
	and member_type = 'PILOT';

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
values (32346,'NYC','PHL',13),
	   (32347,'PHL','NYC',12),
	   (32348,'BOS','NYC',28),
	   (32349,'ATL','BOS',44),
	   (32340,'PHL','ATL',80)
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

-- JOINS & aliases tables
select  f.*, m.member_id, m.first_name, m.last_name 
from flights f
inner join members m on f.pilot = m.member_id
where m.member_id = 123456;

-- Aggregate Functions & Aliases (columns)
select origin_airport, count(*) as flight_count
from flights
group by origin_airport 
order by flight_count DESC;

-- Top 2 destinations
select destination_airport, count(*) as flight_count
from flights
group by destination_airport 
order by flight_count desc
limit 2;

-- 3rd highest ranking destination
select destination_airport, count(*) as flight_count
from flights
group by destination_airport 
order by flight_count desc
offset 2
limit 1;

select count(*) as total_flights from flights;

-- scalar functions apply to every single value in a result set
select *, substring(first_name, 1, 3) as first_name from members;

select member_id, length(first_name) as length_fname
from members 
order by length_fname desc;

select *, now() 
from members;

select time_arrival
from flights f 
where time_arrival notnull
and time_arrival > current_timestamp;

select *
from flights f 
where time_departure > current_timestamp;

UPDATE flights
SET time_departure = current_timestamp + interval '20 day'
where time_departure is null;

UPDATE flights
SET time_arrival = current_timestamp + interval '20 day' + interval '6 hour'
where time_arrival is null;

select * from flights;

-- Advanced SQL Topics
create view active_flights as
select *
from flights f 
where time_departure > current_timestamp;

create view inactive_flights as
select *
from flights f 
where time_departure < current_timestamp;

-- create or replace will overwrite if the view already exist
create or replace view active_flights as
select f.*, email, last_name
from flights f 
inner join members m on m.member_id = f.pilot
where time_departure > current_timestamp;

select * from active_flights;
select * from inactive_flights;

-- Functions - they always return some value, new.column_name indicates the new record being added
create or replace function generate_pilot_id2()
	returns trigger
	language plpgsql
	as $$
declare 
	new_id integer; -- variable
begin 
	select random()*1000000 into new_id;
	if new.member_type = 'PILOT' then 
		new.member_id := new_id; -- := is reassignment, because = is value evaluator
	end if;
	return new;
end; $$

-- Trigger
create or replace trigger assign_pilot_id 
before insert -- occurs before every insert into the members tables
on members
for each row -- will apply to multiple rows, other option would be statement
execute function generate_pilot_id2(); -- executes our above function

insert into members(first_name, last_name, email, member_type, "password")
values 
('david', 'jeske', 'david2@mail.com', 'PILOT', 'superPass123'),
('asaf', 'ahmed', 'asaf2@mail.com', 'PILOT', 'superPass123'),
('callie', 'williams', 'callie2@mail.com', 'PILOT', 'superPass123'),
('karlas', 'gonzalez', 'karla2@mail.com', 'PILOT', 'superPass123')
;

select * from members;
select * from flights;
-- Procedure helps replace & automate some of the more verbose commands
create or replace procedure update_flight(
	in p_flight_number int,
	in p_origin_airport VARCHAR(3),
	in p_destination_airport VARCHAR(3),
	in p_time_departure timestamp,
	in p_time_arrival timestamp,
	in p_seat_count smallint,
	in p_pilot int,
	in p_airline int,
	inout updated_rows int
)
language plpgsql
as $$
declare
	_row_count int = 0;
begin
	update flights 
	set origin_airport = p_origin_airport, 
		destination_airport = p_destination_airport,
		time_departure = p_time_departure,
		time_arrival = p_time_arrival,
		seat_count = p_seat_count,
		pilot = p_pilot,
		airline = p_airline
	where flight_number = p_flight_number;	
	get diagnostics updated_rows := row_count;

end;
$$;

select * from flights where flight_number = 777778;
call update_flight(77778, 
'HOU',
'PHL', 
(current_timestamp + interval '1 day')::timestamp, 
(current_timestamp + interval '2 day')::timestamp, 
145::smallint, 
123456, 
4567,
0);
 select * from members m ;

-- BOOKINGS
drop table bookings on delete cascade;
create type seat_enum as enum ('SEATSOPTIONAL', 'ECONOMY', 'BUSINESS', 'FIRSTCLASS');
CREATE TABLE bookings (
  booking_id SERIAL PRIMARY KEY,
  flight_number int NOT NULL,
  member_id INT NOT NULL,
  seat_type seat_enum default 'SEATSOPTIONAL',
  carry_on_allowed boolean,
  checked_luggage SMALLINT,
  price DECIMAL(10,2) NOT NULL,
  FOREIGN KEY (flight_number) REFERENCES flights(flight_number),  -- Assuming flights table exists
  FOREIGN KEY (member_id) REFERENCES members(member_id)  -- Assuming members table exists
);
select * from bookings ;
-- Sample data (replace with your actual data)
INSERT INTO bookings (flight_number, member_id, seat_type, checked_luggage, carry_on_allowed, price)
VALUES (777777, 1, 'ECONOMY', 1, false, 280.00),  
       (555555, 2, 'BUSINESS', 3, true, 720.00),  
       (888888, 48, 'FIRSTCLASS', 0, true, 1100.00); 
      
select * from bookings b 
join flights f on f.flight_number = b.flight_number
where b.member_id = 48; 

create or replace function generate_carry_on()
	returns trigger
	language plpgsql
	as $$
begin 
	if new.seat_type = 'SEATSOPTIONAL' or new.seat_type = 'ECONOMY' then
		new.carry_on_allowed := false; -- := is reassignment, because = is value evaluator
	else 
		new.carry_on_allowed := true;	
	end if;
	return new;
end; 
$$

create or replace trigger assign_carry_on 
before insert 
on bookings
for each row
execute function generate_carry_on();

select * from bookings ;



