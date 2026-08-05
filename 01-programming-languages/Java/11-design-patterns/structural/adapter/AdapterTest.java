package academy.javaengineering.patterns.structural.adapter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdapterTest {

    @Test
    void testMediaPlayerInterface() {
        MediaPlayer player = new AudioPlayer();
        assertNotNull(player);
    }

    @Test
    void testMediaAdapterCreation() {
        MediaAdapter vlcAdapter = new MediaAdapter("vlc");
        MediaAdapter mp4Adapter = new MediaAdapter("mp4");
        assertNotNull(vlcAdapter);
        assertNotNull(mp4Adapter);
    }

    @Test
    void testAudioPlayerCreation() {
        AudioPlayer player = new AudioPlayer();
        assertNotNull(player);
        assertTrue(player instanceof MediaPlayer);
    }

    @Test
    void testVlcPlayerAdaptee() {
        VlcPlayer vlc = new VlcPlayer();
        assertNotNull(vlc);
    }

    @Test
    void testMp4PlayerAdaptee() {
        Mp4Player mp4 = new Mp4Player();
        assertNotNull(mp4);
    }
}
