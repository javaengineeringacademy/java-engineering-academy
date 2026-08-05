package academy.javaengineering.patterns.structural.facade;

public class FacadeExample {
    public static void main(String[] args) {
        Computer computer = new Computer();
        computer.start();
        System.out.println("---");
        computer.shutDown();
    }
}
