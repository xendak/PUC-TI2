package service;

import java.util.Scanner;
import java.time.LocalDate;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import dao.ProductDAO;
import model.Product;
import spark.Request;
import spark.Response;


public class ProductService {

    private ProductDAO productDAO = new ProductDAO();
    private String form;
    private final int FORM_INSERT = 1;
    private final int FORM_DETAIL = 2;
    private final int FORM_UPDATE = 3;
    private final int FORM_ORDERBY_ID = 1;
    private final int FORM_ORDERBY_DESCRIPTION = 2;
    private final int FORM_ORDERBY_PRICE = 3;
    
    
    public ProductService() {
        makeForm();
    }

    
    public void makeForm() {
        makeForm(FORM_INSERT, new Product(), FORM_ORDERBY_DESCRIPTION);
    }

    
    public void makeForm(int orderBy) {
        makeForm(FORM_INSERT, new Product(), orderBy);
    }

    
    public void makeForm(int type, Product product, int orderBy) {
        String fileName = "form.html";
        form = "";
        try{
            Scanner input = new Scanner(new File(fileName));
            while(input.hasNext()){
                form += (input.nextLine() + "\n");
            }
            input.close();
        }  catch (Exception e) { System.out.println(e.getMessage()); }
        
        String singleProduct = "";
        if(type != FORM_INSERT) {
            singleProduct += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
            singleProduct += "\t\t<tr>";
            singleProduct += "\t\t\t<td align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;<a href=\"/produto/list/1\">New Product</a></b></font></td>";
            singleProduct += "\t\t</tr>";
            singleProduct += "\t</table>";
            singleProduct += "\t<br>";            
        }
        
        if(type == FORM_INSERT || type == FORM_UPDATE) {
            String action = "/produto/";
            String name, description, buttonLabel;
            if (type == FORM_INSERT){
                action += "insert";
                name = "Insert Product";
                description = "milk, bread, ...";
                buttonLabel = "Insert";
            } else {
                action += "update/" + product.getId();
                name = "Update Product (ID " + product.getId() + ")";
                description = product.getDescription();
                buttonLabel = "Update";
            }
            singleProduct += "\t<form class=\"form--register\" action=\"" + action + "\" method=\"post\" id=\"form-add\">";
            singleProduct += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
            singleProduct += "\t\t<tr>";
            singleProduct += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;" + name + "</b></font></td>";
            singleProduct += "\t\t</tr>";
            singleProduct += "\t\t<tr>";
            singleProduct += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
            singleProduct += "\t\t</tr>";
            singleProduct += "\t\t<tr>";
            singleProduct += "\t\t\t<td>&nbsp;Description: <input class=\"input--register\" type=\"text\" name=\"descricao\" value=\""+ description +"\"></td>";
            singleProduct += "\t\t\t<td>Price: <input class=\"input--register\" type=\"text\" name=\"preco\" value=\""+ product.getPrice() +"\"></td>";
            singleProduct += "\t\t\t<td>Quantity: <input class=\"input--register\" type=\"text\" name=\"quantidade\" value=\""+ product.getQuantity() +"\"></td>";
            singleProduct += "\t\t</tr>";
            singleProduct += "\t\t<tr>";
            singleProduct += "\t\t\t<td>&nbsp;Manufacturing Date: <input class=\"input--register\" type=\"text\" name=\"dataFabricacao\" value=\""+ product.getManufacturingDate().toString() + "\"></td>";
            singleProduct += "\t\t\t<td>Expiration Date: <input class=\"input--register\" type=\"text\" name=\"dataValidade\" value=\""+ product.getExpirationDate().toString() + "\"></td>";
            singleProduct += "\t\t\t<td align=\"center\"><input type=\"submit\" value=\""+ buttonLabel +"\" class=\"input--main__style input--button\"></td>";
            singleProduct += "\t\t</tr>";
            singleProduct += "\t</table>";
            singleProduct += "\t</form>";        
        } else if (type == FORM_DETAIL){
            singleProduct += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
            singleProduct += "\t\t<tr>";
            singleProduct += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Product Details (ID " + product.getId() + ")</b></font></td>";
            singleProduct += "\t\t</tr>";
            singleProduct += "\t\t<tr>";
            singleProduct += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
            singleProduct += "\t\t</tr>";
            singleProduct += "\t\t<tr>";
            singleProduct += "\t\t\t<td>&nbsp;Description: "+ product.getDescription() +"</td>";
            singleProduct += "\t\t\t<td>Price: "+ product.getPrice() +"</td>";
            singleProduct += "\t\t\t<td>Quantity: "+ product.getQuantity() +"</td>";
            singleProduct += "\t\t</tr>";
            singleProduct += "\t\t<tr>";
            singleProduct += "\t\t\t<td>&nbsp;Manufacturing Date: "+ product.getManufacturingDate().toString() + "</td>";
            singleProduct += "\t\t\t<td>Expiration Date: "+ product.getExpirationDate().toString() + "</td>";
            singleProduct += "\t\t\t<td>&nbsp;</td>";
            singleProduct += "\t\t</tr>";
            singleProduct += "\t</table>";        
        } else {
            System.out.println("ERROR! Type not identified " + type);
        }
        form = form.replaceFirst("<UM-PRODUTO>", singleProduct);
        
        String list = new String("<table width=\"80%\" align=\"center\" bgcolor=\"#f3f3f3\">");
        list += "\n<tr><td colspan=\"6\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Product List</b></font></td></tr>\n" +
                "\n<tr><td colspan=\"6\">&nbsp;</td></tr>\n" +
                "\n<tr>\n" + 
                "\t<td><a href=\"/produto/list/" + FORM_ORDERBY_ID + "\"><b>ID</b></a></td>\n" +
                "\t<td><a href=\"/produto/list/" + FORM_ORDERBY_DESCRIPTION + "\"><b>Description</b></a></td>\n" +
                "\t<td><a href=\"/produto/list/" + FORM_ORDERBY_PRICE + "\"><b>Price</b></a></td>\n" +
                "\t<td width=\"100\" align=\"center\"><b>Details</b></td>\n" +
                "\t<td width=\"100\" align=\"center\"><b>Update</b></td>\n" +
                "\t<td width=\"100\" align=\"center\"><b>Delete</b></td>\n" +
                "</tr>\n";
        
        List<Product> products;
        if (orderBy == FORM_ORDERBY_ID) {                     products = productDAO.getOrderById();
        } else if (orderBy == FORM_ORDERBY_DESCRIPTION) {     products = productDAO.getOrderByDescription();
        } else if (orderBy == FORM_ORDERBY_PRICE) {           products = productDAO.getOrderByPrice();
        } else {                                              products = productDAO.get();
        }

        int i = 0;
        String bgcolor = "";
        for (Product p : products) {
            bgcolor = (i++ % 2 == 0) ? "#fff5dd" : "#dddddd";
            list += "\n<tr bgcolor=\""+ bgcolor +"\">\n" + 
                      "\t<td>" + p.getId() + "</td>\n" +
                      "\t<td>" + p.getDescription() + "</td>\n" +
                      "\t<td>" + p.getPrice() + "</td>\n" +
                      "\t<td align=\"center\" valign=\"middle\"><a href=\"/produto/" + p.getId() + "\"><img src=\"/image/detail.png\" width=\"20\" height=\"20\"/></a></td>\n" +
                      "\t<td align=\"center\" valign=\"middle\"><a href=\"/produto/update/" + p.getId() + "\"><img src=\"/image/update.png\" width=\"20\" height=\"20\"/></a></td>\n" +
                      "\t<td align=\"center\" valign=\"middle\"><a href=\"javascript:confirmarDeleteProduto('" + p.getId() + "', '" + p.getDescription() + "', '" + p.getPrice() + "');\"><img src=\"/image/delete.png\" width=\"20\" height=\"20\"/></a></td>\n" +
                      "</tr>\n";
        }
        list += "</table>";        
        form = form.replaceFirst("<LISTAR-PRODUTO>", list);                
    }
    
    
    public Object insert(Request request, Response response) {
        String description = request.queryParams("descricao");
        float price = Float.parseFloat(request.queryParams("preco"));
        int quantity = Integer.parseInt(request.queryParams("quantidade"));
        LocalDateTime manufacturingDate = LocalDateTime.parse(request.queryParams("dataFabricacao"));
        LocalDate expirationDate = LocalDate.parse(request.queryParams("dataValidade"));
        
        String resp = "";
        
        Product product = new Product(-1, description, price, quantity, manufacturingDate, expirationDate);
        
        if(productDAO.insert(product) == true) {
            resp = "Product (" + description + ") inserted!";
            response.status(201); // 201 Created
        } else {
            resp = "Product (" + description + ") not inserted!";
            response.status(404); // 404 Not found
        }
            
        makeForm();
        return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
    }

    
    public Object get(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));        
        Product product = (Product) productDAO.get(id);
        
        if (product != null) {
            response.status(200); // success
            makeForm(FORM_DETAIL, product, FORM_ORDERBY_DESCRIPTION);
        } else {
            response.status(404); // 404 Not found
            String resp = "Product " + id + " not found.";
            makeForm();
            form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

        return form;
    }

    
    public Object getToUpdate(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));        
        Product product = (Product) productDAO.get(id);
        
        if (product != null) {
            response.status(200); // success
            makeForm(FORM_UPDATE, product, FORM_ORDERBY_DESCRIPTION);
        } else {
            response.status(404); // 404 Not found
            String resp = "Product " + id + " not found.";
            makeForm();
            form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

        return form;
    }
    
    
    public Object getAll(Request request, Response response) {
        int orderBy = Integer.parseInt(request.params(":orderby"));
        makeForm(orderBy);
        response.header("Content-Type", "text/html");
        response.header("Content-Encoding", "UTF-8");
        return form;
    }            
    
    public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Product product = productDAO.get(id);
        String resp = "";       

        if (product != null) {
            product.setDescription(request.queryParams("descricao"));
            product.setPrice(Float.parseFloat(request.queryParams("preco")));
            product.setQuantity(Integer.parseInt(request.queryParams("quantidade")));
            product.setManufacturingDate(LocalDateTime.parse(request.queryParams("dataFabricacao")));
            product.setExpirationDate(LocalDate.parse(request.queryParams("dataValidade")));
            productDAO.update(product);
            response.status(200); // success
            resp = "Product (ID " + product.getId() + ") updated!";
        } else {
            response.status(404); // 404 Not found
            resp = "Product (ID " + product.getId() + ") not found!";
        }
        makeForm();
        return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
    }

    
    public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Product product = productDAO.get(id);
        String resp = "";       

        if (product != null) {
            productDAO.delete(id);
            response.status(200); // success
            resp = "Product (" + id + ") deleted!";
        } else {
            response.status(404); // 404 Not found
            resp = "Product (" + id + ") not found!";
        }
        makeForm();
        return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
    }
}
