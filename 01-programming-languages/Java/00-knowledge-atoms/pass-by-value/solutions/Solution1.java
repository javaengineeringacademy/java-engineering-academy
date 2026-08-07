public class Solution1 {
    public static void main(String[] args) {
        int num = 5;
        System.out.println("Before method call: " + num);
        
        doubleValue(num);
        
        System.out.println("After method call: " + num);
        System.out.println("Output: 5 (unchanged)");
    }

    public static void doubleValue(int x) {
        x = x * 2;
        System.out.println("Inside method: " + x);
    }
}