package JokerCasino;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
public class JokerCasino {

    public static void Login(){
        MusicPlayer.play();

        try {

            // System.setProperty("javax.net.debug", "ssl:handshake");

            // Creare l'istanza di MSIdP
            MSIdP msIdp = new MSIdP("Certs/GP2.0/ms_keystore.jks");

            // Creare l'istanza di JokerServer
            JokerServer jokerServer = new JokerServer();
                                    
            // Creare l'istanza di PlayerClient
            PlayerClient pi = new PlayerClient("Certs/GP2.0/andy_keystore.jks");

            // Avviare il server MSIdP in un nuovo thread
            new Thread(() -> {
                msIdp.startServer();
            }).start();
            
            new Thread(() -> {
                jokerServer.startServer();
            }).start();

            // Dopo aver atteso un po' per assicurarti che il server sia avviato, avviare il client Andy
            Thread.sleep(2000);
            String response = pi.connectAndFetchResponse();       // Port 4000 → IdP MS

            System.out.println(response);

        } catch (Exception e) {
            e.printStackTrace();
        }      

        MusicPlayer.stop();
    }


    public static void main(String[] args) {
        System.out.println("\u001B[41m\u001B[1m Mr. Joker's Casino \u001B[0m");

        Login();

    }

}