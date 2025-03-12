#!/bin/bash

ls -l
mariadb < scripts/db/install-db-tests.sql
