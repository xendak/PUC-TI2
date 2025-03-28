package dao;

import model.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class ProductDAO extends DAO {    
    public ProductDAO() {
        super();
        connect();
    }
    
    
    public void finalize() {
        close();
    }
    
    
    public boolean insert(Product product) {
        boolean status = false;
        try {
            String sql = "INSERT INTO produto (descricao, preco, quantidade, datafabricacao, datavalidade) "
                       + "VALUES ('" + product.getDescription() + "', "
                       + product.getPrice() + ", " + product.getQuantity() + ", ?, ?);";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setTimestamp(1, Timestamp.valueOf(product.getManufacturingDate()));
            st.setDate(2, Date.valueOf(product.getExpirationDate()));
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException u) {  
            throw new RuntimeException(u);
        }
        return status;
    }

    
    public Product get(int id) {
        Product product = null;
        
        try {
            Statement st = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * FROM produto WHERE id="+id;
            ResultSet rs = st.executeQuery(sql);    
            if(rs.next()){            
                 product = new Product(rs.getInt("id"), rs.getString("descricao"), (float)rs.getDouble("preco"), 
                                       rs.getInt("quantidade"), 
                                       rs.getTimestamp("datafabricacao").toLocalDateTime(),
                                       rs.getDate("datavalidade").toLocalDate());
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return product;
    }
    
    
    public List<Product> get() {
        return get("");
    }

    
    public List<Product> getOrderById() {
        return get("id");        
    }
    
    
    public List<Product> getOrderByDescription() {
        return get("descricao");        
    }
    
    
    public List<Product> getOrderByPrice() {
        return get("preco");        
    }
    
    
    private List<Product> get(String orderBy) {
        List<Product> products = new ArrayList<Product>();
        
        try {
            Statement st = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * FROM produto" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
            ResultSet rs = st.executeQuery(sql);               
            while(rs.next()) {                
                Product p = new Product(rs.getInt("id"), rs.getString("descricao"), (float)rs.getDouble("preco"), 
                                        rs.getInt("quantidade"),
                                        rs.getTimestamp("datafabricacao").toLocalDateTime(),
                                        rs.getDate("datavalidade").toLocalDate());
                products.add(p);
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return products;
    }
    
    
    public boolean update(Product product) {
        boolean status = false;
        try {  
            String sql = "UPDATE produto SET descricao = '" + product.getDescription() + "', "
                       + "preco = " + product.getPrice() + ", " 
                       + "quantidade = " + product.getQuantity() + ","
                       + "datafabricacao = ?, " 
                       + "datavalidade = ? WHERE id = " + product.getId();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setTimestamp(1, Timestamp.valueOf(product.getManufacturingDate()));
            st.setDate(2, Date.valueOf(product.getExpirationDate()));
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException u) {  
            throw new RuntimeException(u);
        }
        return status;
    }
    
    
    public boolean delete(int id) {
        boolean status = false;
        try {  
            Statement st = connection.createStatement();
            st.executeUpdate("DELETE FROM produto WHERE id = " + id);
            st.close();
            status = true;
        } catch (SQLException u) {  
            throw new RuntimeException(u);
        }
        return status;
    }
}
