package JokerChain;

import JokerCasino.Player;
import JokerCasino.Autentication.SSLBase;
import JokerChain.Utils.Generator;
import JokerChain.Utils.SocketUtils;

import java.math.BigInteger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SSLBanco extends SSLBase{

    // private static final int PORT = 4001;
    private static final int ADM_PORT = 4002; 
    private static final String ADM_HOST = "localhost";
    private Player banco;
    private JokerChain jokerChain;
    private SSLContext sslContext;

    /**
     * Metodo Costruttore
     * @param keystorePath - Path al keystore del banco, contenente il certificato (joker_cert.pem) e la relativa chiave privata.
     * @param banco - Istanza della classe Player, che rappresenta il banco di gioco.
     * @param jokerChain - Istanza della JokerChain.
     * @throws Exception
     */
    public SSLBanco(String keystorePath, Player banco, JokerChain jokerChain) throws Exception {
        super(keystorePath);
        this.sslContext = getSslContext();
        this.banco = banco;
        this.jokerChain = jokerChain;
    }

    public void connectTo(SSLADM ssladm) {
        try {
            SSLSocketFactory ssf = this.sslContext.getSocketFactory();
            SSLSocket socket = (SSLSocket) ssf.createSocket(ADM_HOST, ADM_PORT);
            socket.startHandshake();

            // Connessione
            SocketUtils.sendData(socket, this.banco.getNickname());
            
            // Blocco Chiavi
            this.banco.setKeyPair(Generator.generateKeyPair());
            SocketUtils.sendData(socket, this.banco.getKeyPair().getPublic());
            ssladm.awaitCompletion();

            // Blocco Giocate
            SocketUtils.sendData(socket, null);
            ssladm.awaitCompletion();

            // Blocco Stringhe Casuali
            byte[] prng = Generator.generatePRG();
            byte[] signedPrng = signData(prng, this.banco.getKeyPair().getPrivate());
            SocketUtils.sendData(socket, prng);
            SocketUtils.sendData(socket, signedPrng);
            ssladm.awaitCompletion();

            // Blocco Risultato
            String merkleRoot = this.jokerChain.calculateMerkleRootForType(4);
            String risultato = String.valueOf(new BigInteger(merkleRoot, 16).mod(new BigInteger("37")).intValue());
            byte[] signedRisultato = signData(risultato.getBytes(), this.banco.getKeyPair().getPrivate());
            SocketUtils.sendData(socket, risultato);
            SocketUtils.sendData(socket, signedRisultato);

            socket.close();

        } catch (Exception ex) {
            ex.printStackTrace();            
        }
    }

    public Player getBanco() {
        return banco;
    }

    public void setBanco(Player banco) {
        this.banco = banco;
    }  

}
