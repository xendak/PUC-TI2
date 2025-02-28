import java.sql.*;
import java.security.*;
import java.math.*;

public class DAO {
    protected Connection connection;

    public DAO() {
        connection = null;
    }

    public boolean connect() {
        String driver = "org.postgresql.Driver";                    
        String server = "localhost";
        String db = "ti2";
        int port = 5432;

        String url = "jdbc:postgresql://" + server + ":" + port +"/" + db;
        String user = "drops";
        String pw = "";

        boolean status = false;

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, pw);
            status = (connection == null);
            System.out.println("Conexão efetuada com o postgres!");
        } catch (ClassNotFoundException e) { 
            System.err.println("Conexão NÃO efetuada com o postgres -- Driver não encontrado -- " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Conexão NÃO efetuada com o postgres -- " + e.getMessage());
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


    public static String toMD5(String senha) throws Exception {
        MessageDigest m=MessageDigest.getInstance("MD5");
        m.update(senha.getBytes(),0, senha.length());
        return new BigInteger(1,m.digest()).toString(16);
    }
}
