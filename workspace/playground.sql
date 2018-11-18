-- GATE_ACTIVITY
insert into GATE_ACTIVITY (GATE_ID, car_enter_timestamp) values (1, sysdate);
-- Entering times
select to_char(car_enter_timestamp,'DD/MM/YYYY HH24:MI') from gate_activity where car_enter_timestamp is not null;
-- Exiting times
select to_char(car_exit_timestamp,'DD/MM/YYYY HH24:MI') from gate_activity where car_exit_timestamp is not null;

--PARKING_SPOT_STATE
insert into PARKING_SPOT_STATE values (sysdate, 'ParkingSpot1', 7.65555555555555555, 1);
select to_char(PARKING_START_TIMESTAMP,'DD/MM/YYYY HH24:MI'), PARKING_SPOT_ID, MINUTS_OCCUPIED, IS_CONTINUED from PARKING_SPOT_STATE ;
select to_char(PARKING_START_TIMESTAMP,'DD/MM/YYYY HH24:MI'), PARKING_SPOT_ID, MINUTS_OCCUPIED, IS_CONTINUED from PARKING_SPOT_STATE where IS_CONTINUED = 0 order by PARKING_START_TIMESTAMP;
select avg(MINUTS_OCCUPIED) from PARKING_SPOT_STATE where to_char(PARKING_START_TIMESTAMP,'DD/MM/YYYY HH24:MI') between '01/06/2018 09:00' and '01/06/2018 10:00';
select to_char(PARKING_START_TIMESTAMP,'DD/MM/YYYY HH24:MI'), PARKING_SPOT_ID, MINUTS_OCCUPIED, IS_CONTINUED from PARKING_SPOT_STATE where IS_CONTINUED = 0 and PARKING_SPOT_ID = 'ParkingSpot6';

-- INTERVALS
select to_char(PARKING_START_TIMESTAMP,'DD/MM/YYYY HH24:MI'), DURATION, RATE_ENTER, RATE_EXIT from INTERVALS ;
insert into intervals values (sysdate, 10, 1,1, interval_id_seq.nextval);

--PARKING_SPOT
update PARKING_SPOT set IS_ACTIVE = 0 where PARKING_SPOT_ID = 'ParkingSpot2';
select * from PARKING_SPOT where PARKING_SPOT_ID = 'ParkingSpot2';

-- AFEKA_PL_DATA
select to_char(GATE_PASS_TIME,'DD/MM/YYYY HH24:MI'), ALT_LICENSE_NUMBER, REASON 
from AFEKA_PL_DATA 
where (to_char(GATE_PASS_TIME,'DD/MM/YYYY HH24:MI') between '01/01/2018 06:00' and '01/01/2018 23:59'
    and REASON in ('Standard entrance', 'Standard exit') )
order by ALT_LICENSE_NUMBER, GATE_PASS_TIME;