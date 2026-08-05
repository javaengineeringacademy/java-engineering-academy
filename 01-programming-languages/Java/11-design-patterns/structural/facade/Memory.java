package academy.javaengineering.patterns.structural.facade;

public class Memory {
    public void load(long address, String data) {
        System.out.println("Memory: Loading data '" + data + "' to address " + address);
    }
}
