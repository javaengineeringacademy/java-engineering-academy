package academy.javaengineering.oop.compositionaggregation;

/**
 * Computer - Demonstrates deep composition with multiple parts.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class Computer {

    private final CPU cpu;
    private final int ramGB;
    private final int storageGB;

    public Computer(String cpuModel, int ramGB, int storageGB) {
        this.cpu = new CPU(cpuModel, 8);
        this.ramGB = ramGB;
        this.storageGB = storageGB;
    }

    public String getSpecification() {
        return "CPU: " + cpu.getSpecification() +
            ", RAM: " + ramGB + "GB" +
            ", Storage: " + storageGB + "GB";
    }

    public CPU getCpu() { return cpu; }
    public int getRamGB() { return ramGB; }
    public int getStorageGB() { return storageGB; }
}