#!/bin/bash

ls -l
service mariadb status
mysql < scripts/db/install-db-tests.sql
