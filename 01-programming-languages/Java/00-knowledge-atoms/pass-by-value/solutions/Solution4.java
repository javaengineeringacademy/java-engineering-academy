public class Solution4 {
    public static void main(String[] args) {
        int a = 10;
        int b = 20;
        System.out.println("Before swap: a=" + a + ", b=" + b);
        
        swap(a, b);
        
        System.out.println("After swap: a=" + a + ", b=" + b);
        System.out.println("Output: a=10, b=20 (swap doesn't work with primitives)");
    }

    public static void swap(int x, int y) {
        int temp = x;
        x = y;
        y = temp;
    }
}