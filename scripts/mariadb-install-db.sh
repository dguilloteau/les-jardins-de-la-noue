#!/bin/bash

ls -l
service mysqld start
mysql < scripts/db/install-db-tests.sql
