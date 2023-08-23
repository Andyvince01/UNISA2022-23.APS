package JokerCasino;

import JokerCasino.Autentication.SSLJoker;
import JokerCasino.Autentication.SSLMS;
import JokerCasino.Autentication.SSLCitizen;
import JokerChain.JokerChain;
import JokerChain.Player;
import JokerChain.SSLADM;
import JokerChain.SSLBanco;
import JokerChain.SSLPlayer;

import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JokerCasino {

    public static boolean Login(Player p, Scanner scanner) throws InterruptedException{

        String player_cert = "";

        // Lettura Path GP2.0 del Player
        boolean flag = true;
        do{
            if(flag)
                System.out.print("Per poter accedere, inserisca il path del suo GP 2.0: ");
            else
                System.out.print("Errore! Per poter accedere, inserisca un path valido: ");
            player_cert = scanner.nextLine();
            if (!Files.exists(Paths.get(player_cert)) || player_cert == "")
                flag = false;
            else   
                flag = true;
        }while(!flag);      

        // Procedura di Autenticazione
        try {
            // System.setProperty("javax.net.debug", "ssl:handshake");

            // Creare l'istanza di SSLMSIdP
            SSLMS sslMS = new SSLMS("Certs/ms_keystore.jks");

            new Thread(() -> {
                sslMS.startConnection();
            }).start();

            // Creare l'istanza di SSLJoker
            SSLJoker sslJoker = new SSLJoker("Certs/joker_keystore.jks", p);

            new Thread(() -> {
                sslJoker.startConnection();
            }).start();
        
            // Creare l'istanza di SSLPlayer
            SSLCitizen sslPlayer = new SSLCitizen(player_cert);

            Thread.sleep(1000);
            sslPlayer.connectToMS();           // Port 4000 → IdP MS

            new Thread(() -> {
                sslPlayer.startConnection();
            }).start();

            Thread.sleep(1000);
            return !p.equals(null) ? true : false;

        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }            

    }

    public static void main(String[] args) throws Exception {
        System.out.println("\u001B[41m\u001B[1m Mr. Joker's Casino \u001B[0m");

        // Riproduttore Musica
        MusicPlayer.play();
        Thread.sleep(1000);

        // Single Scanner instance for the entire program
        Scanner scanner = new Scanner(System.in);
    
        // LOGIN
        Player p = new Player();
        boolean authorized = Login(p, scanner);  // Pass the scanner to the Login method if required
    
        if(!authorized){
            System.out.println("Accesso non autorizzato. Terminazione del programma.");
            System.exit(0);
        }

        // GIOCO

        // Scelta Nickname
        System.out.print("Inserisci un nickname per la sala da gioco: ");
        String nickname = scanner.nextLine();
        if(nickname == null || nickname.trim().isEmpty())
            p.setNickname(p.getCodiceFiscale());
        else
            p.setNickname(nickname);
            
        System.out.println("Benvenuto "+ p.getNickname() + "\n");

        // JokerChain
        JokerChain jokerChain = new JokerChain(true);

        // Istanze
        SSLADM sslAdm = new SSLADM("Certs/adm_keystore.jks", jokerChain);
        SSLBanco sslBanco = new SSLBanco("Certs/joker_keystore.jks", new Player("Banco"), jokerChain);
        SSLPlayer sslPlayer = new SSLPlayer(p);
        SSLPlayer sslPlayerAlice = new SSLPlayer(new Player("Alice"));
        SSLPlayer sslPlayerBob = new SSLPlayer(new Player("Bob"));

        new Thread(() -> {
            sslAdm.startConnection();
        }).start();

        new Thread(() -> {
            sslBanco.connectTo(sslAdm, new Player("Banco").getNickname());
        }).start();

        Thread.sleep(100);
        new Thread(() -> {
            sslPlayer.connectTo(sslAdm, p.getNickname());
        }).start();

        Thread.sleep(100);
        new Thread(() -> {
            sslPlayerAlice.connectTo(sslAdm, "Alice");
        }).start();

        Thread.sleep(100);
        new Thread(() -> {
            sslPlayerBob.connectTo(sslAdm, "Bob");
        }).start();

    }   

}