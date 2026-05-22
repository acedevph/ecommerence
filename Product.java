package ecommerce;

public abstract class Product {
    protected String productId;
    protected String name;
    protected double price;
    protected int stock;
    protected String description;

    public Product(String productId, String name, double price, int stock, String description) {
        this.productId   = productId;
        this.name        = name;
        this.price       = price;
        this.stock       = stock;
        this.description = description;
    }

    public abstract String getCategory();
    public abstract double getDiscount();   // category-specific discount rate (0.0 – 1.0)

    public double getDiscountedPrice() {
        return price * (1.0 - getDiscount());
    }

    public boolean isInStock()        { return stock > 0; }
    public boolean hasStock(int qty)  { return stock >= qty; }
    public void reduceStock(int qty)  { stock -= qty; }

    // Getters
    public String getProductId()  { return productId; }
    public String getName()       { return name; }
    public double getPrice()      { return price; }
    public int    getStock()      { return stock; }
    public String getDescription(){ return description; }

    @Override
    public String toString() {
        String disc = getDiscount() > 0 ? String.format(" [%.0f%% OFF → PHP %.2f]", getDiscount()*100, getDiscountedPrice()) : "";
        return String.format("%-8s | %-25s | PHP %8.2f%s | Stock: %d | %s",
                productId, name, price, disc, stock, getCategory());
    }
}
