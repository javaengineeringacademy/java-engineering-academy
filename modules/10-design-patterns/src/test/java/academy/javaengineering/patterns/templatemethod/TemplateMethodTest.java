package academy.javaengineering.patterns.templatemethod;

import academy.javaengineering.patterns.templatemethod.TemplateMethodExample.DataMiner;
import academy.javaengineering.patterns.templatemethod.TemplateMethodExample.CsvMiner;
import academy.javaengineering.patterns.templatemethod.TemplateMethodExample.JsonMiner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TemplateMethodTest {

    @Test
    @DisplayName("CsvMiner should be a DataMiner")
    void csvMinerShouldBeDataMiner() {
        DataMiner miner = new CsvMiner();
        assertInstanceOf(DataMiner.class, miner);
    }

    @Test
    @DisplayName("JsonMiner should be a DataMiner")
    void jsonMinerShouldBeDataMiner() {
        DataMiner miner = new JsonMiner();
        assertInstanceOf(DataMiner.class, miner);
    }

    @Test
    @DisplayName("CsvMiner should execute mine without error")
    void csvMinerShouldMineWithoutError() {
        DataMiner miner = new CsvMiner();
        assertDoesNotThrow(miner::mine);
    }

    @Test
    @DisplayName("JsonMiner should execute mine without error")
    void jsonMinerShouldMineWithoutError() {
        DataMiner miner = new JsonMiner();
        assertDoesNotThrow(miner::mine);
    }

    @Test
    @DisplayName("mine() should be final (template method pattern)")
    void mineMethodShouldBeFinal() throws Exception {
        var method = DataMiner.class.getDeclaredMethod("mine");
        assertTrue(java.lang.reflect.Modifier.isFinal(method.getModifiers()),
                "mine() should be final to enforce the template method pattern");
    }

    @Test
    @DisplayName("extractData should be abstract")
    void extractDataShouldBeAbstract() throws Exception {
        var method = DataMiner.class.getDeclaredMethod("extractData");
        assertTrue(java.lang.reflect.Modifier.isAbstract(method.getModifiers()),
                "extractData() should be abstract");
    }

    @Test
    @DisplayName("parseData should be abstract")
    void parseDataShouldBeAbstract() throws Exception {
        var method = DataMiner.class.getDeclaredMethod("parseData");
        assertTrue(java.lang.reflect.Modifier.isAbstract(method.getModifiers()),
                "parseData() should be abstract");
    }

    @Test
    @DisplayName("analyzeData should be concrete with default implementation")
    void analyzeDataShouldBeConcrete() throws Exception {
        var method = DataMiner.class.getDeclaredMethod("analyzeData");
        assertFalse(java.lang.reflect.Modifier.isAbstract(method.getModifiers()),
                "analyzeData() should have a default implementation");
    }

    @Test
    @DisplayName("Different miners should be distinct types")
    void differentMinersShouldBeDistinct() {
        DataMiner csv = new CsvMiner();
        DataMiner json = new JsonMiner();
        assertNotEquals(csv.getClass(), json.getClass(),
                "CsvMiner and JsonMiner should be different concrete classes");
    }

    @Test
    @DisplayName("DataMiner should be abstract")
    void dataMinerShouldBeAbstract() {
        assertTrue(java.lang.reflect.Modifier.isAbstract(DataMiner.class.getModifiers()),
                "DataMiner should be an abstract class");
    }
}
