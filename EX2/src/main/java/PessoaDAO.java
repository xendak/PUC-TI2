import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//import Pessoa;

public class PessoaDAO extends DAO {

    public PessoaDAO() {
        super();
        connect();
    }

    public void finalize() {
        close();
    }

    public boolean insert(Pessoa pessoa) {
        boolean status = false;
        try {  
            Statement st = connection.createStatement();
            String sql = "INSERT INTO pessoas (cpf, nome, idade, profissao) "
                + "VALUES ("+pessoa.getCpf()+ ", '" + pessoa.getNome() + "', '"  
                + pessoa.getIdade() + "', '" + pessoa.getProfissao() + "');";
            System.out.println(sql);
            st.executeUpdate(sql);
            st.close();
            status = true;
        } catch (SQLException u) {  
            throw new RuntimeException(u);
        }
        return status;
    }

    public Pessoa get(int cpf) {
        Pessoa pessoa = null;

        try {
            Statement st = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * FROM pessoas WHERE id=" + cpf;
            System.out.println(sql);
            ResultSet rs = st.executeQuery(sql);	
            if(rs.next()){            
                pessoa = new Pessoa(rs.getInt("cpf"), rs.getInt("idade"), rs.getString("nome"), rs.getString("profissao"));
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return pessoa;
    }


    public List<Pessoa> get() {
        return get("");
    }


    public List<Pessoa> getOrderByCpf() {
        return get("cpf");		
    }


    public List<Pessoa> getOrderByNome() {
        return get("nome");		
    }


    public List<Pessoa> getOrderByIdade() {
        return get("idade");		
    }

    public List<Pessoa> getOrderByProfissao() {
        return get("profissao");		
    }


    private List<Pessoa> get(String orderBy) {	

        List<Pessoa> pessoas = new ArrayList<Pessoa>();

        try {
            Statement st = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * FROM pessoas" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
            System.out.println(sql);
            ResultSet rs = st.executeQuery(sql);	           
            while(rs.next()) {	            	
                Pessoa u = new Pessoa(rs.getInt("cpf"), rs.getInt("idade"), rs.getString("nome"), rs.getString("profissao"));
                pessoas.add(u);
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return pessoas;
    }

    public boolean update(Pessoa pessoa) {
        boolean status = false;
        try {  
            Statement st = connection.createStatement();
            String sql = "UPDATE pessoas SET nome = '" + pessoa.getNome() + "', idade = '"  
                + pessoa.getIdade() + "', profissao = '" + pessoa.getProfissao() + "'"
                + " WHERE cpf = " + pessoa.getCpf();
            System.out.println(sql);
            st.executeUpdate(sql);
            st.close();
            status = true;
        } catch (SQLException u) {  
            throw new RuntimeException(u);
        }
        return status;
    }

    public boolean delete(int cpf) {
        boolean status = false;
        try {  
            Statement st = connection.createStatement();
            String sql = "DELETE FROM pessoas WHERE cpf = " + cpf;
            System.out.println(sql);
            st.executeUpdate(sql);
            st.close();
            status = true;
        } catch (SQLException u) {  
            throw new RuntimeException(u);
        }
        return status;
    }


    public boolean verify(int cpf, String nome) {
        boolean resp = false;

        try {
            Statement st = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * FROM pessoas WHERE nome LIKE '" + nome + "' AND cpf LIKE '" + cpf  + "'";
            System.out.println(sql);
            ResultSet rs = st.executeQuery(sql);
            resp = rs.next();
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return resp;
    }	
}
