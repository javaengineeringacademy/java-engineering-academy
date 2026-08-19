package academy.javaengineering.patterns.structural.proxy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logging Proxy that logs every method call on the real image.
 * Useful for debugging, auditing, and monitoring.
 */
public class LoggingProxy implements Image {

    private final RealImage realImage;
    private final String fileName;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public LoggingProxy(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void display() {
        log("display() called");
        long start = System.nanoTime();

        RealImage image = getRealImage();
        image.display();

        long duration = (System.nanoTime() - start) / 1_000;
        log("display() completed in " + duration + "μs");
    }

    @Override
    public String getFileName() {
        log("getFileName() called");
        return fileName;
    }

    private RealImage getRealImage() {
        if (realImage == null) {
            log("Creating RealImage for: " + fileName);
            return new RealImage(fileName);
        }
        return realImage;
    }

    private void log(String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        System.out.println("[LoggingProxy] " + timestamp + " - " + message);
    }
}
