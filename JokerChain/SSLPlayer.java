package JokerChain;

import JokerChain.Utils.Generator;
import JokerChain.Utils.SSLConnection;

import java.security.KeyPair;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import javax.net.ssl.SSLSocket;

public class SSLPlayer extends SSLConnection{

    // private static final int PORT = 4001;
    private Player p;
    private static final Semaphore inputSemaphore = new Semaphore(1);

    /**
     * Costruttore di SSLPlayer. Inizializza un nuovo oggetto SSLPlayer con un'istanza di Player.
     * 
     * @param p - L'istanza del Player da associare a questo SSLPlayer.
     * @throws Exception Se si verifica un errore durante l'inizializzazione.
     */
    public SSLPlayer(Player p) throws Exception {
        super();
        this.p = p;
    }

    /**
     * Restituisce l'istanza del Player associata a questo SSLPlayer.
     * 
     * @return L'istanza del Player 'p'.
     */
    public Player getPlayer() {
        return p;
    }

    /**
     * Imposta un'istanza del Player per questo SSLPlayer.
     * 
     * @param p - L'istanza del Player da impostare.
     */
    public void setPlayer(Player p) {
        this.p = p;
    }

    @Override
    protected void handleChiavi(SSLSocket socket, SSLADM ssladm) throws Exception {
        KeyPair keyPair = Generator.generateKeyPair();
        this.p.setKeyPair(keyPair);
        SSLConnection.sendData(socket, keyPair.getPublic());
        ssladm.awaitCompletion();
    }

    @Override
    protected void handleGiocate(SSLSocket socket, SSLADM ssladm) throws Exception {
        try(Scanner scanner = new Scanner(System.in)){
            inputSemaphore.acquire();
            Thread.sleep(500);
            boolean invalid = false;
            // Numero
            System.out.print("(" + this.p.getNickname() + ") Scegli un numero (0-36 | \"\"): ");
            String numero = scanner.nextLine();
            if(numero != "" && !(Integer.parseInt(numero) >= 0 && Integer.parseInt(numero) <= 36))
                invalid = true;
            // Colore
            System.out.print("(" + this.p.getNickname() + ") Scegli un colore (nero | rosso | \"\"): ");
            String colore = scanner.nextLine();
            if(!colore.contains("nero") && !colore.contains("rosso") && colore != "")
                invalid = true;
            // Pari o Dispari
            System.out.print("(" + this.p.getNickname() + ") Scegli pari o dispari (pari | dispari | \"\"): ");
            String parita = scanner.nextLine();
            if(!parita.contains("pari") && !parita.contains("dispari") && parita != "")
                invalid = true;
            if(numero == "" && colore == "" && parita == "")
                invalid = true;
            
            if (!invalid)
                this.p.setGiocata(new Giocata(numero, colore, parita));
            else
                this.p.setGiocata(new Giocata("", "", ""));

            byte[] signedG = signData(this.p.getGiocata().getBit().getBytes(), this.p.getKeyPair().getPrivate());
            SSLConnection.sendData(socket, this.p.getGiocata().getBit());
            SSLConnection.sendData(socket, signedG);
            inputSemaphore.release();
            ssladm.awaitCompletion();    
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void handleStringheCasuali(SSLSocket socket, SSLADM ssladm) throws Exception {
        if(!this.p.getGiocata().getBit().equals("00000000000")){
            byte[] prng = Generator.generatePRG();
            byte[] signedPrng = signData(prng, this.p.getKeyPair().getPrivate());
            SSLConnection.sendData(socket, prng);
            SSLConnection.sendData(socket, signedPrng);    
        }
        else
            SSLConnection.sendData(socket, null);
        ssladm.awaitCompletion();
    }

    @Override
    protected void handleRisultato(SSLSocket socket, SSLADM ssladm) throws Exception {
        SSLConnection.sendData(socket, null);
    }

}
