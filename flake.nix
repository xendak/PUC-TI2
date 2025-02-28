{
  description = "Development environment for PostgreSQL, Java, Node.js, and additional tools";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";  # Use the latest unstable nixpkgs
    flake-utils.url = "github:numtide/flake-utils";      # Utility for handling multiple systems
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
          # List of packages to include in the environment
          packages = with pkgs; [
            gcc          # GNU Compiler Collection
            gradle       # Build tool for Java
            jdk          # Java Development Kit (default JDK)
            maven        # Build automation tool for Java
            ncurses      # Library for text-based user interfaces
            patchelf     # Utility to modify ELF binaries
            zlib         # Compression library
            postgresql   # PostgreSQL database
            pgcli        # Command-line interface for PostgreSQL
            nodejs       # Node.js (latest stable version)
            git          # Git for version control
            inotify-tools # File system event monitoring
          ];

          # Packages to be included in the build environment
          buildInputs = with pkgs; [
            glibcLocales
            postgresql
            pgcli
            nodejs
            git
            inotify-tools
          ];

          shellHook = ''
            mkdir -p .nix-shell
            export NIX_SHELL_DIR=$PWD/.nix-shell
            export PGDATA=$NIX_SHELL_DIR/db

            ####################################################################
            # Clean up after exiting the Nix shell using `trap`.
            ####################################################################
            # trap \
            # "
            #   echo 'Stopping PostgreSQL...'
            #   pg_ctl -D $PGDATA stop
            #   echo 'Cleaning up .nix-shell directory...'
            #   rm -rf $NIX_SHELL_DIR
            # " EXIT

            ####################################################################
            # If the database is not initialized, set it up.
            ####################################################################
            if ! test -d $PGDATA; then
              echo "Initializing PostgreSQL database..."
              pg_ctl initdb -D $PGDATA

              HOST_COMMON="host\s\+all\s\+all"
              sed -i "s|^$HOST_COMMON.*127.*$|host all all 0.0.0.0/0 trust|" $PGDATA/pg_hba.conf
              sed -i "s|^$HOST_COMMON.*::1.*$|host all all ::/0 trust|" $PGDATA/pg_hba.conf
            fi

            ####################################################################
            # Start PostgreSQL
            ####################################################################
            # echo "Starting PostgreSQL..."
            # pg_ctl                                                  \
            #   -D $PGDATA                                            \
            #   -l $PGDATA/postgres.log                               \
            #   -o "-c unix_socket_directories='$PGDATA'"             \
            #   -o "-c listen_addresses='*'"                          \
            #   -o "-c log_destination='stderr'"                      \
            #   -o "-c logging_collector=on"                          \
            #   -o "-c log_directory='log'"                           \
            #   -o "-c log_filename='postgresql-%Y-%m-%d_%H%M%S.log'" \
            #   -o "-c log_min_messages=info"                         \
            #   -o "-c log_min_error_statement=info"                  \
            #   -o "-c log_connections=on"                            \
            #   start

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
