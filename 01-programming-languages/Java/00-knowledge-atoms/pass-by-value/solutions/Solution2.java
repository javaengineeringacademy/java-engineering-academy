public class Solution2 {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5};
        System.out.println("Before method call: " + arr[0]);
        
        modifyArray(arr);
        
        System.out.println("After method call: " + arr[0]);
        System.out.println("Output: 100 (array contents modified)");
    }

    public static void modifyArray(int[] a) {
        a[0] = 100;
    }
}