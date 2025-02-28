#!/bin/sh

echo 'Stopping PostgreSQL...'
pg_ctl -D $PGDATA stop
echo 'Cleaning up .nix-shell directory...'
rm -rf $NIX_SHELL_DIR
