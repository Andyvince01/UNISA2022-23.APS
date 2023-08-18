package JokerCasino;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import javax.sound.sampled.*;

public class MusicPlayer {

    private static Clip clip;
    private static AudioInputStream audioInputStream;

    public static void play() {
        try {
            Path path = FileSystems.getDefault().getPath("").toAbsolutePath();
            File file = new File(path + "/Varie/Franco Califano - La mia libertà.wav");
            audioInputStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            setVolume(-15.0f); // Riduci il volume di 30 decibel

            // Aggiungi un listener per ascoltare gli eventi della clip
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.START) {
                        System.out.println("\u001B[3mFranco Califano - La mia libertà (1981)\u001B[0m\n");
                    } else if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (clip != null) {
            clip.setFramePosition(0);
            clip.loop(-1);
        }
    }

    public static void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public static void setVolume(float volume) {
        if (clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volume);
        }
    }

}
