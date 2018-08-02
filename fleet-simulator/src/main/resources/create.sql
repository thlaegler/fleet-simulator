
-- CREATE DATABASE gtfs;
-- CREATE USER fleetsimulator PASSWORD '7f512a41a3506989d1233017f624b35c0fa70bae';
-- ALTER USER fleetsimulator WITH SUPERUSER;
-- GRANT ALL PRIVILEGES ON DATABASE gtfs TO fleetsimulator;
CREATE SCHEMA IF NOT EXISTS GTFS_NAME;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA GTFS_NAME TO fleetsimulator;



DROP TABLE IF EXISTS GTFS_NAME.agency CASCADE;
CREATE TABLE IF NOT EXISTS GTFS_NAME.agency (
	agency_id varchar(255) NOT NULL,
	fare_url varchar(255) NULL,
	agency_lang varchar(255) NULL,
	agency_name varchar(255) NOT NULL,
	agency_phone varchar(255) NULL,
	agency_timezone varchar(255) NOT NULL,
	agency_url varchar(255) NOT NULL,
	CONSTRAINT agency_pkey PRIMARY KEY (agency_id)
)
WITH (
	OIDS=FALSE
) ;


DROP TABLE IF EXISTS GTFS_NAME.routes CASCADE;
CREATE TABLE IF NOT EXISTS GTFS_NAME.routes (
	route_id varchar(255) NOT NULL,
	route_desc varchar(255) NULL,
	hex_path_color varchar(255) NULL,
	hex_text_color varchar(255) NULL,
	route_long_name varchar(255) NOT NULL,
	route_short_name varchar(255) NOT NULL,
	route_type int4 NULL,
	url varchar(255) NULL,
	agency_id varchar(255) NOT NULL,
	CONSTRAINT routes_pkey PRIMARY KEY (route_id),
	CONSTRAINT fkdas5s68f11p6gw6k5k82qaheo FOREIGN KEY (agency_id) REFERENCES GTFS_NAME.agency(agency_id)
)
WITH (
	OIDS=FALSE
) ;


DROP TABLE IF EXISTS GTFS_NAME.fare_rules CASCADE;
CREATE TABLE IF NOT EXISTS GTFS_NAME.fare_rules (
	fare_id varchar(255) NOT NULL,
	contains_zone_id varchar(255) NULL,
	destination_zone_id varchar(255) NULL,
	origin_zone_id varchar(255) NULL,
	route_id varchar(255) NOT NULL,
	CONSTRAINT fare_rules_pkey PRIMARY KEY (fare_id),
	CONSTRAINT fkm52rb0413cpdjsd1gwj2mgsg8 FOREIGN KEY (route_id) REFERENCES GTFS_NAME.routes(route_id)
)
WITH (
	OIDS=FALSE
) ;


DROP TABLE IF EXISTS GTFS_NAME.fare_attributes CASCADE;
CREATE TABLE IF NOT EXISTS GTFS_NAME.fare_attributes (
	fare_id varchar(255) NOT NULL,
	currency_type int4 NULL,
	payment_type int4 NULL,
	price float8 NOT NULL,
	transfer_duration float8 NULL,
	transfer_type int4 NULL,
	CONSTRAINT fare_attributes_pkey PRIMARY KEY (fare_id)
)
WITH (
	OIDS=FALSE
) ;


DROP TABLE IF EXISTS GTFS_NAME.stops CASCADE;
CREATE TABLE IF NOT EXISTS GTFS_NAME.stops (
	stop_id varchar(255) NOT NULL,
	stop_code varchar(255) NULL,
	stop_name varchar(255) NOT NULL,
	stop_desc varchar(255) NULL,
	stop_lat float8 NULL,
	stop_lon float8 NULL,
	zone_id varchar(255) NULL,
	stop_url varchar(255) NULL,
	location_type int4 NULL,
	parent_station int4 NULL,
	time_zone varchar(255) NULL,
	wheelchair_type int4 NULL,
	platform_code varchar(255) NULL,
	CONSTRAINT stops_pkey PRIMARY KEY (stop_id)
)
WITH (
	OIDS=FALSE
) ;


DROP TABLE IF EXISTS GTFS_NAME.shapes CASCADE;
CREATE TABLE IF NOT EXISTS GTFS_NAME.shapes (
	shape_unique_id serial NOT NULL,
	shape_dist_traveled float8 NULL,
	shape_pt_lat float8 NULL,
	shape_pt_lon float8 NULL,
	shape_pt_sequence int8 NULL,
	shape_id varchar(255) NOT NULL,
	CONSTRAINT shapes_pkey PRIMARY KEY (shape_unique_id)
)
WITH (
	OIDS=FALSE
) ;


DROP TABLE IF EXISTS GTFS_NAME.feed_info CASCADE;
CREATE TABLE IF NOT EXISTS GTFS_NAME.feed_info (
	feed_publisher_name varchar(255) NOT NULL,
	feed_end_date varchar(255) NULL,
	feed_lang varchar(255) NOT NULL,
	feed_publisher_url varchar(255) NOT NULL,
	feed_start_date varchar(255) NULL,
	feed_version varchar(255) NULL,
	CONSTRAINT feed_info_pkey PRIMARY KEY (feed_publisher_name)
)
WITH (
	OIDS=FALSE
) ;


DROP TABLE IF EXISTS GTFS_NAME.calendar CASCADE;
CREATE TABLE IF NOT EXISTS GTFS_NAME.calendar (
	service_id varchar(255) NOT NULL,
	end_date varchar(255) NULL,
	friday bool NULL,
	monday bool NULL,
	saturday bool NULL,
	start_date varchar(255) NULL,
	sunday bool NULL,
	thursday bool NULL,
	tuesday bool NULL,
	wednesday bool NULL,
	CONSTRAINT calendar_pkey PRIMARY KEY (service_id)
)
WITH (
	OIDS=FALSE
) ;


DROP TABLE IF EXISTS GTFS_NAME.calendar_dates CASCADE;
CREATE TABLE IF NOT EXISTS GTFS_NAME.calendar_dates (
	calendar_date_id varchar(255) NOT NULL,
	"date" varchar(255) NOT NULL,
	exception_type int4 NULL,
	service_id varchar(255) NULL,
	CONSTRAINT calendar_dates_pkey PRIMARY KEY (calendar_date_id),
	CONSTRAINT fkrc0bciwewbr0cjmjq0cl0ofwy FOREIGN KEY (service_id) REFERENCES GTFS_NAME.calendar(service_id)
)
WITH (
	OIDS=FALSE
) ;


DROP TABLE IF EXISTS GTFS_NAME.trips CASCADE;
CREATE TABLE IF NOT EXISTS GTFS_NAME.trips (
	trip_id varchar(255) NOT NULL,
	block_id varchar(255) NULL,
	direction_type int4 NULL,
	trip_headsign varchar(255) NULL,
	short_name varchar(255) NULL,
	wheelchair_type int4 NULL,
	route_id varchar(255) NULL,
	service_id varchar(255) NULL,
	shape_id int4 NULL,
	CONSTRAINT trips_pkey PRIMARY KEY (trip_id),
	CONSTRAINT fk4x1bnuj57j8vbbesao0l73mr7 FOREIGN KEY (service_id) REFERENCES GTFS_NAME.calendar(service_id),
	CONSTRAINT fk6b4sfhg7rp2e7h090i0v6jnm8 FOREIGN KEY (shape_id) REFERENCES GTFS_NAME.shapes(shape_unique_id),
	CONSTRAINT fkm7ci3blm9wj2k0d94chu18y7s FOREIGN KEY (route_id) REFERENCES GTFS_NAME.routes(route_id)
)
WITH (
	OIDS=FALSE
) ;


DROP TABLE IF EXISTS GTFS_NAME.frequencies CASCADE;
CREATE TABLE IF NOT EXISTS GTFS_NAME.frequencies (
	id bigserial NOT NULL,
	end_time time NOT NULL,
	exact_time int4 NULL,
	headway_secs int8 NULL,
	start_time time NOT NULL,
	trip_id varchar(255) NOT NULL,
	CONSTRAINT frequencies_pkey PRIMARY KEY (id),
	CONSTRAINT fk7n8lkn4e85f0f1t0wj4mbvv0r FOREIGN KEY (trip_id) REFERENCES GTFS_NAME.trips(trip_id)
)
WITH (
	OIDS=FALSE
) ;


DROP TABLE IF EXISTS GTFS_NAME.stop_times CASCADE;
CREATE TABLE IF NOT EXISTS GTFS_NAME.stop_times (
	stop_time_id varchar(255) NOT NULL,
	arrival_time time NOT NULL,
	departure_time time NOT NULL,
	drop_off_type int4 NULL,
	pickup_type int4 NULL,
	shape_distance_traveled float8 NULL,
	stop_headsign varchar(255) NULL,
	stop_sequence int4 NULL,
	stop_id varchar(255) NULL,
	trip_id varchar(255) NULL,
	CONSTRAINT stop_times_pkey PRIMARY KEY (stop_time_id),
	CONSTRAINT fkgciqxg4foq54nv2e25nxe1wrp FOREIGN KEY (stop_id) REFERENCES GTFS_NAME.stops(stop_id),
	CONSTRAINT fkh883f08us95bsdycf6ll8e0g1 FOREIGN KEY (trip_id) REFERENCES GTFS_NAME.trips(trip_id)
)
WITH (
	OIDS=FALSE
) ;

DROP TABLE IF EXISTS GTFS_NAME.transfer CASCADE;
CREATE TABLE IF NOT EXISTS GTFS_NAME.transfer (
	transfer_id varchar(255) NOT NULL,
	min_transfer int8 NULL,
	transfer_type int4 NULL,
	from_stop_id varchar(255) NULL,
	to_stop_id varchar(255) NULL,
	CONSTRAINT transfers_pkey PRIMARY KEY (transfer_id),
	CONSTRAINT fkajmd9smbgtl6c63askyxafdck FOREIGN KEY (to_stop_id) REFERENCES GTFS_NAME.stops(stop_id),
	CONSTRAINT fkpmvnub08qrhut68bdrbwaff2k FOREIGN KEY (from_stop_id) REFERENCES GTFS_NAME.stops(stop_id)
)
WITH (
	OIDS=FALSE
) ;


COPY GTFS_NAME.agency(AGENCY_COLUMNS) FROM 'GTFS_FOLDER/GTFS_NAME/gtfs/agency.txt' DELIMITER ',' CSV HEADER;

COPY GTFS_NAME.routes(ROUTES_COLUMNS) FROM 'GTFS_FOLDER/GTFS_NAME/gtfs/routes.txt' DELIMITER ',' CSV HEADER;

COPY GTFS_NAME.fare_rules(FARE_RULES_COLUMNS) FROM 'GTFS_FOLDER/GTFS_NAME/gtfs/fare_rules.txt' DELIMITER ',' CSV HEADER;

COPY GTFS_NAME.fare_attributes(FARE_ATTRIBUTES) FROM 'GTFS_FOLDER/GTFS_NAME/gtfs/fare_attributes.txt' DELIMITER ',' CSV HEADER;

COPY GTFS_NAME.stops(STOPS) FROM 'GTFS_FOLDER/GTFS_NAME/gtfs/stops.txt' DELIMITER ',' CSV HEADER;

COPY GTFS_NAME.shapes(shape_dist_traveled, shape_pt_lat, shape_pt_lon, shape_pt_sequence, shape_id) FROM 'GTFS_FOLDER/GTFS_NAME/gtfs/shapes.txt' DELIMITER ',' CSV HEADER;

COPY GTFS_NAME.frequencies(end_time, exact_time, headway_secs, start_time, trip_id) FROM 'GTFS_FOLDER/GTFS_NAME/gtfs/frequencies.txt' DELIMITER ',' CSV HEADER;

COPY GTFS_NAME.feed_info(feed_publisher_name, feed_end_date, feed_lang, feed_publisher_url, feed_start_date, feed_version) FROM 'GTFS_FOLDER/GTFS_NAME/gtfs/feed_info.txt' DELIMITER ',' CSV HEADER;

COPY GTFS_NAME.calendar(service_id, end_date, friday, monday, saturday, start_date, sunday, thursday, tuesday, wednesday) FROM 'GTFS_FOLDER/GTFS_NAME/gtfs/calendar.txt' DELIMITER ',' CSV HEADER;

COPY GTFS_NAME.calendar_dates(service_id,date,exception_type) FROM 'GTFS_FOLDER/GTFS_NAME/gtfs/calendar_dates.txt' DELIMITER ',' CSV HEADER;

COPY GTFS_NAME.trips(trip_id, block_id, direction_type, trip_headsign, short_name, wheelchair_type, route_id, service_id, shape_id) FROM 'GTFS_FOLDER/GTFS_NAME/gtfs/trips.txt' DELIMITER ',' CSV HEADER;

COPY GTFS_NAME.stop_times(trip_id,arrival_time,departure_time,stop_id,stop_sequence,stop_headsign,pickup_type,drop_off_type,shape_dist_traveled) FROM 'GTFS_FOLDER/GTFS_NAME/gtfs/stop_times.txt' DELIMITER ',' CSV HEADER;

COPY GTFS_NAME.transfer(transfer_id, min_transfer, transfer_type, from_stop_id, to_stop_id) FROM 'GTFS_FOLDER/GTFS_NAME/gtfs/transfer.txt' DELIMITER ',' CSV HEADER;
