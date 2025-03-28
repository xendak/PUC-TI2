package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Product {
    private int id;
    private String description;
    private float price;
    private int quantity;
    private LocalDateTime manufacturingDate;    
    private LocalDate expirationDate;
    
    public Product() {
        id = -1;
        description = "";
        price = 0.00F;
        quantity = 0;
        manufacturingDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        expirationDate = LocalDate.now().plusMonths(6); // default is 6 months expiration
    }

    public Product(int id, String description, float price, int quantity, LocalDateTime manufacturingDate, LocalDate expirationDate) {
        setId(id);
        setDescription(description);
        setPrice(price);
        setQuantity(quantity);
        setManufacturingDate(manufacturingDate);
        setExpirationDate(expirationDate);
    }        
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public LocalDateTime getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(LocalDateTime manufacturingDate) {
        // Get current date
        LocalDateTime now = LocalDateTime.now();
        // Ensure manufacturing date is not in the future
        if (now.compareTo(manufacturingDate) >= 0)
            this.manufacturingDate = manufacturingDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        // manufacturing date must be before expiration date
        if (getManufacturingDate().isBefore(expirationDate.atStartOfDay()))
            this.expirationDate = expirationDate;
    }

    public boolean isValid() {
        return LocalDateTime.now().isBefore(this.getExpirationDate().atTime(23, 59));
    }

    @Override
    public String toString() {
        return "Product: " + description + "   Price: R$" + price + "   Quantity: " + quantity + 
               "   Manufacturing Date: " + manufacturingDate + "   Expiration Date: " + expirationDate;
    }
    
    @Override
    public boolean equals(Object obj) {
        return (this.getId() == ((Product) obj).getId());
    }    
}
