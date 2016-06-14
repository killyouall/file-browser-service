#!/bin/bash

/usr/lib/postgresql/9.3/bin/postgres -D /var/lib/postgresql/9.3/main -c config_file=/etc/postgresql/9.3/main/postgresql.conf 2>&1 &
sleep 10
psql -f /filebrowser/setup.sql -d filebrowser
java -agentlib:jdwp=transport=dt_socket,server=y,address=8000,suspend=n -jar /filebrowser/fileservice.jar server filebrowser/files-service.yml