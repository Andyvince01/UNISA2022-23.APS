package JokerCasino.Autentication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SSLMS extends SSLBase implements SSLClientServer{

    private static final int PORT = 4000;
    private static final int JOKER_PORT = 4001; 
    private static final String JOKER_HOST = "localhost";
    private String response;
    private String message;
    private SSLContext sslContext;

    public SSLMS(String keystorePath) throws Exception {
        super(keystorePath);
        this.sslContext = getSslContext();
        this.message = "";
    }

    @Override
    public void startConnection() {
        try {
            SSLServerSocketFactory ssf = this.sslContext.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(PORT);
            serverSocket.setNeedClientAuth(true);

            System.out.println("\u001B[34m(SSLMS) In attesa di connessioni ...\u001B[0m");
            SSLSocket socket = (SSLSocket) serverSocket.accept();
            System.out.println("\u001B[34m(SSLMS) Connesso con il Player!\u001B[0m");
            
            SSLSession session = socket.getSession();
            X509Certificate cert = (X509Certificate) session.getPeerCertificates()[0];
            
            OutputStream out = socket.getOutputStream();
            out.write("I seguenti dati stanno per essere inviati a Mr.Joker: Data di Nascita e Codice Fiscale".getBytes());

            String subjectInfo = cert.getSubjectX500Principal().toString();
            List<String> oidList = Arrays.asList("OID.1.2.3.1.4", "OID.1.2.3.1.7");

            String[] fields = subjectInfo.split(",");
            for(String field : fields){
                String[] fieldSplit = field.split("=");
                if(oidList.contains(fieldSplit[0].trim()))
                    this.message += fieldSplit[1] + ";";
            }
            this.message += '\n';

            socket.close();

            Thread.sleep(100);
            System.out.println("\u001B[34m(SSLMS) Socket chiusa.\u001B[0m");

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        this.connectToJoker();

    }

    @Override
    public String getResponse() {
        return this.response;
    }

    @Override
    public void setResponse(String response) {
        this.response = response;
    }

    public void connectToJoker() {
        
        try {
            SSLSocketFactory ssf = this.sslContext.getSocketFactory();
            SSLSocket socket = (SSLSocket) ssf.createSocket(JOKER_HOST, JOKER_PORT);
            socket.startHandshake();
            
            OutputStream out = socket.getOutputStream();
            out.write(this.message.getBytes());

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.setResponse(reader.readLine());
            
            socket.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    
    }

}