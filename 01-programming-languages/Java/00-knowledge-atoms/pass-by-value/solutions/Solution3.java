public class Solution3 {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder("Hello");
        System.out.println("Before method call: " + sb);
        
        modifyStringBuilder(sb);
        
        System.out.println("After method call: " + sb);
        System.out.println("Output: Hello World (object modified through reference)");
    }

    public static void modifyStringBuilder(StringBuilder builder) {
        builder.append(" World");
    }
}