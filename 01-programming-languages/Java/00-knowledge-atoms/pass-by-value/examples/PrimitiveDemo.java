public class PrimitiveDemo {
    public static void main(String[] args) {
        int num = 10;
        System.out.println("Before method call: " + num);
        
        modifyPrimitive(num);
        
        System.out.println("After method call: " + num);
        System.out.println("Expected: 10 (unchanged)");
    }

    public static void modifyPrimitive(int x) {
        System.out.println("Inside method, before modification: " + x);
        x = 100;
        System.out.println("Inside method, after modification: " + x);
    }
}