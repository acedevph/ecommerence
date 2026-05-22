package ecommerce;

import java.util.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MiniECommerceSystem {

    private List<Product>        catalog   = new ArrayList<>();
    private ShoppingCart         cart      = new ShoppingCart();
    private List<PurchaseRecord> history   = new ArrayList<>();
    private Scanner              scanner   = new Scanner(System.in);
    private int                  orderCtr  = 1000;
    private static final String  HIST_FILE = "purchase_history.txt";

    public static void main(String[] args) {
        new MiniECommerceSystem().run();
    }

    // ─────────────────────────────────────────────
    //  Main Loop
    // ─────────────────────────────────────────────
    public void run() {
        printBanner();
        seedProducts();
        loadHistory();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = getIntInput("Enter choice: ");
            switch (choice) {
                case 1  -> displayProducts(null);
                case 2  -> browseByCategory();
                case 3  -> searchProducts();
                case 4  -> addToCart();
                case 5  -> removeFromCart();
                case 6  -> updateCartQty();
                case 7  -> viewCart();
                case 8  -> checkout();
                case 9  -> viewPurchaseHistory();
                case 10 -> clearCart();
                case 0  -> { running = false; System.out.println("Goodbye! Happy shopping!"); }
                default ->   System.out.println("[!] Invalid choice.");
            }
        }
    }

    // ─────────────────────────────────────────────
    //  Display / Browse
    // ─────────────────────────────────────────────
    private void printBanner() {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║          MINI E-COMMERCE SYSTEM              ║");
        System.out.println("║      Electronics • Clothing • Food • Books   ║");
        System.out.println("╚══════════════════════════════════════════════╝");
    }

    private void printMenu() {
        int cartCount = cart.getItemCount();
        System.out.println("\n┌───────────────────────────────────────────────┐");
        System.out.printf( "│  🛒 Cart: %-5d item(s)   Total: PHP %10.2f │%n",
                cartCount, cart.getTotal());
        System.out.println("├───────────────────────────────────────────────┤");
        System.out.println("│   1. View All Products                        │");
        System.out.println("│   2. Browse by Category                       │");
        System.out.println("│   3. Search Products                          │");
        System.out.println("│   4. Add Item to Cart                         │");
        System.out.println("│   5. Remove Item from Cart                    │");
        System.out.println("│   6. Update Cart Quantity                     │");
        System.out.println("│   7. View Cart                                │");
        System.out.println("│   8. Checkout                                 │");
        System.out.println("│   9. Purchase History                         │");
        System.out.println("│  10. Clear Cart                               │");
        System.out.println("│   0. Exit                                     │");
        System.out.println("└───────────────────────────────────────────────┘");
    }

    private void displayProducts(List<Product> list) {
        List<Product> src = (list != null) ? list : catalog;
        if (src.isEmpty()) { System.out.println("  No products found."); return; }
        System.out.println("\n" + "─".repeat(110));
        System.out.printf("%-8s | %-25s | %-12s | %-10s | %-6s | Details%n",
                "ID", "Name", "Price", "Category", "Stock");
        System.out.println("─".repeat(110));
        src.forEach(System.out::println);
        System.out.println("─".repeat(110));
    }

    private void browseByCategory() {
        System.out.println("\n--- BROWSE BY CATEGORY ---");
        System.out.println("  1. Electronics   2. Clothing   3. Food   4. Book");
        int choice = getIntInput("Choose: ");
        String cat = switch (choice) {
            case 1 -> "Electronics"; case 2 -> "Clothing";
            case 3 -> "Food";        case 4 -> "Book";
            default -> null;
        };
        if (cat == null) { System.out.println("[!] Invalid category."); return; }
        System.out.println("\n═══ " + cat + " ═══");
        List<Product> filtered = catalog.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(cat))
                .toList();
        displayProducts(filtered);
    }

    private void searchProducts() {
        System.out.print("Search keyword: "); String kw = scanner.nextLine().trim().toLowerCase();
        List<Product> results = catalog.stream()
                .filter(p -> p.getName().toLowerCase().contains(kw)
                          || p.getDescription().toLowerCase().contains(kw)
                          || p.getCategory().toLowerCase().contains(kw))
                .toList();
        System.out.println("\nSearch results for \"" + kw + "\": " + results.size() + " found");
        displayProducts(results);
    }

    // ─────────────────────────────────────────────
    //  Cart Operations
    // ─────────────────────────────────────────────
    private void addToCart() {
        displayProducts(null);
        System.out.print("Product ID to add: "); String id = scanner.nextLine().trim();
        Product product = findProduct(id);
        if (product == null)           { System.out.println("[!] Product not found."); return; }
        if (!product.isInStock())      { System.out.println("[!] Product out of stock."); return; }

        int qty = getIntInput("Quantity: ");
        if (qty <= 0)                  { System.out.println("[!] Quantity must be > 0."); return; }
        if (!product.hasStock(qty))    {
            System.out.println("[!] Only " + product.getStock() + " in stock."); return;
        }

        cart.addItem(product, qty);
        System.out.printf("[✓] Added %d x %s to cart. (PHP %.2f each)%n",
                qty, product.getName(), product.getDiscountedPrice());
        if (product.getDiscount() > 0)
            System.out.printf("    Discount applied: %.0f%% off!%n", product.getDiscount() * 100);
    }

    private void removeFromCart() {
        if (cart.isEmpty()) { System.out.println("[!] Cart is empty."); return; }
        viewCart();
        System.out.print("Product ID to remove: "); String id = scanner.nextLine().trim();
        if (cart.removeItem(id)) System.out.println("[✓] Item removed from cart.");
        else System.out.println("[!] Item not found in cart.");
    }

    private void updateCartQty() {
        if (cart.isEmpty()) { System.out.println("[!] Cart is empty."); return; }
        viewCart();
        System.out.print("Product ID to update: "); String id = scanner.nextLine().trim();
        int qty = getIntInput("New quantity (0 = remove): ");
        if (cart.updateQty(id, qty)) System.out.println("[✓] Cart updated.");
        else System.out.println("[!] Item not found in cart.");
    }

    private void viewCart() {
        System.out.println("\n--- SHOPPING CART ---");
        if (cart.isEmpty()) { System.out.println("  Your cart is empty."); return; }
        cart.getItems().forEach(System.out::println);
        System.out.println("  " + "─".repeat(70));
        System.out.printf("  Subtotal : PHP %10.2f%n", cart.getSubtotal());
        System.out.printf("  VAT(12%%) : PHP %10.2f%n", cart.getVAT());
        System.out.printf("  TOTAL    : PHP %10.2f%n", cart.getTotal());
    }

    private void clearCart() {
        if (cart.isEmpty()) { System.out.println("[!] Cart is already empty."); return; }
        System.out.print("Clear all items? (y/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            cart.clear(); System.out.println("[✓] Cart cleared.");
        }
    }

    // ─────────────────────────────────────────────
    //  Checkout
    // ─────────────────────────────────────────────
    private void checkout() {
        if (cart.isEmpty()) { System.out.println("[!] Cart is empty. Add items first."); return; }

        System.out.println("\n--- CHECKOUT ---");
        viewCart();

        System.out.print("\nYour Name: "); String name = scanner.nextLine().trim();
        if (name.isBlank()) name = "Guest";

        System.out.println("Payment Method:");
        System.out.println("  1. Cash  2. GCash  3. Credit Card  4. PayMaya");
        int payChoice = getIntInput("Choose: ");
        String payMethod = switch (payChoice) {
            case 1 -> "Cash"; case 2 -> "GCash";
            case 3 -> "Credit Card"; case 4 -> "PayMaya";
            default -> "Cash";
        };

        System.out.printf("\nTotal to pay: PHP %.2f via %s%n", cart.getTotal(), payMethod);
        System.out.print("Confirm order? (y/n): ");
        if (!scanner.nextLine().trim().equalsIgnoreCase("y")) {
            System.out.println("[!] Order cancelled."); return;
        }

        // Deduct stock
        for (CartItem item : cart.getItems()) {
            item.getProduct().reduceStock(item.getQuantity());
        }

        String orderId = "ORD" + (++orderCtr);
        PurchaseRecord record = new PurchaseRecord(
                orderId, name, cart.getItems(),
                cart.getSubtotal(), cart.getVAT(), cart.getTotal(), payMethod);

        history.add(record);
        appendHistoryToFile(record);

        String receipt = record.toReceiptString();
        System.out.println("\n" + receipt);

        // Save receipt file
        String receiptFile = "receipt_" + orderId + ".txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(receiptFile))) {
            pw.print(receipt);
            System.out.println("[✓] Receipt saved → " + receiptFile);
        } catch (IOException e) {
            System.out.println("[!] Could not save receipt: " + e.getMessage());
        }

        cart.clear();
    }

    // ─────────────────────────────────────────────
    //  Purchase History
    // ─────────────────────────────────────────────
    private void viewPurchaseHistory() {
        System.out.println("\n--- PURCHASE HISTORY ---");
        if (history.isEmpty()) {
            System.out.println("  No purchases yet.");
            System.out.println("  (History file: " + HIST_FILE + ")");
            return;
        }
        System.out.printf("%-10s | %-20s | %-8s | %-12s | %-15s | %s%n",
                "Order ID", "Customer", "Items", "Total", "Payment", "Date");
        System.out.println("─".repeat(100));
        history.forEach(r -> System.out.println(r.toHistoryLine()));
        double totalRevenue = history.stream().mapToDouble(PurchaseRecord::getTotal).sum();
        System.out.println("─".repeat(100));
        System.out.printf("Total Revenue: PHP %.2f from %d order(s)%n", totalRevenue, history.size());
    }

    private void appendHistoryToFile(PurchaseRecord record) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(HIST_FILE, true))) {
            pw.println(record.toHistoryLine());
        } catch (IOException e) {
            System.out.println("[!] Could not save history: " + e.getMessage());
        }
    }

    private void loadHistory() {
        File file = new File(HIST_FILE);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            long lines = br.lines().count();
            System.out.println("[✓] Loaded " + lines + " previous order(s) from history.");
        } catch (IOException e) {
            System.out.println("[!] Could not load history: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    //  Helpers
    // ─────────────────────────────────────────────
    private Product findProduct(String id) {
        return catalog.stream()
                .filter(p -> p.getProductId().equalsIgnoreCase(id))
                .findFirst().orElse(null);
    }

    private int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Integer.parseInt(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("[!] Enter a valid number."); }
        }
    }

    // ─────────────────────────────────────────────
    //  Seed Data
    // ─────────────────────────────────────────────
    private void seedProducts() {
        // Electronics
        catalog.add(new Electronics("E001", "Samsung Galaxy S24",    49999, 10, "Flagship Android smartphone",   "Samsung",  24));
        catalog.add(new Electronics("E002", "Apple AirPods Pro",     14999,  8, "Noise-canceling earbuds",        "Apple",    12));
        catalog.add(new Electronics("E003", "Logitech MX Master 3",   5999, 15, "Advanced wireless mouse",        "Logitech", 12));
        catalog.add(new Electronics("E004", "Sony WH-1000XM5",       18999,  5, "Over-ear noise-canceling",       "Sony",     12));

        // Clothing
        catalog.add(new Clothing("C001", "Classic White Tee",      599, 50, "100% cotton t-shirt",     "M",   "Cotton"));
        catalog.add(new Clothing("C002", "Slim Fit Jeans",        1299, 30, "Stretch denim jeans",     "32",  "Denim"));
        catalog.add(new Clothing("C003", "Running Shoes",         3499, 20, "Lightweight mesh shoes",  "42",  "Mesh/Rubber"));
        catalog.add(new Clothing("C004", "Hoodie Sweatshirt",     1799, 25, "Warm fleece hoodie",      "L",   "Fleece"));

        // Food
        catalog.add(new Food("F001", "Arabica Coffee Beans (250g)",  450, 100, "Single origin Benguet beans", "2026-12-31", true));
        catalog.add(new Food("F002", "Dark Chocolate Bar",           180,  80, "70% cacao, fair trade",       "2026-08-01", true));
        catalog.add(new Food("F003", "Organic Oats (500g)",          299,  60, "Whole grain rolled oats",     "2027-01-15", true));
        catalog.add(new Food("F004", "Instant Noodles Pack (5s)",    125, 200, "Chicken flavor",              "2026-11-30", false));

        // Books
        catalog.add(new Book("B001", "Clean Code",                    899, 20, "R. Martin – software craftsmanship",    "Robert C. Martin",  "978-0132350884"));
        catalog.add(new Book("B002", "The Pragmatic Programmer",      950, 15, "Hunt & Thomas – developer best practices","D. Thomas & A. Hunt","978-0135957059"));
        catalog.add(new Book("B003", "Java: The Complete Reference", 1299, 12, "Schildt – comprehensive Java guide",     "Herbert Schildt",   "978-1260440232"));
        catalog.add(new Book("B004", "Atomic Habits",                 699, 35, "James Clear – habit building",           "James Clear",       "978-0735211292"));
    }
}
