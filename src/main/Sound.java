package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {
    Clip clip;
    URL soundURL[] = new URL[10];

    public Sound() {
        soundURL[0] = getClass().getResource("/res/sounds/game_start.wav");
        soundURL[1] = getClass().getResource("/res/sounds/bullet_shot.wav");
        soundURL[2] = getClass().getResource("/res/sounds/explosion_1.wav");
        soundURL[3] = getClass().getResource("/res/sounds/level_up.wav");
        soundURL[4] = getClass().getResource("/res/sounds/game_over.wav");
        soundURL[5] = getClass().getResource("/res/sounds/item_pickup.wav");
        soundURL[6] = getClass().getResource("/res/sounds/game_win.wav");

    }
    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            clip.setFramePosition(0); // Rewind to the beginning
            clip.start();
        }
    }

    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }

}
