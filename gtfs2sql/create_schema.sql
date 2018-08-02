
CREATE SCHEMA IF NOT EXISTS GTFS_NAME;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA GTFS_NAME TO fleetsimulator;

DROP TABLE IF EXISTS GTFS_NAME.transfer CASCADE;
DROP TABLE IF EXISTS GTFS_NAME.stop_times CASCADE;
DROP TABLE IF EXISTS GTFS_NAME.frequencies CASCADE;
DROP TABLE IF EXISTS GTFS_NAME.trips CASCADE;
DROP TABLE IF EXISTS GTFS_NAME.calendar_dates CASCADE;
DROP TABLE IF EXISTS GTFS_NAME.calendar CASCADE;
DROP TABLE IF EXISTS GTFS_NAME.feed_info CASCADE;
DROP TABLE IF EXISTS GTFS_NAME.shapes CASCADE;
DROP TABLE IF EXISTS GTFS_NAME.stops CASCADE;
DROP TABLE IF EXISTS GTFS_NAME.fare_attributes CASCADE;
DROP TABLE IF EXISTS GTFS_NAME.fare_rules CASCADE;
DROP TABLE IF EXISTS GTFS_NAME.routes CASCADE;
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

CREATE TABLE IF NOT EXISTS GTFS_NAME.routes (
	route_id varchar(255) NOT NULL,
	route_desc varchar(255) NULL,
	route_color varchar(255) NULL,
	route_text_color varchar(255) NULL,
	route_long_name varchar(255) NULL,
	route_short_name varchar(255) NULL,
	route_type int4 NULL,
	route_url varchar(255) NULL,
	agency_id varchar(255) NULL,
	CONSTRAINT routes_pkey PRIMARY KEY (route_id),
	CONSTRAINT fk_GTFS_NAME_routes_agency FOREIGN KEY (agency_id) REFERENCES GTFS_NAME.agency(agency_id) NOT VALID
)
WITH (
	OIDS=FALSE
) ;

CREATE TABLE IF NOT EXISTS GTFS_NAME.fare_rules (
	fare_id varchar(255) NOT NULL,
	contains_zone_id varchar(255) NULL,
	destination_zone_id varchar(255) NULL,
	origin_zone_id varchar(255) NULL,
	route_id varchar(255) NULL,
	CONSTRAINT fare_rules_pkey PRIMARY KEY (fare_id),
	CONSTRAINT fk_GTFS_NAME_fare_rules_route FOREIGN KEY (route_id) REFERENCES GTFS_NAME.routes(route_id) NOT VALID
)
WITH (
	OIDS=FALSE
) ;

CREATE TABLE IF NOT EXISTS GTFS_NAME.fare_attributes (
	fare_id varchar(255) NOT NULL,
	currency_type int4 NULL,
	payment_type int4 NULL,
	price float8 NULL,
	transfer_duration float8 NULL,
	transfer_type int4 NULL,
	CONSTRAINT fare_attributes_pkey PRIMARY KEY (fare_id)
)
WITH (
	OIDS=FALSE
) ;

CREATE TABLE IF NOT EXISTS GTFS_NAME.stops (
	stop_id varchar(255) NOT NULL,
	stop_code varchar(255) NULL,
	stop_name varchar(255) NULL,
	stop_desc varchar(255) NULL,
	stop_lat float8 NULL,
	stop_lon float8 NULL,
	zone_id varchar(255) NULL,
	stop_url varchar(255) NULL,
	location_type int4 NULL,
	parent_station varchar(255) NULL,
	time_zone varchar(255) NULL,
	wheelchair_type int4 NULL,
	platform_code varchar(255) NULL,
	CONSTRAINT stops_pkey PRIMARY KEY (stop_id)
)
WITH (
	OIDS=FALSE
) ;

CREATE TABLE IF NOT EXISTS GTFS_NAME.shapes (
	shape_id varchar(255) NOT NULL,
	shape_dist_traveled float8 NULL,
	shape_pt_lat float8 NULL,
	shape_pt_lon float8 NULL,
	shape_pt_sequence int8 NULL,
	CONSTRAINT shapes_pkey PRIMARY KEY (shape_id, shape_pt_sequence)
)
WITH (
	OIDS=FALSE
) ;

CREATE UNIQUE INDEX idx_GTFS_NAME_shapes_id_sequence_unique ON GTFS_NAME.shapes (shape_id, shape_pt_sequence);
--	, CONSTRAINT shapes_pkey PRIMARY KEY (shape_id)

CREATE TABLE IF NOT EXISTS GTFS_NAME.feed_info (
	feed_publisher_name varchar(255) NOT NULL,
	feed_end_date varchar(255) NULL,
	feed_lang varchar(255) NULL,
	feed_publisher_url varchar(255) NULL,
	feed_start_date varchar(255) NULL,
	feed_version varchar(255) NULL,
	CONSTRAINT feed_info_pkey PRIMARY KEY (feed_publisher_name)
)
WITH (
	OIDS=FALSE
) ;

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

CREATE TABLE IF NOT EXISTS GTFS_NAME.calendar_dates (
	calendar_date_id bigserial NOT NULL,
	"date" varchar(255) NULL,
	exception_type int4 NULL,
	service_id varchar(255) NULL,
	CONSTRAINT calendar_dates_pkey PRIMARY KEY (calendar_date_id),
	CONSTRAINT fk_GTFS_NAME_calendar_dates_calendar FOREIGN KEY (service_id) REFERENCES GTFS_NAME.calendar(service_id) NOT VALID
)
WITH (
	OIDS=FALSE
) ;

CREATE TABLE IF NOT EXISTS GTFS_NAME.trips (
	trip_id varchar(255) NOT NULL,
	block_id varchar(255) NULL,
	direction_id int4 NULL,
	trip_headsign varchar(255) NULL,
	trip_short_name varchar(255) NULL,
	trip_type int4 NULL,
	route_id varchar(255) NULL,
	service_id varchar(255) NULL,
	shape_id varchar(255) NULL,
	CONSTRAINT trips_pkey PRIMARY KEY (trip_id),
	CONSTRAINT fk_GTFS_NAME_trips_calendar FOREIGN KEY (service_id) REFERENCES GTFS_NAME.calendar(service_id) NOT VALID,
	CONSTRAINT fk_GTFS_NAME_trips_route FOREIGN KEY (route_id) REFERENCES GTFS_NAME.routes(route_id) NOT VALID
)
WITH (
	OIDS=FALSE
) ;
--	CONSTRAINT fk_GTFS_NAME_trips_shape FOREIGN KEY (shape_id) REFERENCES GTFS_NAME.shapes(shape_id) NOT VALID,

CREATE TABLE IF NOT EXISTS GTFS_NAME.frequencies (
	frequency_id bigserial NOT NULL,
	end_time time NULL,
	exact_time int4 NULL,
	headway_secs int8 NULL,
	start_time time NULL,
	trip_id varchar(255) NULL,
	CONSTRAINT frequencies_pkey PRIMARY KEY (frequency_id),
	CONSTRAINT fk_GTFS_NAME_frequencies_trip FOREIGN KEY (trip_id) REFERENCES GTFS_NAME.trips(trip_id) NOT VALID
)
WITH (
	OIDS=FALSE
) ;

CREATE TABLE IF NOT EXISTS GTFS_NAME.stop_times (
	stop_time_id bigserial NOT NULL,
	arrival_time varchar(255) NULL,
	departure_time varchar(255) NULL,
	drop_off_type int4 NULL,
	pickup_type int4 NULL,
	shape_dist_traveled float8 NULL,
	stop_headsign varchar(255) NULL,
	stop_sequence int4 NULL,
	stop_id varchar(255) NULL,
	trip_id varchar(255) NULL,
	CONSTRAINT stop_times_pkey PRIMARY KEY (stop_time_id),
	CONSTRAINT fk_GTFS_NAME_stop_times_stop FOREIGN KEY (stop_id) REFERENCES GTFS_NAME.stops(stop_id) NOT VALID,
	CONSTRAINT fk_GTFS_NAME_stop_times_trip FOREIGN KEY (trip_id) REFERENCES GTFS_NAME.trips(trip_id) NOT VALID
)
WITH (
	OIDS=FALSE
) ;

CREATE TABLE IF NOT EXISTS GTFS_NAME.transfer (
	transfer_id varchar(255) NOT NULL,
	min_transfer int8 NULL,
	transfer_type int4 NULL,
	from_stop_id varchar(255) NULL,
	to_stop_id varchar(255) NULL,
	CONSTRAINT transfers_pkey PRIMARY KEY (transfer_id),
	CONSTRAINT fk_GTFS_NAME_transfer_to_stop FOREIGN KEY (to_stop_id) REFERENCES GTFS_NAME.stops(stop_id) NOT VALID,
	CONSTRAINT fk_GTFS_NAME_transfer_from_stop FOREIGN KEY (from_stop_id) REFERENCES GTFS_NAME.stops(stop_id) NOT VALID
)
WITH (
	OIDS=FALSE
) ;
