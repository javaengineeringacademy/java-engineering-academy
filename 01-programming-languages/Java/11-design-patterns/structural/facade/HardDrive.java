package academy.javaengineering.patterns.structural.facade;

public class HardDrive {
    public byte[] read(long sector, int size) {
        System.out.println("HardDrive: Reading " + size + " bytes from sector " + sector);
        return new byte[size];
    }

    public void write(long sector, byte[] data) {
        System.out.println("HardDrive: Writing " + data.length + " bytes to sector " + sector);
    }
}
