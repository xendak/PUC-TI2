{
  description = "Development environment for PostgreSQL, Java, Node.js, and additional tools";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs {
          inherit system;
          config = {};
          overlays = [];
        };
      in
      {
        devShell = pkgs.mkShell {
          packages = with pkgs; [
            gcc          
            gradle       
            jdk          
            maven        
            ncurses      
            patchelf     
            zlib         
            postgresql   
            pgcli        
            nodejs       
            git          
            inotify-tools 
            postgresql_jdbc
          ];

          buildInputs = with pkgs; [
            glibcLocales
            postgresql
            pgcli
            nodejs
            postgresql_jdbc
            git
            inotify-tools
          ];

          shellHook = ''
            mkdir -p .nix-shell
            export NIX_SHELL_DIR=$PWD/.nix-shell
            export PGDATA=$NIX_SHELL_DIR/db

            if ! test -d $PGDATA; then
              echo "Initializing PostgreSQL database..."
              pg_ctl initdb -D $PGDATA

              HOST_COMMON="host\s\+all\s\+all"
              sed -i "s|^$HOST_COMMON.*127.*$|host all all 0.0.0.0/0 trust|" $PGDATA/pg_hba.conf
              sed -i "s|^$HOST_COMMON.*::1.*$|host all all ::/0 trust|" $PGDATA/pg_hba.conf

              psql --host=localhost --dbname=postgres -c "CREATE USER myuser WITH PASSWORD 'test' SUPERUSER;"
              psql --host=localhost --dbname=postgres -c "CREATE DATABASE ti2 OWNER myuser;"
              pg_ctl stop -D $PGDATA

              # change this change port if multiple nix-shell instance
              # sed -i "s|^#port.*$|port = 5433|" $PGDATA/postgresql.conf
            fi

            # TODO: fix this to trap.
            # Start PostgreSQL instance
            # pg_ctl start -D $PGDATA -o "-k /tmp"
            # trap "pg_ctl stop -D $PGDATA" EXIT

            # Set up JDBC
            export POSTGRES_JDBC_DRIVER="${pkgs.postgresql_jdbc}/share/java/postgresql.jar"
            export CLASSPATH="$POSTGRES_JDBC_DRIVER:$CLASSPATH"

            ####################################################################
            # Install Node.js dependencies if a package.json exists.
            ####################################################################
            if test -f "$PWD/package.json"; then
              echo "Installing Node.js dependencies..."
              npm install
            fi

            echo "Development environment ready!"
          '';

          ####################################################################
          # Fix locale issues in pure shells (required for NixOS).
          ####################################################################
          LOCALE_ARCHIVE = if pkgs.stdenv.isLinux then "${pkgs.glibcLocales}/lib/locale/locale-archive" else "";
        };
      });
}
