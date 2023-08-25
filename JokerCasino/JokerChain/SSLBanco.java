package JokerCasino.JokerChain;

import java.math.BigInteger;
import java.security.KeyPair;
import javax.net.ssl.SSLSocket;

import JokerCasino.JokerChain.Utils.Generator;
import JokerCasino.JokerChain.Utils.SSLConnection;

public class SSLBanco extends SSLConnection{

    private Player banco;
    private JokerChain jokerChain;

    /**
     * Costruttore di SSLBanco. Inizializza un nuovo oggetto SSLBanco con un percorso del keystore, 
     * un'istanza di Player e un'istanza di JokerChain.
     * 
     * @param keystorePath Il percorso del keystore da utilizzare per la connessione SSL.
     * @param banco L'istanza del Player che rappresenta il banco.
     * @param jokerChain L'istanza di JokerChain associata a questo SSLBanco.
     * @throws Exception Se si verifica un errore durante l'inizializzazione.
     */
    public SSLBanco(String keystorePath, Player banco, JokerChain jokerChain) throws Exception {
        super(keystorePath);
        this.banco = banco;
        this.jokerChain = jokerChain;
    }

    /**
     * Restituisce l'istanza del Player associata a questo SSLBanco.
     * 
     * @return L'istanza del Player 'banco'.
     */
    public Player getBanco() {
        return banco;
    }

    /**
     * Imposta un'istanza del Player per questo SSLBanco.
     * 
     * @param p - L'istanza del Player 'banco' da impostare.
     */
    public void setBanco(Player banco) {
        this.banco = banco;
    }

    @Override
    protected void handleChiavi(SSLSocket socket, SSLADM ssladm) throws Exception {
        KeyPair keyPair = Generator.generateKeyPair();
        this.banco.setKeyPair(keyPair);
        SSLConnection.sendData(socket, keyPair.getPublic());
        ssladm.awaitCompletion();
    }

    @Override
    protected void handleGiocate(SSLSocket socket, SSLADM ssladm) throws Exception {
        SSLConnection.sendData(socket, null);
        ssladm.awaitCompletion();
    }

    @Override
    protected void handleStringheCasuali(SSLSocket socket, SSLADM ssladm) throws Exception {
        byte[] prng = Generator.generatePRG();
        byte[] signedPrng = signData(prng, this.banco.getKeyPair().getPrivate());
        SSLConnection.sendData(socket, prng);
        SSLConnection.sendData(socket, signedPrng);
        ssladm.awaitCompletion();
    }

    @Override
    protected void handleRisultato(SSLSocket socket, SSLADM ssladm) throws Exception {
        String merkleRoot = this.jokerChain.calculateMerkleRootForType(4);
        String risultato = String.valueOf(new BigInteger(merkleRoot, 16).mod(new BigInteger("37")).intValue());
        byte[] signedRisultato = signData(risultato.getBytes(), this.banco.getKeyPair().getPrivate());
        SSLConnection.sendData(socket, risultato);
        SSLConnection.sendData(socket, signedRisultato);
    }  

}
