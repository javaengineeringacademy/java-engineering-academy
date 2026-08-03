package academy.javaengineering.patterns.facade;

// Subsystem classes
class CPU {
    public void freeze() { System.out.println("CPU: Freezing processor"); }
    public void jump(long address) { System.out.println("CPU: Jumping to address " + address); }
    public void execute() { System.out.println("CPU: Executing instructions"); }
}

class Memory {
    public void load(long address, byte[] data) { 
        System.out.println("Memory: Loading data at address " + address); 
    }
}

class HardDrive {
    public byte[] read(long sector, int size) { 
        System.out.println("HardDrive: Reading " + size + " bytes from sector " + sector);
        return new byte[size]; 
    }
}

class BIOS {
    public void start() { System.out.println("BIOS: Starting boot sequence"); }
    public void runDiagnostic() { System.out.println("BIOS: Running diagnostic"); }
}

// Facade
class ComputerFacade {
    private final CPU cpu;
    private final Memory memory;
    private final HardDrive hardDrive;
    private final BIOS bios;
    
    public ComputerFacade() {
        this.cpu = new CPU();
        this.memory = new Memory();
        this.hardDrive = new HardDrive();
        this.bios = new BIOS();
    }
    
    public void start() {
        System.out.println("=== Starting Computer ===");
        bios.start();
        bios.runDiagnostic();
        cpu.freeze();
        byte[] bootData = hardDrive.read(0, 1024);
        memory.load(0, bootData);
        cpu.jump(0);
        cpu.execute();
        System.out.println("=== Computer Started ===\n");
    }
    
    public void shutdown() {
        System.out.println("=== Shutting Down Computer ===");
        System.out.println("CPU: Saving state");
        System.out.println("HardDrive: Flushing cache");
        System.out.println("CPU: Powering off");
        System.out.println("=== Computer Shut Down ===");
    }
}

public class FacadeExample {
    public static void main(String[] args) {
        System.out.println("=== Facade Pattern ===\n");
        
        ComputerFacade computer = new ComputerFacade();
        computer.start();
        computer.shutdown();
    }
}
