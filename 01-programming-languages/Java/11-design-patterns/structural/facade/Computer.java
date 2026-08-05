package academy.javaengineering.patterns.structural.facade;

public class Computer {
    private final CPU cpu;
    private final Memory memory;
    private final HardDrive hardDrive;

    public Computer() {
        this.cpu = new CPU();
        this.memory = new Memory();
        this.hardDrive = new HardDrive();
    }

    public void start() {
        System.out.println("Computer: Starting up...");
        cpu.freeze();
        memory.load(0L, "boot");
        cpu.jump(0L);
        cpu.execute();
    }

    public void shutDown() {
        System.out.println("Computer: Shutting down...");
        cpu.freeze();
    }
}
