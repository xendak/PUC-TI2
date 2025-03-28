package dao;

import java.sql.*;

public class DAO {
    protected Connection connection;
    
    public DAO() {
        connection = null;
    }
    
    public boolean connect() {
        String driverName = "org.postgresql.Driver";                    
        String serverName = "localhost";
        String database = "ti2";
        int port = 5432;
        String url = "jdbc:postgresql://" + serverName + ":" + port +"/" + database;
        String username = "myuser";
        String password = "test";
        boolean status = false;

        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(url, username, password);
            status = (connection == null);
            System.out.println("Connection established with PostgreSQL!");
        } catch (ClassNotFoundException e) { 
            System.err.println("Connection NOT established -- Driver not found -- " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Connection NOT established -- " + e.getMessage());
        }

        return status;
    }
    
    public boolean close() {
        boolean status = false;
        
        try {
            connection.close();
            status = true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return status;
    }
}
