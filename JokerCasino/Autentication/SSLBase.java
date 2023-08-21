package JokerCasino.Autentication;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public abstract class SSLBase {

    private static final String TRUSTSTOREPATH = "Certs/truststore.jks";
    private static final String PASSWORD = "aps2023";
    private SSLContext sslContext;
    private KeyStore ks;

    public SSLBase(String keystorePath) throws Exception {
        this.initializeSSLContext(keystorePath);
    }

    private void initializeSSLContext(String keystorePath) throws Exception {
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
    }

    public SSLContext getSslContext() {
        return sslContext;
    }

    public KeyStore getKs() throws KeyStoreException {
        return this.ks;
    }

}
