import javax.sound.sampled.*;

public class MusicPlayer {

    private static Clip clip;
    private static AudioInputStream audioInputStream;

    public static void play() {
        try {
            audioInputStream = AudioSystem.getAudioInputStream(MusicPlayer.class.getResource("Varie\\Franco Califano - La mia libertà.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            setVolume(-15.0f); // Riduci il volume di 30 decibel

            // Aggiungi un listener per ascoltare gli eventi della clip
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.START) {
                        System.out.println("Franco Califano - La mia libertà (1981)");
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
            clip.start();
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

    public static class MusicThread extends Thread {

        private boolean playing = true;

        @Override
        public void run() {
            MusicPlayer.play();
            while(playing){
                try {
                    Thread.sleep(120000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        String asciiArt = "█▀▄▀█ █▀█       █ █▀█ █▄▀ █▀▀ █▀█ ▀ █▀   █▀▀ ▄▀█ █▀ █ █▄ █ █▀█\n" +
                          "█ ▀ █ █▀▄ ▄   █▄█ █▄█ █ █ ██▄ █▀▄   ▄█   █▄▄ █▀█ ▄█ █ █ ▀█ █▄█";
        System.out.println(asciiArt);
        MusicThread musicThread = new MusicThread();
        musicThread.start();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        musicThread.playing = false;

        MusicPlayer.stop();
    }
}
