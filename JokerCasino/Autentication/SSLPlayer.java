package JokerCasino.Autentication;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.net.ssl.*;

public class SSLPlayer extends SSLBase implements SSLClientServer{

    private static final int PORT = 4002; 
    private static final String MS_HOST = "localhost";
    private static final int MS_PORT = 4000; 
    private String response;
    private SSLContext sslContext;

    public SSLPlayer(String keystorePath) throws Exception {
        super(keystorePath);
        this.sslContext = getSslContext();
    }

    @Override
    public void startConnection() {
        try {
            SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(PORT);

            System.out.println("\u001B[32m(SSLPlayer) In attesa di connessioni ...\u001B[0m");
            SSLSocket socket = (SSLSocket) serverSocket.accept();
            System.out.println("\u001B[32m(SSLPlayer) Connesso con Mr.Joker!\u001B[0m");

            // SSLSession session = socket.getSession();
            // X509Certificate cert = (X509Certificate) session.getPeerCertificates()[0];
            // System.out.println(cert.getSubjectX500Principal().toString());

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = reader.readLine();

            System.out.println("\u001B[32m(SSLPlayer) Risposta da Mr.Joker: \u001B[1m" + response + "\u001B[0m");
            socket.close();

            Thread.sleep(100);
            System.out.println("\u001B[32m(SSLPlayer) Socket chiusa.\n\u001B[0m");

            setResponse(response);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String getResponse() {
        return this.response;
    }

    @Override
    public void setResponse(String response) {
        this.response = response;
    }

    public void connectToMS() {
        try {
            SSLSocketFactory ssf = this.sslContext.getSocketFactory();
            SSLSocket socket = (SSLSocket) ssf.createSocket(MS_HOST, MS_PORT);
            socket.startHandshake();

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = reader.readLine();

            System.out.println("\u001B[32m(SSLPlayer) Risposta da IdP-MS: " + response + "\u001B[0m");
            socket.close();

            System.out.println("\u001B[32m(SSLPlayer) Socket chiusa.\u001B[0m");

        } catch (Exception ex) {
            ex.printStackTrace();            
        }
    }


}