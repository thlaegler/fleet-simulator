#!/bin/bash


usage() {
    cat <<EOF
NAME
    ${0##*/} Convert GTFS into SQL File

SYNOPSIS
    ${0##*/} GTFS_NAME

Options:

    For example:
        ./gtfs2sql.sh stuttgart

RETURN CODES
    Returns 0 on success, 1 if an error occurs.

SEE ALSO
    See the documentation on Confluence for more details, including
    instructions on creating environments.

EOF
}


function gtfs2sql() {
	GTFS_NAME="$1"
	GTFS_FOLDER="$(pwd)"
	GTFS_DATABASE="gtfs"
	GTFS_BUCKET="mobilityos-dev-cloudbuild"
	GTFS_POSTGRES_INSTANCE="mobilityos-dev"

	mkdir $GTFS_NAME
	gsutil cp -r gs://$GTFS_BUCKET/_sql/$GTFS_DATABASE/$GTFS_NAME/* $GTFS_NAME/.
	cd $GTFS_NAME
	unzip $GTFS_NAME/gtfs.zip
	cd ..

	AGENCY_COLUMNS=$(head -n 1 $GTFS_NAME/gtfs/agency.txt)
	ROUTES_COLUMNS=$(head -n 1 $GTFS_NAME/gtfs/routes.txt)
	FARE_RULES_COLUMNS=$(head -n 1 $GTFS_NAME/gtfs/fare_rules.txt)
	FARE_ATTRIBUTES_COLUMNS=$(head -n 1 $GTFS_NAME/gtfs/fare_attributes.txt)
	STOPS_COLUMNS=$(head -n 1 $GTFS_NAME/gtfs/stops.txt)
	SHAPES_COLUMNS=$(head -n 1 $GTFS_NAME/gtfs/shapes.txt)
	FREQUENCIES_COLUMNS=$(head -n 1 $GTFS_NAME/gtfs/frequencies.txt)
	FEED_INFO_COLUMNS=$(head -n 1 $GTFS_NAME/gtfs/feed_info.txt)
	CALENDAR_COLUMNS=$(head -n 1 $GTFS_NAME/gtfs/calendar.txt)
	CALENDAR_DATES_COLUMNS=$(head -n 1 $GTFS_NAME/gtfs/calendar_dates.txt)
	TRIPS_COLUMNS=$(head -n 1 $GTFS_NAME/gtfs/trips.txt)
	STOP_TIMES_COLUMNS=$(head -n 1 $GTFS_NAME/gtfs/stop_times.txt)
	TRANSFER_COLUMNS=$(head -n 1 $GTFS_NAME/gtfs/transfer.txt)

	cp create_schema.sql $GTFS_NAME/create_schema.sql
	cp init_database.sql $GTFS_NAME/init_database.sql
	cp import_csv.sql $GTFS_NAME/import_csv.sql
	cp trigger_disable.sql $GTFS_NAME/trigger_disable.sql
	cp trigger_enable.sql $GTFS_NAME/trigger_enable.sql

	sed -i -e "s#\GTFS_NAME#$GTFS_NAME#g" \
		-e "s#\GTFS_FOLDER#$GTFS_FOLDER#g" $GTFS_NAME/init_database.sql

	sed -i -e "s#\GTFS_NAME#$GTFS_NAME#g" \
		-e "s#\GTFS_FOLDER#$GTFS_FOLDER#g" $GTFS_NAME/create_schema.sql

	sed -i -e "s#\GTFS_NAME#$GTFS_NAME#g" \
		-e "s#\GTFS_FOLDER#$GTFS_FOLDER#g" $GTFS_NAME/trigger_disable.sql

	sed -i -e "s#\GTFS_NAME#$GTFS_NAME#g" \
		-e "s#\GTFS_FOLDER#$GTFS_FOLDER#g" $GTFS_NAME/trigger_enable.sql

	sed -i -e "s#\GTFS_NAME#$GTFS_NAME#g" \
		-e "s#\GTFS_FOLDER#$GTFS_FOLDER#g" \
		-e "s#\AGENCY_COLUMNS#$AGENCY_COLUMNS#g" \
		-e "s#\ROUTES_COLUMNS#$ROUTES_COLUMNS#g" \
		-e "s#\FARE_RULES_COLUMNS#$FARE_RULES_COLUMNS#g" \
		-e "s#\FARE_ATTRIBUTES_COLUMNS#$FARE_ATTRIBUTES_COLUMNS#g" \
		-e "s#\STOPS_COLUMNS#$STOPS_COLUMNS#g" \
		-e "s#\SHAPES_COLUMNS#$SHAPES_COLUMNS#g" \
		-e "s#\FREQUENCIES_COLUMNS#$FREQUENCIES_COLUMNS#g" \
		-e "s#\FEED_INFO_COLUMNS#$FEED_INFO_COLUMNS#g" \
		-e "s#\CALENDAR_COLUMNS#$CALENDAR_COLUMNS#g" \
		-e "s#\CALENDAR_DATES_COLUMNS#$CALENDAR_DATES_COLUMNS#g" \
		-e "s#\TRIPS_COLUMNS#$TRIPS_COLUMNS#g" \
		-e "s#\STOP_TIMES_COLUMNS#$STOP_TIMES_COLUMNS#g" \
		-e "s#\TRANSFER_COLUMNS#$TRANSFER_COLUMNS#g" $GTFS_NAME/import_csv.sql

	#CREATE DATABASE gtfs;
	#CREATE USER fleetsimulator PASSWORD '7f512a41a3506989d1233017f624b35c0fa70bae';
	#ALTER USER fleetsimulator WITH SUPERUSER;
	#GRANT ALL PRIVILEGES ON DATABASE $GTFS_DATABASE TO fleetsimulator;

	psql -f $GTFS_NAME/init_database.sql $GTFS_DATABASE
	psql -f $GTFS_NAME/create_schema.sql $GTFS_DATABASE
	psql -f $GTFS_NAME/trigger_disable.sql $GTFS_DATABASE
	psql -f $GTFS_NAME/import_csv.sql $GTFS_DATABASE
	psql -f $GTFS_NAME/trigger_enable.sql $GTFS_DATABASE

	cat $GTFS_NAME/create_schema.sql > $GTFS_NAME/$GTFS_NAME.sql
	cat $GTFS_NAME/trigger_disable.sql >> $GTFS_NAME/$GTFS_NAME.sql
	pg_dump --schema=$GTFS_NAME $GTFS_DATABASE --no-owner --data-only >> $GTFS_NAME/$GTFS_NAME.sql
	cat $GTFS_NAME/trigger_enable.sql >> $GTFS_NAME/$GTFS_NAME.sql

	gsutil cp $GTFS_NAME/$GTFS_NAME.sql gs://$GTFS_BUCKET/_sql/$GTFS_DATABASE/$GTFS_NAME/$GTFS_NAME.sql
	#gcloud sql instances import $GTFS_POSTGRES_INSTANCE gs://$GTFS_BUCKET/_sql/$GTFS_DATABASE/$GTFS_NAME/$GTFS_NAME.sql --database=$GTFS_DATABASE  # gcloud 1gen
	echo "y" | gcloud sql import sql $GTFS_POSTGRES_INSTANCE gs://$GTFS_BUCKET/_sql/$GTFS_DATABASE/$GTFS_NAME/$GTFS_NAME.sql --database=$GTFS_DATABASE # gcloud 2gen
}

if [[ $# == 1 ]]; then
	gtfs2sql $1 $2 $3 $4 $5 $6
else
    usage
    exit 1
fi
