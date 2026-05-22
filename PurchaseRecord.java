package ecommerce;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PurchaseRecord {
    private String orderId;
    private String customerName;
    private List<CartItem> items;
    private double subtotal;
    private double vat;
    private double total;
    private LocalDateTime purchasedAt;
    private String paymentMethod;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public PurchaseRecord(String orderId, String customerName, List<CartItem> items,
                          double subtotal, double vat, double total, String paymentMethod) {
        this.orderId       = orderId;
        this.customerName  = customerName;
        this.items         = List.copyOf(items);
        this.subtotal      = subtotal;
        this.vat           = vat;
        this.total         = total;
        this.paymentMethod = paymentMethod;
        this.purchasedAt   = LocalDateTime.now();
    }

    public String getOrderId()      { return orderId; }
    public double getTotal()        { return total; }
    public LocalDateTime getDate()  { return purchasedAt; }

    public String toReceiptString() {
        StringBuilder sb = new StringBuilder();
        sb.append("╔════════════════════════════════════════════════════╗\n");
        sb.append("║             MINI E-COMMERCE RECEIPT                ║\n");
        sb.append("╠════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║  Order ID  : %-37s║%n", orderId));
        sb.append(String.format("║  Customer  : %-37s║%n", customerName));
        sb.append(String.format("║  Date      : %-37s║%n", purchasedAt.format(FMT)));
        sb.append(String.format("║  Payment   : %-37s║%n", paymentMethod));
        sb.append("╠════════════════════════════════════════════════════╣\n");
        sb.append("║  ITEMS PURCHASED                                   ║\n");
        sb.append("╠════════════════════════════════════════════════════╣\n");
        for (CartItem item : items) {
            sb.append(String.format("║  %-25s x%3d @ PHP %8.2f  ║%n",
                    item.getProduct().getName(), item.getQuantity(),
                    item.getProduct().getDiscountedPrice()));
            sb.append(String.format("║    Subtotal: PHP %8.2f %24s║%n", item.getSubtotal(), ""));
        }
        sb.append("╠════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║  Subtotal  : PHP %8.2f %24s║%n", subtotal, ""));
        sb.append(String.format("║  VAT (12%%) : PHP %8.2f %24s║%n", vat, ""));
        sb.append(String.format("║  TOTAL     : PHP %8.2f %24s║%n", total, ""));
        sb.append("╠════════════════════════════════════════════════════╣\n");
        sb.append("║         Thank you for shopping with us!            ║\n");
        sb.append("╚════════════════════════════════════════════════════╝\n");
        return sb.toString();
    }

    public String toHistoryLine() {
        return String.format("%s | %-20s | %d items | PHP %.2f | %s | %s",
                orderId, customerName, items.size(), total, paymentMethod,
                purchasedAt.format(FMT));
    }
}
