--  DATABASE CREATION FILE
--  created by: Dvir Twina.

-- Tables creation
create table GATE_ACTIVITY (
    gate_id int not null,
    car_enter_timestamp date,
    car_exit_timestamp date
    );
    
create table PARKING_SPOT (
    parking_spot_id varchar2(35) not null PRIMARY KEY, 
    area VARCHAR2(20), 
    is_handicapped number(1), -- 0: False, 1: True
    is_active number(1) -- 0: False, 1: True
    );
        
create table INTERVALS (
    parking_start_timestamp date not null,
    duration int not null,
    rate_enter number not null,
    rate_exit number not null,
    interval_id int not null PRIMARY KEY
    );
    
create table PARKING_SPOT_STATE (
    parking_start_timestamp date not null, -- This attribute is NOT designated as primary key to be able to insert more than 1 record with the same enter/exit time to/from different parking spots. 
    parking_spot_id REFERENCES PARKING_SPOT(parking_spot_id) not null,
    minuts_occupied number not null,
    is_continued number(1), -- 0: False, 1: True
    interval_id REFERENCES INTERVALS(interval_id),
    
    CONSTRAINT PARKING_SPOT_STATE_tbl_unique UNIQUE (parking_start_timestamp, parking_spot_id) -- To prevent more than 1 car from being in the same parking spot in the same time.
    );
    
create table USERS (
    username VARCHAR2(30),
    password VARCHAR2(20),
    role VARCHAR2(20),
    
    CONSTRAINT USERS_tbl_unique UNIQUE (username)
    );

-- Sequences
CREATE SEQUENCE interval_id_seq
START WITH 1
INCREMENT BY 1
NOCYCLE
NOCACHE;
    
insert into PARKING_SPOT VALUES ('ParkingSpot1', 'W', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot2', 'W', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot3', 'W', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot4', 'W', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot5', 'W', 0, 0);

insert into PARKING_SPOT VALUES ('ParkingSpot6', 'W', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot7', 'W', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot8', 'W', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot9', 'W', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot10', 'W', 0, 0);

insert into PARKING_SPOT VALUES ('ParkingSpot11', 'E', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot12', 'E', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot13', 'E', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot14', 'E', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot15', 'E', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot16', 'E', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot17', 'E', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot18', 'E', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot19', 'E', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot20', 'E', 0, 0);

insert into PARKING_SPOT VALUES ('ParkingSpot21', 'S', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot22', 'S', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot23', 'S', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot24', 'S', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot25', 'S', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot26', 'S', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot27', 'S', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot28', 'S', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot29', 'S', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot30', 'S', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot31', 'S', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot32', 'S', 0, 0);

insert into PARKING_SPOT VALUES ('ParkingSpot33', 'N', 1, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot34', 'N', 1, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot35', 'N', 1, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot36', 'N', 1, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot37', 'N', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot38', 'N', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot39', 'N', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot40', 'N', 0, 0);

insert into PARKING_SPOT VALUES ('ParkingSpot41', 'M', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot42', 'M', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot43', 'M', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot44', 'M', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot45', 'M', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot46', 'M', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot47', 'M', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot48', 'M', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot49', 'M', 0, 0);
insert into PARKING_SPOT VALUES ('ParkingSpot50', 'M', 0, 0);

insert into USERS VALUES ('admin', 'adminpass', 'Administrator');
insert into USERS VALUES ('user', 'userpass', 'User');

-- PL/SQL
    
create or replace trigger CAR_EXIT
after insert on PARKING_SPOT_STATE
for each row
    DECLARE
    parking_start_timestamp date;
    minuts_occupied number;
    parking_end_timestamp date;
    BEGIN
    parking_start_timestamp := :new.parking_start_timestamp;
    minuts_occupied := :new.minuts_occupied;
    parking_end_timestamp := parking_start_timestamp + interval '1' minute *minuts_occupied;

    insert into GATE_ACTIVITY (GATE_ID, CAR_EXIT_TIMESTAMP) values (1, parking_end_timestamp);
    END;

--COMMIT;