package JokerCasino.Autentication;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public abstract class SSLBase {

    private static final String TRUSTSTOREPATH = "Certs/truststore.jks";
    private static final String PASSWORD = "aps2023";
    private SSLContext sslContext;
    private KeyStore ks;
    private String keystorePath;

    /*
     * Metodo Costruttore utilizzato nel caso in cui non venga richiesto al client di validarsi per poter connettersi al server. 
     */
    public SSLBase() throws Exception {
        this.keystorePath = "";
        this.initializeSSLContext();
    }

    /**
     * Metodo Costruttore utilizzato nel caso in cui venga richiesto al client di validarsi per poter connettersi al server.
     * @param keystorePath
     * @throws Exception
     */
    public SSLBase(String keystorePath) throws Exception {
        this.keystorePath = keystorePath;
        this.initializeSSLContext();
    }

    

    /**
     * 
     * @return [SSLContext] - Istanza del contesto SSL 'sslContext'
     */
    public SSLContext getSslContext() {
        return sslContext;
    }

    /**
     * @return [KeyStore] - Istanza del KeyStore 'ks'
     * @throws KeyStoreException
     */
    public KeyStore getKs() throws KeyStoreException {
        return this.ks;
    }

    /**
     * Metodo privato che implementa la logica dietro all'inizializzazione di un constesto SSL. Prevede, quindi, di inizializzare
     * un contesto SSL 'sslContext' specificando un TrustManagerFactory ed eventualmente un KeyManagerFactory.
     * @throws Exception
     */
    private void initializeSSLContext() throws Exception {

        this.sslContext = SSLContext.getInstance("TLS");

        // Carica il truststore
        KeyStore ts = KeyStore.getInstance("JKS");
        try (InputStream tis = new FileInputStream(TRUSTSTOREPATH)) {
            ts.load(tis, PASSWORD.toCharArray());
        }

        // Inizializza il TrustManagerFactory
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        tmf.init(ts);
        
        if(!this.keystorePath.isEmpty()){
            // Carica il keystore
            this.ks = KeyStore.getInstance("JKS");
            try (InputStream kis = new FileInputStream(this.keystorePath)) {
                this.ks.load(kis, PASSWORD.toCharArray());
            }
            // Inizializza il KeyManagerFactory
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(this.ks, PASSWORD.toCharArray());
            // Inizializza il SSLContext
            this.sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        }
        else
            this.sslContext.init(null, tmf.getTrustManagers(), null);

    }

    /**
     * Tale metodo permette di poter firmare, data una chiave privata, delle informazioni espresse in byte. 
     * @param g - Informazioni in byte da firmare
     * @param privateKey - Chiave privata con cui firmare l'informazione 'g'
     * @return [byte[]] - Restituisce la firma in byte di tutti i dati caricati
     * @throws SignatureException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public byte[] signData(byte[] g, PrivateKey privateKey) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException{
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(g);
        return signature.sign();
    }

}
