package JokerCasino.Autentication;

import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SSLMS extends SSLBase{

    private static final int PORT = 4000;
    private static final int JOKER_PORT = 4001; 
    private static final String JOKER_HOST = "localhost";
    private SSLContext sslContext;

    /**
     * Costruttore di SSLMS.
     * @param keystorePath - Path al keystore dell'Identity Provider del Ministero della Salute.
     * @throws Exception - Lancia un'eccezione in caso di errori durante la fase di autenticazione.
     */
    public SSLMS(String keystorePath) throws Exception {
        super(keystorePath);
        this.sslContext = getSslContext();
    }

    /**
     * Tale metodo permtte al Cittadino di potersi collegare all'IdP del Ministero della Salute.
     */
    public void startConnection() {
        String message = "";
        try {
            SSLServerSocketFactory ssf = this.sslContext.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(PORT);
            serverSocket.setNeedClientAuth(true);

            System.out.println("\u001B[34m(SSLMS) In attesa di connessioni ...\u001B[0m");
            SSLSocket socket = (SSLSocket) serverSocket.accept();
            System.out.println("\u001B[34m(SSLMS) Connesso con il Player!\u001B[0m");
            
            // Estrazione delle informazioni essenziali dal GP2.0 del giocatore.
            SSLSession session = socket.getSession();
            X509Certificate cert = (X509Certificate) session.getPeerCertificates()[0];
            System.out.println("\u001B[34m(SSLMS) I seguenti dati stanno per essere inviati a Mr.Joker: Data di Nascita e Codice Fiscale!\u001B[0m");
            String subjectInfo = cert.getSubjectX500Principal().toString();
            List<String> oidList = Arrays.asList("OID.1.2.3.1.4", "OID.1.2.3.1.7");
            String[] fields = subjectInfo.split(",");
            for(String field : fields){
                String[] fieldSplit = field.split("=");
                if(oidList.contains(fieldSplit[0].trim()))
                    message += fieldSplit[1] + ";";
            }
            // Chiusura socket
            socket.close();
            Thread.sleep(100);
            System.out.println("\u001B[34m(SSLMS) Socket chiusa.\u001B[0m");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.connectToJoker(message);
    }

    /**
     * Tale metodo permette all'IdP del Ministero della Salute di avviare una connessione con il Fornitore di Servizi (Mr. Joker).
     * @param message
     */
    private void connectToJoker(String message) {
        try {
            SSLSocketFactory ssf = this.sslContext.getSocketFactory();
            SSLSocket socket = (SSLSocket) ssf.createSocket(JOKER_HOST, JOKER_PORT);
            socket.startHandshake();
            // Invio dati essenziali per l'autenticazione.
            sendData(socket, message);        
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}