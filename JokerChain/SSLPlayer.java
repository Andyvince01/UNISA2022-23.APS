package JokerChain;

import JokerCasino.Player;
import JokerCasino.Autentication.SSLBase;
import JokerChain.Utils.Generator;
import JokerChain.Utils.SocketUtils;

import java.util.Scanner;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SSLPlayer extends SSLBase{

    // private static final int PORT = 4001;
    private static final int ADM_PORT = 4002; 
    private static final String ADM_HOST = "localhost";
    private SSLContext sslContext;
    private Player p;
    private Giocata g;

    /**
     * Metodo Costruttore
     * @param p
     * @throws Exception
     */
    public SSLPlayer(Player p) throws Exception {
        super();
        this.sslContext = getSslContext();
        this.p = p;
    }

    /**
     * Tale metodo permette di poter collegare il giocatore 'p' al nodo ADM. In particolare,
     * una volta connesso, prevede di generare una coppia di chiavi pubblica e privata, di
     * effettuare eventualmente una giocata e, nel caso in cui ciò fosse vero, di generare una
     * stringa casuale, utilizzata in seguito per il processo di generazione del risultato.
     * 
     * @param ssladm - Istanza della classe SSLADM per l'esecuzione sincrona delle operazioni
     * eseguite dai player e dal banco.
     */
    public void connectTo(SSLADM ssladm) {
        try {
            SSLSocketFactory ssf = this.sslContext.getSocketFactory();
            SSLSocket socket = (SSLSocket) ssf.createSocket(ADM_HOST, ADM_PORT);
            socket.startHandshake();

            // Connessione
            SocketUtils.sendData(socket, this.p.getNickname());

            // Blocco Chiavi
            this.p.setKeyPair(Generator.generateKeyPair());
            SocketUtils.sendData(socket, this.p.getKeyPair().getPublic());
            ssladm.awaitCompletion();

            // Blocco Giocate
            String g = generazioneGiocata();
            byte[] signedG = signData(g.getBytes(), this.p.getKeyPair().getPrivate());
            SocketUtils.sendData(socket, g);
            SocketUtils.sendData(socket, signedG);
            ssladm.awaitCompletion();

            // Blocco Stringhe Casuali
            if(g != ""){
                byte[] prng = Generator.generatePRG();
                byte[] signedPrng = signData(prng, this.p.getKeyPair().getPrivate());
                SocketUtils.sendData(socket, prng);
                SocketUtils.sendData(socket, signedPrng);    
            }
            else
                SocketUtils.sendData(socket, null);
            ssladm.awaitCompletion();

            // Blocco Risultato
            SocketUtils.sendData(socket, null);
                
            socket.close();

        } catch (Exception ex) {
            ex.printStackTrace();            
        }
    }

    /** 
     * @return [Player] - Istanza del player 'p'
     */
    public Player getPlayer() {
        return p;
    }

    /**
     * Settiamo un'istanza del player 'p'
     * @param p
     */
    public void setPlayer(Player p) {
        this.p = p;
    }

    /**
     * Tale metodo chiede al player 'p' di effettuare una giocata a linea di comando. In particolare,
     * viene chiesto al player di scegliere un numero, un colore o una scelta tra pari e dispari in modo
     * da poter effettuare una valida giocata. 
     * @return [String] - Restituisce la giocata effettuata. La giocata "00000000000" indica che non è stata
     * effettuata alcuna scelta dal player e quindi la giocata non ha alcun valore.
     * @throws Exception
     */
    private String generazioneGiocata() throws Exception{
        try(Scanner scanner = new Scanner(System.in)){
            boolean invalid = false;
            // Numero
            System.out.print("(" + this.p.getNickname() + ") Scegli un numero (0-36 | null): ");
            String numero = scanner.nextLine();
            if(numero != "" && !(Integer.parseInt(numero) >= 0 && Integer.parseInt(numero) <= 36))
                invalid = true;
            // Colore
            System.out.print("(" + this.p.getNickname() + ") Scegli un colore (nero | rosso | null): ");
            String colore = scanner.nextLine();
            if(!colore.contains("nero") && !colore.contains("rosso") && colore != "")
                invalid = true;
            // Pari o Dispari
            System.out.print("(" + this.p.getNickname() + ") Scegli pari o dispari (pari | dispari | null): ");
            String parita = scanner.nextLine();
            if(!parita.contains("pari") && !parita.contains("dispari") && parita != "")
                invalid = true;
            if(numero == "" && colore == "" && parita == "")
                invalid = true;
            
            String g = (!invalid) ? new Giocata(numero, colore, parita).getBit() : new Giocata("", "", "").getBit();
            
            return g;
        }
    }

}
