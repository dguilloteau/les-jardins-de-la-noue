#!/bin/bash

ls -l
echo ${MARIADB_ROOT_PASSWORD}
echo ${MARIADB_DATABASE}
echo ${MARIADB_USER}
echo ${MARIADB_PASSWORD}

mysql -u${MARIADB_USER} -p${MARIADB_PASSWORD}; create database ${MARIADB_DATABASE}; use ${MARIADB_DATABASE}; source scripts/db/install-db-tests.sql;
