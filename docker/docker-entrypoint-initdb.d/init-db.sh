#!/bin/bash

set +e # there might be errors when restoring, ignoring them
psql -U $POSTGRES_USER -d $POSTGRES_DB -c "UPDATE  Settings SET value = '7979' WHERE name = 'system/server/port';"
pg_restore -U $POSTGRES_USER -d $POSTGRES_DB /docker-entrypoint-initdb.d/dump
