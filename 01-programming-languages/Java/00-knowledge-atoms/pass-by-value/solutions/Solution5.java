public class Solution5 {
    public static void main(String[] args) {
        String name = "Alice";
        System.out.println("Before method call: " + name);
        
        changeName(name);
        
        System.out.println("After method call: " + name);
        System.out.println("Output: Alice (Strings are immutable)");
    }

    public static void changeName(String n) {
        n = "Bob";
    }
}