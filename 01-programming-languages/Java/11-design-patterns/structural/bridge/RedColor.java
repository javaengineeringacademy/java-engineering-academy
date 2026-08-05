package academy.javaengineering.patterns.structural.bridge;

public class RedColor implements Color {

    @Override
    public String fill() {
        return "Filling with red color";
    }

    @Override
    public String getColorName() {
        return "red";
    }
}
