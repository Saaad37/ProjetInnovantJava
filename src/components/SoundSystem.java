package components;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;

public class SoundSystem {

    AudioInputStream AIS;
    Clip audio;

    public SoundSystem(String path) {
        try {
            this.AIS = AudioSystem.getAudioInputStream(new BufferedInputStream(this.getClass().getResourceAsStream(path
            )));
            this.audio = AudioSystem.getClip();
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    public void playSound(int numLoops) {
        try {
            if (!this.audio.isOpen())
                audio.open(this.AIS);
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
        audio.loop(numLoops);
    }

    public void stopSound() {
        audio.stop();
        audio.close();
    }
}