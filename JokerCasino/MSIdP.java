package JokerCasino;

import javax.net.ssl.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;

public class MSIdP {

    private static final int PORT = 4000;
    private static final int JOKER_PORT = 4001; 
    private static final String JOKER_HOST = "localhost";
    private static final String PASSWORD = "aps2023";
    private static final String TRUSTSTOREPATH = "Certs/GP2.0/truststore.jks";
    
    private SSLContext sslContext;
    
    public MSIdP(String keystorePath) throws Exception {
        
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

    public void startServer() {
        try {
            SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(PORT);
            serverSocket.setNeedClientAuth(true);

            System.out.println("\u001B[34m(IdP-MS) In attesa di connessioni ...\u001B[0m");
            SSLSocket socket = (SSLSocket) serverSocket.accept();
            System.out.println("\u001B[34m(IdP-MS) Connesso con il Player ...\u001B[0m");
            
            SSLSession session = socket.getSession();
            X509Certificate cert = (X509Certificate) session.getPeerCertificates()[0];
            
            OutputStream out = socket.getOutputStream();
            out.write("I seguenti dati stanno per essere inviati a Mr.Joker: Data di Nascita e Codice Fiscale".getBytes());

            System.out.println("\u001B[34m(IdP-MS) Socket chiusa.\u001B[0m");

            // Creare l'istanza di JokerServer
            String response = sendToJoker(cert);
            
            OutputStream out1 = socket.getOutputStream();
            out1.write(response.getBytes());

            socket.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String sendToJoker(X509Certificate clientCert) {
        String response = "";
        
        try {
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            SSLSocket socket = (SSLSocket) ssf.createSocket(JOKER_HOST, JOKER_PORT);
            socket.startHandshake();
            
            OutputStream out = socket.getOutputStream();
            String subjectInfo = clientCert.getSubjectX500Principal().toString();
            String infoPlayer = "";

            List<String> oidList = Arrays.asList("OID.1.2.3.1.4", "OID.1.2.3.1.7");

            String[] fields = subjectInfo.split(",");
            for(String field : fields){
                String[] fieldSplit = field.split("=");
                if(oidList.contains(fieldSplit[0].trim()))
                    infoPlayer += fieldSplit[1] + ";";
            }
            infoPlayer += '\n';
            
            out.write(infoPlayer.getBytes());

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            response = reader.readLine();
            
            socket.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    
    return response;

    }

}
