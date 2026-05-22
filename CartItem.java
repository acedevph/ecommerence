package ecommerce;

import java.util.ArrayList;
import java.util.List;

public class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product  = product;
        this.quantity = quantity;
    }

    public Product getProduct()  { return product; }
    public int     getQuantity() { return quantity; }
    public void    setQuantity(int qty) { this.quantity = qty; }

    public double getSubtotal() {
        return product.getDiscountedPrice() * quantity;
    }

    @Override
    public String toString() {
        double unitPrice = product.getDiscountedPrice();
        return String.format("  %-25s | Qty: %3d | Unit: PHP %8.2f | Subtotal: PHP %10.2f",
                product.getName(), quantity, unitPrice, getSubtotal());
    }
}


class ShoppingCart {
    private List<CartItem> items = new ArrayList<>();

    public void addItem(Product product, int qty) {
        for (CartItem item : items) {
            if (item.getProduct().getProductId().equals(product.getProductId())) {
                item.setQuantity(item.getQuantity() + qty);
                return;
            }
        }
        items.add(new CartItem(product, qty));
    }

    public boolean removeItem(String productId) {
        return items.removeIf(i -> i.getProduct().getProductId().equalsIgnoreCase(productId));
    }

    public boolean updateQty(String productId, int qty) {
        for (CartItem item : items) {
            if (item.getProduct().getProductId().equalsIgnoreCase(productId)) {
                if (qty <= 0) { items.remove(item); return true; }
                item.setQuantity(qty);
                return true;
            }
        }
        return false;
    }

    public void clear() { items.clear(); }

    public boolean isEmpty() { return items.isEmpty(); }

    public List<CartItem> getItems() { return items; }

    public double getSubtotal() {
        return items.stream().mapToDouble(CartItem::getSubtotal).sum();
    }

    public double getVAT() { return getSubtotal() * 0.12; }

    public double getTotal() { return getSubtotal() + getVAT(); }

    public int getItemCount() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }
}
