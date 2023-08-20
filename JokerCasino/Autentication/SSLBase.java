package JokerCasino.Autentication;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public abstract class SSLBase {

    protected static final String TRUSTSTOREPATH = "Certs/truststore.jks";
    protected static final String PASSWORD = "aps2023";
    protected SSLContext sslContext;

    public SSLBase(String keystorePath) throws Exception {
        initializeSSLContext(keystorePath);
    }

    protected void initializeSSLContext(String keystorePath) throws Exception {
        // Carica il keystore
        KeyStore ks = KeyStore.getInstance("JKS");
        try (InputStream kis = new FileInputStream(keystorePath)) {
            ks.load(kis, PASSWORD.toCharArray());
        }

        // Inizializza il KeyManagerFactory
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, PASSWORD.toCharArray());

        // Carica il truststore
        KeyStore ts = KeyStore.getInstance("JKS");
        try (InputStream tis = new FileInputStream(TRUSTSTOREPATH)) {
            ts.load(tis, PASSWORD.toCharArray());
        }

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ts);

        // Crea e inizializza il SSLContext
        this.sslContext = SSLContext.getInstance("TLS");
        this.sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
    }

    public SSLContext getSslContext() {
        return sslContext;
    }
}
