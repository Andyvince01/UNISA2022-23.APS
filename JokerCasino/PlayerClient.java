package JokerCasino;

import javax.net.ssl.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PlayerClient {

    private SSLContext sslContext;
    private static final String HOST = "localhost";
    private static final int PORT = 4000;
    private static final String PASSWORD = "aps2023";
    private static final String TRUSTSTOREPATH = "Certs/GP2.0/truststore.jks";

    public PlayerClient(String keystorePath) throws Exception {
        // Carica il keystore del Player
        KeyStore ks = KeyStore.getInstance("JKS");
        InputStream kis = new FileInputStream(keystorePath);
        ks.load(kis, PASSWORD.toCharArray());

        // Inizializza il KeyManagerFactory
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, PASSWORD.toCharArray());

        // Carica il truststore del Player
        System.setProperty("javax.net.ssl.trustStore", TRUSTSTOREPATH);
        System.setProperty("javax.net.ssl.trustStorePassword", PASSWORD);

        // Crea e inizializza il SSLContext
        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, null);
    }

    public String connectAndFetchResponse() {
        try {
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            SSLSocket socket = (SSLSocket) ssf.createSocket(HOST, PORT);
            socket.startHandshake();

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = reader.readLine();

            System.out.println("\u001B[32m(Player) " + response + "\u001B[0m\n");
            socket.close();

            System.out.println("\u001B[32m(Player) Socket chiusa.\u001B[0m");
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
