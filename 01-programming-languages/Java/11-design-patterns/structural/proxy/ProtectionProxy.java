package academy.javaengineering.patterns.structural.proxy;

/**
 * Protection Proxy that controls access to the real image based on user roles.
 * Checks permissions before delegating to the real object.
 */
public class ProtectionProxy implements Image {

    private RealImage realImage;
    private final String fileName;
    private final String userRole;

    public ProtectionProxy(String fileName, String userRole) {
        this.fileName = fileName;
        this.userRole = userRole;
    }

    @Override
    public void display() {
        if (hasAccess()) {
            getRealImage().display();
        } else {
            System.out.println("[ProtectionProxy] Access denied for role: " + userRole);
        }
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    private boolean hasAccess() {
        return "ADMIN".equals(userRole) || "EDITOR".equals(userRole);
    }

    private RealImage getRealImage() {
        if (realImage == null) {
            realImage = new RealImage(fileName);
        }
        return realImage;
    }
}
