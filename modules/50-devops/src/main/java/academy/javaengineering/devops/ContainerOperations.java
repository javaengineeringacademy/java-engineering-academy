package academy.javaengineering.devops;

/**
 * Demonstrates container operations.
 */
public class ContainerOperations {

    public record ContainerInfo(
        String id,
        String name,
        String image,
        String status,
        int port
    ) {}

    public ContainerInfo buildImage(String dockerfile, String imageName) {
        System.out.println("Building Docker image: " + imageName);
        return new ContainerInfo("abc123", imageName, "latest", "built", 8080);
    }

    public ContainerInfo runContainer(String image, int port) {
        System.out.println("Running container: " + image);
        return new ContainerInfo("def456", "app", image, "running", port);
    }

    public void stopContainer(String containerId) {
        System.out.println("Stopping container: " + containerId);
    }

    public void removeContainer(String containerId) {
        System.out.println("Removing container: " + containerId);
    }
}
