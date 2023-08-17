package JokerCasino;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;


public class JokerCasino {

    public static void Login(){
        MusicPlayer.play();
        boolean repeat = false;
        List<String> info_player = new ArrayList<String>();

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Inserisci path del certificato per accedere:");

            do{
                try{
                    String cert_pem = scanner.nextLine();
                    repeat = false;
                    info_player = PemReader.read(cert_pem);
                }catch(Exception e){
                    System.out.println("Errore! Inserisci un path valido del certificato per accedere:");
                    repeat = true;
                }
            } while(repeat);

            scanner.close();
        }
        for(String info: info_player)
           System.out.println(info);
                      
        MusicPlayer.stop();
    }


    public static void main(String[] args) {
        System.out.println("\u001B[31m\u001B[1mMr. Joker's Casino \u001B[0m");

        Login();


    }

}