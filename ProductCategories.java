package ecommerce;

// ── Electronics ──────────────────────────────────────────────────────────────
class Electronics extends Product {
    private String brand;
    private int warrantyMonths;

    public Electronics(String id, String name, double price, int stock,
                       String description, String brand, int warrantyMonths) {
        super(id, name, price, stock, description);
        this.brand = brand;
        this.warrantyMonths = warrantyMonths;
    }

    @Override public String getCategory()  { return "Electronics"; }
    @Override public double getDiscount()  { return 0.05; }  // 5% off electronics

    public String getBrand()          { return brand; }
    public int    getWarrantyMonths() { return warrantyMonths; }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Brand: %s | Warranty: %d mo", brand, warrantyMonths);
    }
}

// ── Clothing ─────────────────────────────────────────────────────────────────
class Clothing extends Product {
    private String size;
    private String material;

    public Clothing(String id, String name, double price, int stock,
                    String description, String size, String material) {
        super(id, name, price, stock, description);
        this.size     = size;
        this.material = material;
    }

    @Override public String getCategory() { return "Clothing"; }
    @Override public double getDiscount() { return 0.10; }  // 10% off clothing

    @Override
    public String toString() {
        return super.toString() + String.format(" | Size: %s | Material: %s", size, material);
    }
}

// ── Food ─────────────────────────────────────────────────────────────────────
class Food extends Product {
    private String expiryDate;
    private boolean isOrganic;

    public Food(String id, String name, double price, int stock,
                String description, String expiryDate, boolean isOrganic) {
        super(id, name, price, stock, description);
        this.expiryDate = expiryDate;
        this.isOrganic  = isOrganic;
    }

    @Override public String getCategory() { return "Food"; }
    @Override public double getDiscount() { return 0.0; }  // no discount on food

    @Override
    public String toString() {
        return super.toString() + String.format(" | Expiry: %s | Organic: %s", expiryDate, isOrganic ? "Yes" : "No");
    }
}

// ── Books ─────────────────────────────────────────────────────────────────────
class Book extends Product {
    private String author;
    private String isbn;

    public Book(String id, String name, double price, int stock,
                String description, String author, String isbn) {
        super(id, name, price, stock, description);
        this.author = author;
        this.isbn   = isbn;
    }

    @Override public String getCategory() { return "Book"; }
    @Override public double getDiscount() { return 0.15; }  // 15% off books

    @Override
    public String toString() {
        return super.toString() + String.format(" | Author: %s | ISBN: %s", author, isbn);
    }
}
