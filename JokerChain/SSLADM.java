package JokerChain;

import java.security.KeyStore;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.concurrent.CountDownLatch;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import JokerCasino.Autentication.SSLBase;
import JokerCasino.Autentication.SSLClientServer;
import JokerChain.Utils.SocketUtils;

public class SSLADM extends SSLBase implements SSLClientServer{

    private static final int PORT = 4002;
    private SSLContext sslContext;
    private JokerChain jokerChain;
    private KeyStore ks;
    private CountDownLatch countDownLatch;

    public SSLADM(String keystorePath, JokerChain jokerChain) throws Exception {
        super(keystorePath);
        this.sslContext = getSslContext();
        this.jokerChain = jokerChain;
        this.ks = getKs();
        this.countDownLatch = new CountDownLatch(2);
    }

    @Override
    public String getResponse() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setResponse(String response) {
        // TODO Auto-generated method stub
    }

    @Override
    public void startConnection() {
        try {
            SSLServerSocketFactory ssf = this.sslContext.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(PORT);
            serverSocket.setWantClientAuth(true);

            System.out.println("\u001B[33m(SSLADM) In attesa di connessioni ...\u001B[0m");
            int count = 0;
            while(count < 2){
                final SSLSocket socket = (SSLSocket) serverSocket.accept();
                new Thread(() -> handleClientConnection(socket)).start();;
                count++;
            }
            serverSocket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleClientConnection(SSLSocket socket) {
        try{
            String client = (String) SocketUtils.receiveData(socket);
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
            Thread.sleep(100);
            PublicKey publicKey = (PublicKey) SocketUtils.receiveData(socket);
            System.out.println("\u001B[33m(SSLADM) Ricevuta chiave pubblica da: " + client + "\u001B[0m");
            this.jokerChain.addTransaction(new Transaction(2, Base64.getEncoder().encodeToString(publicKey.getEncoded()), client));
            this.countDownLatch.countDown();
            if(this.countDownLatch.getCount() == 0)
                this.resetLatch(2);

            // Blocco Giocate
            Thread.sleep(100);
            String g = (String) SocketUtils.receiveData(socket);
            if(g != null){
                byte[] signedG = (byte[]) SocketUtils.receiveData(socket);
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
                this.resetLatch(2);

            // Blocco Stringhe Casuali
            Thread.sleep(100);
            byte[] prng = (byte[]) SocketUtils.receiveData(socket);
            if(prng != null){
                byte[] signedPrng = (byte[]) SocketUtils.receiveData(socket);
                System.out.println("\u001B[33m(SSLADM) Ricevuta stringa casuale da: " + client + "\u001B[0m");
                if (!verifySignature(prng, signedPrng, publicKey))
                    System.out.println("\u001B[33m(SSLADM) Firma non valida. Stringa potenzialmente compromessa!\u001B[0m");   
                else{
                    System.out.println("\u001B[33m(SSLADM) Firma valida. Inserimento della stringa nella JokerChain in corso ...\u001B[0m");              
                    this.jokerChain.addTransaction(new Transaction(4, Base64.getEncoder().encodeToString(prng), client));
                }
            }

            this.countDownLatch.countDown();
            if(this.countDownLatch.getCount() == 0)
                this.resetLatch(2);

            // Blocco Risultato
            Thread.sleep(100);
            String risultato = (String) SocketUtils.receiveData(socket);
            if(risultato != null){
                byte[] signedRisultato = (byte[]) SocketUtils.receiveData(socket);
                System.out.println("\u001B[33m(SSLADM) Ricevuto risultato da: " + client + "\u001B[0m");
                if (!verifySignature(risultato.getBytes(), signedRisultato, publicKey))
                    System.out.println("\u001B[33m(SSLADM) Firma non valida. Risultato potenzialmente compromessa!\u001B[0m");   
                else{
                    System.out.println("\u001B[33m(SSLADM) Firma valida. Inserimento del risultato nella JokerChain in corso ...\u001B[0m");              
                    this.jokerChain.addTransaction(new Transaction(5, risultato, client));
                }
            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private boolean verifySignature(byte[] data, byte[] signatureBytes, PublicKey publicKey) {
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

    public void resetLatch(int count) {
        this.countDownLatch = new CountDownLatch(count);
    }

    public void awaitCompletion() throws InterruptedException {
        this.countDownLatch.await();
    }
}
