package academy.javaengineering.patterns.behavioral.templatemethod;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TemplateMethodTest {

    @Test
    void csvMinerShouldReturnCorrectFileName() {
        DataMiner miner = new CSVDataMiner();
        assertEquals("data.csv", miner.getFileName());
    }

    @Test
    void jsonMinerShouldReturnCorrectFileName() {
        DataMiner miner = new JSONDataMiner();
        assertEquals("data.json", miner.getFileName());
    }

    @Test
    void csvMinerShouldImplementAllSteps() {
        DataMiner miner = new CSVDataMiner();
        assertNotNull(miner);
        assertDoesNotThrow(miner::mine);
    }

    @Test
    void jsonMinerShouldImplementAllSteps() {
        DataMiner miner = new JSONDataMiner();
        assertNotNull(miner);
        assertDoesNotThrow(miner::mine);
    }

    @Test
    void minersShouldBeDifferentTypes() {
        DataMiner csv = new CSVDataMiner();
        DataMiner json = new JSONDataMiner();
        assertNotEquals(csv.getClass(), json.getClass());
    }
}
