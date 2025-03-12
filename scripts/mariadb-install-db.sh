#!/bin/bash

ls -l
echo ${MARIADB_ROOT_PASSWORD}
echo ${MARIADB_DATABASE}
echo ${MARIADB_USER}
echo ${MARIADB_PASSWORD}

sudo apt install mariadb-client
mariadb < scripts/db/install-db-tests.sql;
