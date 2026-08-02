package academy.javaengineering.oop.`12-collections`;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Demonstrates using Java collections with OOP design patterns.
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Collection-based domain models</li>
 *   <li>Repository pattern with collections</li>
 *   <li>Iterators and iterable patterns</li>
 *   <li>Immutable collections</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @version 1.0
 */
public class OOPCollectionsExample {

    /**
     * Product entity with natural ordering.
     */
    public static class Product implements Comparable<Product> {
        private final long id;
        private final String name;
        private final double price;
        private final String category;

        public Product(long id, String name, double price, String category) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.category = category;
        }

        public long getId() { return id; }
        public String getName() { return name; }
        public double getPrice() { return price; }
        public String getCategory() { return category; }

        @Override
        public int compareTo(Product other) {
            return Double.compare(this.price, other.price);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Product other)) return false;
            return id == other.id;
        }

        @Override
        public int hashCode() { return Long.hashCode(id); }

        @Override
        public String toString() {
            return "Product{id=%d, name='%s', price=%.2f, category='%s'}".formatted(
                    id, name, price, category);
        }
    }

    /**
     * Shopping cart using List and Map collections.
     */
    public static class ShoppingCart {
        private final Map<Long, CartItem> items = new LinkedHashMap<>();
        private final List<CartEventListener> listeners = new ArrayList<>();

        public interface CartEventListener {
            void onItemAdded(CartItem item);
            void onItemRemoved(CartItem item);
            void onCartCleared();
        }

        public static class CartItem {
            private final Product product;
            private int quantity;

            public CartItem(Product product, int quantity) {
                this.product = product;
                this.quantity = quantity;
            }

            public Product getProduct() { return product; }
            public int getQuantity() { return quantity; }
            public void setQuantity(int quantity) { this.quantity = quantity; }

            public double getSubtotal() {
                return product.getPrice() * quantity;
            }
        }

        public void addListener(CartEventListener listener) {
            listeners.add(listener);
        }

        public void addProduct(Product product, int quantity) {
            CartItem existing = items.get(product.getId());
            if (existing != null) {
                existing.setQuantity(existing.getQuantity() + quantity);
            } else {
                CartItem item = new CartItem(product, quantity);
                items.put(product.getId(), item);
                listeners.forEach(l -> l.onItemAdded(item));
            }
        }

        public boolean removeProduct(long productId) {
            CartItem removed = items.remove(productId);
            if (removed != null) {
                listeners.forEach(l -> l.onItemRemoved(removed));
                return true;
            }
            return false;
        }

        public double getTotal() {
            return items.values().stream()
                    .mapToDouble(CartItem::getSubtotal)
                    .sum();
        }

        public int getItemCount() {
            return items.values().stream()
                    .mapToInt(CartItem::getQuantity)
                    .sum();
        }

        public Collection<CartItem> getItems() {
            return Collections.unmodifiableCollection(items.values());
        }

        public void clear() {
            items.clear();
            listeners.forEach(CartEventListener::onCartCleared);
        }
    }

    /**
     * Inventory manager using SortedSet and TreeMap.
     */
    public static class InventoryManager {
        private final TreeMap<String, TreeSet<Product>> inventoryByCategory;
        private final Map<Long, Integer> stockCounts;

        public InventoryManager() {
            this.inventoryByCategory = new TreeMap<>();
            this.stockCounts = new HashMap<>();
        }

        public void addProduct(Product product, int stock) {
            inventoryByCategory
                    .computeIfAbsent(product.getCategory(), k -> new TreeSet<>())
                    .add(product);
            stockCounts.merge(product.getId(), stock, Integer::sum);
        }

        public List<Product> getProductsByCategory(String category) {
            TreeSet<Product> products = inventoryByCategory.get(category);
            return products != null ? new ArrayList<>(products) : List.of();
        }

        public Set<String> getCategories() {
            return Collections.unmodifiableSet(inventoryByCategory.keySet());
        }

        public int getStock(long productId) {
            return stockCounts.getOrDefault(productId, 0);
        }

        public boolean isInStock(long productId) {
            return getStock(productId) > 0;
        }

        public List<Product> getAllProductsSorted() {
            return inventoryByCategory.values().stream()
                    .flatMap(Collection::stream)
                    .sorted()
                    .toList();
        }
    }

    /**
     * Custom iterable using inner class iterator.
     */
    public static class Range implements Iterable<Integer> {
        private final int start;
        private final int end;
        private final int step;

        public Range(int start, int end) {
            this(start, end, 1);
        }

        public Range(int start, int end, int step) {
            this.start = start;
            this.end = end;
            this.step = step;
        }

        @Override
        public Iterator<Integer> iterator() {
            return new Iterator<>() {
                private int current = start;

                @Override
                public boolean hasNext() {
                    return step > 0 ? current < end : current > end;
                }

                @Override
                public Integer next() {
                    if (!hasNext()) throw new NoSuchElementException();
                    int value = current;
                    current += step;
                    return value;
                }
            };
        }

        public int size() {
            return (int) Math.ceil((double) (end - start) / step);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Collections with OOP Demo ===\n");

        // ShoppingCart with List and Map
        System.out.println("--- Shopping Cart ---");
        ShoppingCart cart = new ShoppingCart();
        cart.addListener(new ShoppingCart.CartEventListener() {
            @Override
            public void onItemAdded(ShoppingCart.CartItem item) {
                System.out.println("  Added: " + item.getProduct().getName() + " x" + item.getQuantity());
            }
            @Override
            public void onItemRemoved(ShoppingCart.CartItem item) {
                System.out.println("  Removed: " + item.getProduct().getName());
            }
            @Override
            public void onCartCleared() {
                System.out.println("  Cart cleared!");
            }
        });

        Product laptop = new Product(1, "Laptop", 999.99, "Electronics");
        Product mouse = new Product(2, "Mouse", 29.99, "Electronics");
        Product book = new Product(3, "Java Patterns", 49.99, "Books");

        cart.addProduct(laptop, 1);
        cart.addProduct(mouse, 2);
        cart.addProduct(book, 1);

        System.out.println("Items: " + cart.getItemCount());
        System.out.printf("Total: $%.2f%n", cart.getTotal());

        // Inventory with TreeMap and TreeSet
        System.out.println("\n--- Inventory Manager ---");
        InventoryManager inventory = new InventoryManager();
        inventory.addProduct(new Product(1, "Laptop", 999.99, "Electronics"), 10);
        inventory.addProduct(new Product(2, "Mouse", 29.99, "Electronics"), 50);
        inventory.addProduct(new Product(3, "Java Patterns", 49.99, "Books"), 25);
        inventory.addProduct(new Product(4, "Clean Code", 39.99, "Books"), 30);
        inventory.addProduct(new Product(5, "Desk", 199.99, "Furniture"), 5);

        System.out.println("Categories: " + inventory.getCategories());
        System.out.println("Electronics: " + inventory.getProductsByCategory("Electronics"));
        System.out.println("Books (sorted by price): " + inventory.getProductsByCategory("Books"));
        System.out.println("Laptop in stock: " + inventory.isInStock(1));
        System.out.println("All products: " + inventory.getAllProductsSorted());

        // Custom Iterable
        System.out.println("\n--- Custom Iterable (Range) ---");
        Range range1 = new Range(1, 6);
        Range range2 = new Range(10, 0, -2);

        System.out.print("Range(1,6): ");
        for (int i : range1) System.out.print(i + " ");
        System.out.println();

        System.out.print("Range(10,0,-2): ");
        for (int i : range2) System.out.print(i + " ");
        System.out.println();

        System.out.println("Range size: " + range1.size());
    }
}
