package JokerChain;

import JokerCasino.Player;
import JokerCasino.Autentication.SSLBase;
import JokerCasino.Autentication.SSLClientServer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class SSLBanco implements SSLClientServer {

    private static final int PORT = 4001;
    private static final String TRUSTSTOREPATH = "Certs/truststore.jks";
    private static final String PASSWORD = "aps2023";
    private static final int ADM_PORT = 4002; 
    private static final String ADM_HOST = "localhost";
    private String response;
    private SSLContext sslContext;
    private SSLSocket socket;
    private KeyStore ks;

    public SSLBanco(String keystorePath) throws Exception {
        // Carica il keystore
        this.ks = KeyStore.getInstance("JKS");
        try (InputStream kis = new FileInputStream(keystorePath)) {
            this.ks.load(kis, PASSWORD.toCharArray());
        }

        // Inizializza il KeyManagerFactory
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(this.ks, PASSWORD.toCharArray());

        // Carica il truststore
        System.setProperty("javax.net.ssl.trustStore", TRUSTSTOREPATH);
        System.setProperty("javax.net.ssl.trustStorePassword", PASSWORD);

        // Crea e inizializza il SSLContext
        this.sslContext = SSLContext.getInstance("TLS");
        this.sslContext.init(kmf.getKeyManagers(), null, null);
    }

    @Override
    public void startConnection() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'startConnection'");
    }

    @Override
    public String getResponse() {
        return this.response;
    }

    @Override
    public void setResponse(String response) {
        this.response = response;
    }

    public void connectToADM() {
        try {
            SSLSocketFactory ssf = this.sslContext.getSocketFactory();
            SSLSocket socket = (SSLSocket) ssf.createSocket(ADM_HOST, ADM_PORT);
            socket.startHandshake();

            // BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // String response = reader.readLine();

            // System.out.println("\u001B[35m(SSLBanco) Risposta da SSLBanco: " + response + "\u001B[0m");
            socket.close();

            System.out.println("\u001B[35m(SSLBanco) Socket chiusa.\u001B[0m");

        } catch (Exception ex) {
            ex.printStackTrace();            
        }
    }

}
