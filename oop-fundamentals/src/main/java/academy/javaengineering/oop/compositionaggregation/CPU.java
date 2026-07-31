package academy.javaengineering.oop.compositionaggregation;

/**
 * CPU - Part of Computer composition relationship.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class CPU {

    private final String model;
    private final int cores;

    public CPU(String model, int cores) {
        this.model = model;
        this.cores = cores;
    }

    public String getSpecification() {
        return model + " (" + cores + " cores)";
    }

    public String getModel() { return model; }
    public int getCores() { return cores; }
}