package JokerChain;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

import JokerCasino.Autentication.SSLBase;
import JokerCasino.Autentication.SSLClientServer;

public class SSLADM implements SSLClientServer{

    private static final int PORT = 4002;
    private static final String TRUSTSTOREPATH = "Certs/truststore.jks";
    private static final String PASSWORD = "aps2023";
    private SSLContext sslContext;
    private SSLSocket socket;
    private JokerChain jokerChain;
    private KeyStore ks;

    public SSLADM(String keystorePath, JokerChain jokerChain) throws Exception {
        // Carica il keystore
        this.ks = KeyStore.getInstance("JKS");
        try (InputStream kis = new FileInputStream(keystorePath)) {
            this.ks.load(kis, PASSWORD.toCharArray());
        }

        // Inizializza il KeyManagerFactory
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(this.ks, PASSWORD.toCharArray());

        // Carica il truststore
        KeyStore ts = KeyStore.getInstance("JKS");
        try (InputStream tis = new FileInputStream(TRUSTSTOREPATH)) {
            ts.load(tis, PASSWORD.toCharArray());
        }

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        tmf.init(ts);

        // Crea e inizializza il SSLContext
        this.sslContext = SSLContext.getInstance("TLS");
        this.sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        this.jokerChain = jokerChain;
    }

    @Override
    public void startConnection() {
        try {
            SSLServerSocketFactory ssf = this.sslContext.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(PORT);
            serverSocket.setWantClientAuth(true);

            System.out.println("\u001B[33m(SSLADM) In attesa di connessioni ...\u001B[0m");
            this.socket = (SSLSocket) serverSocket.accept();
            System.out.println("\u001B[33m(SSLADM) Connesso con il Banco!\u001B[0m");
            
            SSLSession session = socket.getSession();
            if(jokerChain.getTransactions().isEmpty()){
                X509Certificate admCert = (X509Certificate) ks.getCertificate("adm");
                PublicKey admPublicKey = admCert.getPublicKey();

                X509Certificate jokerCert = (X509Certificate) session.getPeerCertificates()[0];
                PublicKey jokerPublicKey = jokerCert.getPublicKey();   

                jokerChain.createGenesisBlock(admPublicKey, jokerPublicKey);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String getResponse() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getResponse'");
    }

    @Override
    public void setResponse(String response) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setResponse'");
    }

    public void closeSocketADM() throws IOException{
        this.socket.close();
        System.out.println("\u001B[33m(SSLADM) Socket chiusa.\u001B[0m");
    }
    
}
