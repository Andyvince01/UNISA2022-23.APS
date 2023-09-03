package JokerCasino.Autentication;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

public abstract class SSLBase {

    private static final String TRUSTSTOREPATH = "Certs/truststore.jks";
    private static final String PASSWORD = "aps2023";
    /**
     * Invia dati attraverso una connessione SSL.
     * 
     * @param socket - Il socket SSL attraverso il quale inviare i dati.
     * @param data - L'oggetto dati da inviare.
     * @throws IOException Se si verifica un errore durante l'invio dei dati.
     */
    public static void sendData(SSLSocket socket, Object data) throws IOException {
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.writeObject(data);
        outputStream.flush();
    }
    /**
     * Riceve dati attraverso una connessione SSL.
     * 
     * @param socket - Il socket SSL da cui ricevere i dati.
     * @return [Object] L'oggetto dati ricevuto.
     * @throws IOException Se si verifica un errore durante la ricezione dei dati.
     * @throws ClassNotFoundException Se la classe dell'oggetto ricevuto non può essere trovata.
     */
    public static Object receiveData(SSLSocket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        return inputStream.readObject();
    }
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
     * Tale metodo permette di poter firmare, data una chiave privata, delle informazioni espresse in byte. 
     * @param g - Informazioni in byte da firmare.
     * @param privateKey - Chiave privata con cui firmare l'informazione 'g'.
     * @return [byte[]] - Restituisce la firma in byte di tutti i dati caricati.
     */
    public byte[] signData(byte[] g, PrivateKey privateKey){
        try{
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(g);
            return signature.sign();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }        
    }

    /**
     * Tale metodo permette di poter verificare la firma digitale firmare, data una chiave privata, delle informazioni espresse in byte. 
     * @param data - Informazion in byte da verificare.
     * @param signatureBytes - Firma in byte dei dati firmati con il metodo signData.
     * @param publicKey - Chiave pubblica con cui verifcare la firma.
     * @return Restituisce true se la firma è valida. Altrimenti, false.
     */
    public boolean verifySignature(byte[] data, byte[] signatureBytes, PublicKey publicKey) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(data);
            return signature.verify(signatureBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }    
    }

    /**
     * Metodo privato che implementa la logica dietro all'inizializzazione di un contesto SSL. Prevede, quindi, di inizializzare
     * un contesto SSL 'sslContext' specificando un TrustManagerFactory ed eventualmente un KeyManagerFactory.
     * @throws Exception - Lancia un'eccezione in caso di errori durante l'inizializzazione del contesto SSL 
     * (e.g. NoSuchAlgorithmException, KeyStoreException, etc.).
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


}
