package academy.javaengineering.patterns.structural.bridge;

public class BlueColor implements Color {

    @Override
    public String fill() {
        return "Filling with blue color";
    }

    @Override
    public String getColorName() {
        return "blue";
    }
}
