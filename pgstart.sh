#!/bin/sh

# Start PostgreSQL

echo "Starting PostgreSQL..."
pg_ctl                                                  \
  -D $PGDATA                                            \
  -l $PGDATA/postgres.log                               \
  -o "-c unix_socket_directories='$PGDATA'"             \
  -o "-c listen_addresses='*'"                          \
  -o "-c log_destination='stderr'"                      \
  -o "-c logging_collector=on"                          \
  -o "-c log_directory='log'"                           \
  -o "-c log_filename='postgresql-%Y-%m-%d_%H%M%S.log'" \
  -o "-c log_min_messages=info"                         \
  -o "-c log_min_error_statement=info"                  \
  -o "-c log_connections=on"                            \
  start

