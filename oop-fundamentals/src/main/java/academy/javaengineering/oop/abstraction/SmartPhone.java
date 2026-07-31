package academy.javaengineering.oop.abstraction;

/**
 * SmartPhone - Demonstrates implementing multiple interfaces.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class SmartPhone implements Phone, Camera, MusicPlayer, InternetConnectable {

    private final String brand;
    private final int model;

    public SmartPhone(String brand, int model) {
        this.brand = brand;
        this.model = model;
    }

    @Override
    public void call(String number) {
        System.out.println("  " + brand + " calling " + number);
    }

    @Override
    public String getPhoneNumber() {
        return "555-" + model + "000";
    }

    @Override
    public void takePhoto() {
        System.out.println("  " + brand + " taking photo with " + getResolution() + "MP camera");
    }

    @Override
    public int getResolution() {
        return 48;
    }

    @Override
    public void playMusic() {
        System.out.println("  " + brand + " playing music");
    }

    @Override
    public void connectToInternet() {
        System.out.println("  " + brand + " connected to 5G network");
    }

    @Override
    public String toString() {
        return brand + " Model " + model;
    }
}