package academy.javaengineering.patterns.structural.facade;

public class CPU {
    public void freeze() {
        System.out.println("CPU: Freezing processor.");
    }

    public void jump(long address) {
        System.out.println("CPU: Jumping to address " + address);
    }

    public void execute() {
        System.out.println("CPU: Executing instructions.");
    }
}
