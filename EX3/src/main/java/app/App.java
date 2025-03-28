package app;

import static spark.Spark.*;
import service.ProductService;


public class App {
    
    private static ProductService productService = new ProductService();
    
    public static void main(String[] args) {
        port(6789);
        
        staticFiles.location("/public");
        
        post("/produto/insert", (request, response) -> productService.insert(request, response));

        get("/produto/:id", (request, response) -> productService.get(request, response));
        
        get("/produto/list/:orderby", (request, response) -> productService.getAll(request, response));

        get("/produto/update/:id", (request, response) -> productService.getToUpdate(request, response));
        
        post("/produto/update/:id", (request, response) -> productService.update(request, response));
           
        get("/produto/delete/:id", (request, response) -> productService.delete(request, response));
    }
}
