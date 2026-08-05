package academy.javaengineering.patterns.structural.facade;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FacadeTest {

    @Test
    void testComputerCreation() {
        Computer computer = new Computer();
        assertNotNull(computer);
    }

    @Test
    void testCPU() {
        CPU cpu = new CPU();
        assertNotNull(cpu);
    }

    @Test
    void testMemory() {
        Memory memory = new Memory();
        assertNotNull(memory);
    }

    @Test
    void testHardDrive() {
        HardDrive hardDrive = new HardDrive();
        assertNotNull(hardDrive);
    }

    @Test
    void testHardDriveReadWrite() {
        HardDrive hardDrive = new HardDrive();
        byte[] data = hardDrive.read(0L, 1024);
        assertNotNull(data);
        assertEquals(1024, data.length);
    }
}
