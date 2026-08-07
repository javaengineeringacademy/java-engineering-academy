public class SwapDemo {
    public static void main(String[] args) {
        int x = 1;
        int y = 2;
        System.out.println("Before swap: x=" + x + ", y=" + y);
        
        swap(x, y);
        
        System.out.println("After swap: x=" + x + ", y=" + y);
        System.out.println("Expected: x=1, y=2 (swap doesn't work with primitives)");
    }

    public static void swap(int a, int b) {
        System.out.println("Inside method, before swap: a=" + a + ", b=" + b);
        int temp = a;
        a = b;
        b = temp;
        System.out.println("Inside method, after swap: a=" + a + ", b=" + b);
    }
}