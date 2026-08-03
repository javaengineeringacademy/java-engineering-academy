package academy.javaengineering.patterns.state;

// State Interface
interface OrderState {
    void next(Order order);
    void prev(Order order);
    void cancel(Order order);
    String getStatus();
}

// Concrete States
class NewOrder implements OrderState {
    @Override
    public void next(Order order) {
        System.out.println("Order confirmed, moving to processing");
        order.setState(new Processing());
    }
    
    @Override
    public void prev(Order order) {
        System.out.println("Order is already in initial state");
    }
    
    @Override
    public void cancel(Order order) {
        System.out.println("Order cancelled");
        order.setState(new Cancelled());
    }
    
    @Override
    public String getStatus() { return "NEW"; }
}

class Processing implements OrderState {
    @Override
    public void next(Order order) {
        System.out.println("Order shipped, moving to shipped");
        order.setState(new Shipped());
    }
    
    @Override
    public void prev(Order order) {
        System.out.println("Moving back to new order");
        order.setState(new NewOrder());
    }
    
    @Override
    public void cancel(Order order) {
        System.out.println("Order cancelled during processing");
        order.setState(new Cancelled());
    }
    
    @Override
    public String getStatus() { return "PROCESSING"; }
}

class Shipped implements OrderState {
    @Override
    public void next(Order order) {
        System.out.println("Order delivered");
        order.setState(new Delivered());
    }
    
    @Override
    public void prev(Order order) {
        System.out.println("Moving back to processing");
        order.setState(new Processing());
    }
    
    @Override
    public void cancel(Order order) {
        System.out.println("Cannot cancel shipped order, please return");
        order.setState(new Returned());
    }
    
    @Override
    public String getStatus() { return "SHIPPED"; }
}

class Delivered implements OrderState {
    @Override
    public void next(Order order) {
        System.out.println("Order already delivered");
    }
    
    @Override
    public void prev(Order order) {
        System.out.println("Cannot go back from delivered");
    }
    
    @Override
    public void cancel(Order order) {
        System.out.println("Order delivered, initiating return");
        order.setState(new Returned());
    }
    
    @Override
    public String getStatus() { return "DELIVERED"; }
}

class Cancelled implements OrderState {
    @Override
    public void next(Order order) {
        System.out.println("Cancelled order cannot proceed");
    }
    
    @Override
    public void prev(Order order) {
        System.out.println("Cancelled order cannot go back");
    }
    
    @Override
    public void cancel(Order order) {
        System.out.println("Order already cancelled");
    }
    
    @Override
    public String getStatus() { return "CANCELLED"; }
}

class Returned implements OrderState {
    @Override
    public void next(Order order) {
        System.out.println("Return processed, order complete");
        order.setState(new Delivered());
    }
    
    @Override
    public void prev(Order order) {
        System.out.println("Cannot go back from returned");
    }
    
    @Override
    public void cancel(Order order) {
        System.out.println("Order already returned");
    }
    
    @Override
    public String getStatus() { return "RETURNED"; }
}

// Context
class Order {
    private OrderState state;
    private final String orderId;
    
    public Order(String orderId) {
        this.orderId = orderId;
        this.state = new NewOrder();
        System.out.println("Order " + orderId + " created");
    }
    
    public void setState(OrderState state) {
        this.state = state;
        System.out.println("Order " + orderId + " state: " + state.getStatus());
    }
    
    public void next() { state.next(this); }
    public void prev() { state.prev(this); }
    public void cancel() { state.cancel(this); }
    public String getStatus() { return state.getStatus(); }
}

public class StateExample {
    public static void main(String[] args) {
        System.out.println("=== State Pattern ===\n");
        
        Order order = new Order("ORD-001");
        
        System.out.println();
        order.next();
        order.next();
        order.next();
        
        System.out.println("\n--- Trying to cancel delivered order ---");
        order.cancel();
        
        System.out.println("\n--- New order with cancel ---");
        Order order2 = new Order("ORD-002");
        System.out.println();
        order2.next();
        order2.cancel();
    }
}
