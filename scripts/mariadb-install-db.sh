#!/bin/bash

ls -l
echo ${MARIADB_ROOT_PASSWORD}
echo ${MARIADB_DATABASE}
echo ${MARIADB_USER}
echo ${MARIADB_PASSWORD}

sudo apt install mariadb-client
mariadb --port 5432 --user ${MARIADB_USER} --password ${MARIADB_PASSWORD} < scripts/db/install-db-tests.sql;
