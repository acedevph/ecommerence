# Mini E-Commerce System
**Package:** `ecommerce` | **Main Class:** `MiniECommerceSystem`

---

## Description
A console-based shopping system with a product catalog spread across 4 categories, a fully functional shopping cart, checkout with receipt generation, and persistent purchase history saved to a file.

---

## Features
- Browse all products in the catalog
- Filter products by category
- Search products by keyword
- Add items to cart (with stock validation)
- Remove items from cart
- Update item quantities in cart
- Category-based discounts applied automatically
- Compute subtotal, 12% VAT, and total at checkout
- Select payment method at checkout
- Generate and save receipt as a `.txt` file
- Purchase history saved to `purchase_history.txt`
- Clear the entire cart

---

## File Structure
```
ecommerce/
├── Product.java               ← Abstract base class (price, stock, discount)
├── ProductCategories.java     ← 4 subclasses: Electronics, Clothing, Food, Book
├── CartItem.java              ← CartItem + ShoppingCart class
├── PurchaseRecord.java        ← Order record with receipt formatting
└── MiniECommerceSystem.java   ← Main class
```

---

## Product Categories & Discounts
| Category | Discount | Example Products |
|----------|---------|-----------------|
| Electronics | 5% off | Samsung Galaxy S24, Sony WH-1000XM5 |
| Clothing | 10% off | Classic White Tee, Running Shoes |
| Food | No discount | Arabica Coffee Beans, Organic Oats |
| Books | 15% off | Clean Code, Atomic Habits |

---

## Billing Formula
```
Subtotal  = sum of (discounted price × quantity) for all cart items
VAT       = subtotal × 12%
Total     = subtotal + VAT
```

---

## How to Compile & Run

**Step 1 — Create output folder**
```bash
mkdir -p out/ecommerce
```

**Step 2 — Compile**
```bash
javac -d out/ecommerce ecommerce/*.java
```

**Step 3 — Run**
```bash
java -cp out/ecommerce ecommerce.MiniECommerceSystem
```

---

## Payment Methods Available
- Cash
- GCash
- Credit Card
- PayMaya

---

## Generated Files
| File | Description |
|------|-------------|
| `receipt_ORDxxxx.txt` | Receipt saved after every successful checkout |
| `purchase_history.txt` | All orders appended here; loaded back on startup |

---

## Demo Products (Pre-loaded)
| ID | Name | Category | Price |
|----|------|----------|-------|
| E001 | Samsung Galaxy S24 | Electronics | PHP 49,999 |
| E002 | Apple AirPods Pro | Electronics | PHP 14,999 |
| C001 | Classic White Tee | Clothing | PHP 599 |
| C003 | Running Shoes | Clothing | PHP 3,499 |
| F001 | Arabica Coffee Beans | Food | PHP 450 |
| F004 | Instant Noodles Pack | Food | PHP 125 |
| B001 | Clean Code | Book | PHP 899 |
| B004 | Atomic Habits | Book | PHP 699 |

*(16 products total across all categories)*

---

## Common Errors
| Error | Fix |
|-------|-----|
| `package ecommerce does not match` | Make sure files are inside a folder named exactly `ecommerce` |
| `Only X in stock` | Reduce the quantity — you cannot add more than available stock |
| `Product not found` | Double-check the product ID (case-insensitive, e.g., `E001`) |
| `Cart is empty` | Add at least one item before checking out |
