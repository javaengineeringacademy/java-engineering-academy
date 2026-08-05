package academy.javaengineering.patterns.structural.adapter;

public class MediaAdapter implements MediaPlayer {
    private AdvancedMediaPlayer advancedMusicPlayer;

    public MediaAdapter(String audioType) {
        if (audioType.equalsIgnoreCase("vlc")) {
            advancedMusicPlayer = new VlcPlayerAdapter();
        } else if (audioType.equalsIgnoreCase("mp4")) {
            advancedMusicPlayer = new Mp4PlayerAdapter();
        }
    }

    @Override
    public void play(String audioType, String fileName) {
        if (audioType.equalsIgnoreCase("vlc")) {
            advancedMusicPlayer.playVlc(fileName);
        } else if (audioType.equalsIgnoreCase("mp4")) {
            advancedMusicPlayer.playMp4(fileName);
        }
    }
}

interface AdvancedMediaPlayer {
    void playVlc(String fileName);
    void playMp4(String fileName);
}

class VlcPlayerAdapter implements AdvancedMediaPlayer {
    private final VlcPlayer vlcPlayer = new VlcPlayer();

    @Override
    public void playVlc(String fileName) {
        vlcPlayer.playVlc(fileName);
    }

    @Override
    public void playMp4(String fileName) {
    }
}

class Mp4PlayerAdapter implements AdvancedMediaPlayer {
    private final Mp4Player mp4Player = new Mp4Player();

    @Override
    public void playVlc(String fileName) {
    }

    @Override
    public void playMp4(String fileName) {
        mp4Player.playMp4(fileName);
    }
}
