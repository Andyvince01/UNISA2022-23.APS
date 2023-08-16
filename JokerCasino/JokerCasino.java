package JokerCasino;

import JokerCasino.MusicPlayer.MusicThread;;

public class JokerCasino {

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
    
        musicThread.setPlaying(false);
    
        MusicPlayer.stop();
    }

}