package JokerCasino.JokerChain;

import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import JokerCasino.Autentication.SSLBase;
import JokerCasino.JokerChain.Utils.SSLConnection;

public class SSLADM extends SSLBase{

    private static final int PORT = 4002;
    private SSLContext sslContext;
    private JokerChain jokerChain;
    private KeyStore ks;
    private CountDownLatch countDownLatch;
    private static final Semaphore inputSemaphore = new Semaphore(1);

    /**
     * Costruttore di SSLADM.
     * @param keystorePath - Path al keystore del nodo ADM.
     * @param jokerChain - Istanza della JokerChain.
     * @throws Exception Lancia un'eccezione nel caso di errori durante la fase di connessione.
     */
    public SSLADM(String keystorePath, JokerChain jokerChain) throws Exception {
        super(keystorePath);
        this.sslContext = getSslContext();
        this.jokerChain = jokerChain;
        this.ks = getKs();
        this.countDownLatch = new CountDownLatch(4);
    }

    /**
     * Inizializza la connessione del nodo ADM. Una volta configurata, qualunque peer, anche coloro sprovvisti di certificato,
     * possono connettersi al nodo ADM. 
     */
    public void startConnection() {
        try {
            SSLServerSocketFactory ssf = this.sslContext.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(PORT);
            serverSocket.setWantClientAuth(true);

            System.out.println("\u001B[33m(SSLADM) In attesa di connessioni ...\u001B[0m");
            int count = 0;
            while(count < 4){
                final SSLSocket socket = (SSLSocket) serverSocket.accept();
                new Thread(() -> handleClientConnection(socket)).start();;
                count++;
            }
            serverSocket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Per ogni peer connesso alla sua socket, vengono eseguite le operazioni di generazione dei blocchi della JokerChain.
     * @param socket - Istanza di socket.
     */
    public void handleClientConnection(SSLSocket socket) {
        try{
            String client = (String) SSLConnection.receiveData(socket);
            System.out.println("\u001B[33m(SSLADM) Connesso con " + client + "!\u001B[0m");

            SSLSession session = socket.getSession();
            if(this.jokerChain.getTransactions().isEmpty()){
                X509Certificate admCert = (X509Certificate) this.ks.getCertificate("adm");
                PublicKey admPublicKey = admCert.getPublicKey();

                X509Certificate jokerCert = (X509Certificate) session.getPeerCertificates()[0];
                PublicKey jokerPublicKey = jokerCert.getPublicKey();   

                System.out.println("\u001B[33m(SSLADM) Creazione Blocco Genesi ...\u001B[0m");
                this.jokerChain.createGenesisBlock(admPublicKey, jokerPublicKey);   
            }

            // Blocco Chiavi
            Thread.sleep(500);
            PublicKey publicKey = (PublicKey) SSLConnection.receiveData(socket);
            System.out.println("\u001B[33m(SSLADM) Ricevuta chiave pubblica da: " + client + "\u001B[0m");
            this.jokerChain.addTransaction(new Transaction(2, Base64.getEncoder().encodeToString(publicKey.getEncoded()), client));
            this.countDownLatch.countDown();
            if(this.countDownLatch.getCount() == 0)
                this.resetLatch(4);

            // Blocco Giocate
            Thread.sleep(500);
            String g = (String) SSLConnection.receiveData(socket);
            if(g != null){
                byte[] signedG = (byte[]) SSLConnection.receiveData(socket);
                System.out.println("\u001B[33m(SSLADM) Ricevuta giocata da: " + client + "\u001B[0m");
                if (!verifySignature(g.getBytes(), signedG, publicKey))
                    System.out.println("\u001B[33m(SSLADM) Firma non valida. Giocata potenzialmente compromessa!\u001B[0m");   
                else{
                    System.out.println("\u001B[33m(SSLADM) Firma valida. Inserimento della giocata nella JokerChain in corso ...\u001B[0m");              
                    this.jokerChain.addTransaction(new Transaction(3, g, client));
                }
            }
            this.countDownLatch.countDown();
            if(this.countDownLatch.getCount() == 0)
                this.resetLatch(4);

            // Blocco Stringhe Casuali
            Thread.sleep(500);
            inputSemaphore.acquire();
            byte[] prng = (byte[]) SSLConnection.receiveData(socket);
            if(prng != null){
                byte[] signedPrng = (byte[]) SSLConnection.receiveData(socket);
                System.out.println("\u001B[33m(SSLADM) Ricevuta stringa casuale da: " + client + "\u001B[0m");
                if (!verifySignature(prng, signedPrng, publicKey))
                    System.out.println("\u001B[33m(SSLADM) Firma non valida. Stringa potenzialmente compromessa!\u001B[0m");   
                else{
                    System.out.println("\u001B[33m(SSLADM) Firma valida. Inserimento della stringa nella JokerChain in corso ...\u001B[0m");              
                    this.jokerChain.addTransaction(new Transaction(4, Base64.getEncoder().encodeToString(prng), client));
                }
            }
            inputSemaphore.release();
            this.countDownLatch.countDown();
            if(this.countDownLatch.getCount() == 0)
                this.resetLatch(4);

            // Blocco Risultato
            Thread.sleep(500);
            inputSemaphore.acquire();
            String risultato = (String) SSLConnection.receiveData(socket);
            if(risultato != null){
                byte[] signedRisultato = (byte[]) SSLConnection.receiveData(socket);
                System.out.println("\u001B[33m(SSLADM) Ricevuto risultato da: " + client + "\u001B[0m");
                if (!verifySignature(risultato.getBytes(), signedRisultato, publicKey))
                    System.out.println("\u001B[33m(SSLADM) Firma non valida. Risultato potenzialmente compromessa!\u001B[0m");   
                else{
                    System.out.println("\u001B[33m(SSLADM) Firma valida. Inserimento del risultato nella JokerChain in corso ...\u001B[0m");              
                    this.jokerChain.addTransaction(new Transaction(5, risultato, client));
                    System.out.println("---\n\u001B[33m(SSLADM) Il numero estratto è: \u001B[1m" + risultato + "\u001B[0m\n---");
                }
            }
            inputSemaphore.release();
            if(risultato == null && g != null && !g.equals("00000000000")){
                Roulette roulette = new Roulette();
                String numero = jokerChain.filterTransaction(5, jokerChain.getCurrentID() - 1).get(0).getData();
                System.out.println("\u001B[33m(SSLADM) La giocata di " + client + " è \u001B[1m" + roulette.esito(numero, g) + "\u001B[0m");            
            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Inizializza un nuovo oggetto 'CountDownLatch' così da inizializzare nuovamente il latch.
     * @param count - Il numero di volte che 'countDown' deve essere invocato prima che i thread posso andare oltre lo stato di attesa.
     */
    public void resetLatch(int count) {
        this.countDownLatch = new CountDownLatch(count);
    }

    /**
     * Costringe il thread corrente ad aspettare che 'latch' raggiunga il valore zero. 
     * @throws InterruptedException - Lancia un'eccezione nel caso in cui il thread corrente venga interrotto.
     */
    public void awaitCompletion() throws InterruptedException {
        this.countDownLatch.await();
    }
}
