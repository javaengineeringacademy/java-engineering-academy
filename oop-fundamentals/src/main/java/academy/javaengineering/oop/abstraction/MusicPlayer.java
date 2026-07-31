package academy.javaengineering.oop.abstraction;

/**
 * MusicPlayer - Interface for music playback functionality.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public interface MusicPlayer {

    void playMusic();

    default void pauseMusic() {
        System.out.println("  Music paused");
    }

    default void stopMusic() {
        System.out.println("  Music stopped");
    }
}