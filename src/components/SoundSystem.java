package components;

import javax.sound.sampled.*;
import java.io.IOException;

public class SoundSystem {

    AudioInputStream AIS;
    Clip audio;

    public SoundSystem(String path) {
        try {
            AIS = AudioSystem.getAudioInputStream(this.getClass().getResourceAsStream(path));
            audio = AudioSystem.getClip();
            this.AIS = AIS;
            this.audio = audio;
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    public void playSound() {
        try {
            if (!this.audio.isOpen())
                audio.open(this.AIS);
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
        audio.loop(1);
    }

    public void stopSound() {
        audio.stop();
        audio.close();
    }
}