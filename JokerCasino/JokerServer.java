package JokerCasino;

import javax.net.ssl.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class JokerServer {

    private static final int PORT = 4001; 
    private static final String HOST = "localhost";
    private static final String PASSWORD = "aps2023";
    private static final String KEYSTOREPATH = "Certs/GP2.0/joker_keystore.jks";
    private static final String TRUSTSTOREPAHT = "Certs/GP2.0/truststore.jks";

    public JokerServer() throws Exception {
        
        System.setProperty("javax.net.ssl.keyStore", KEYSTOREPATH);
        System.setProperty("javax.net.ssl.keyStorePassword", PASSWORD);

        System.setProperty("javax.net.ssl.trustStore", TRUSTSTOREPAHT);
        System.setProperty("javax.net.ssl.trustStorePassword", PASSWORD);

    }

    public void startServer() {
        try {
            SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(PORT);
            serverSocket.setNeedClientAuth(true);

            System.out.println("\u001B[35m(JokerServer) In attesa di connessioni ...\u001B[0m");
            SSLSocket socket = (SSLSocket) serverSocket.accept();
            System.out.println("\u001B[35m(JokerServer) Connesso con l'Identity Provier del Ministero della Salute ...\u001B[0m");
                
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String infoPlayer = reader.readLine();

            String response = "";
            if(isMaggiorenne(infoPlayer.split(";")[1]))
                response = "Accesso Consentito. Benvenuto " + infoPlayer.split(";")[0];
            else
                response = "Accesso Negato. Il gioco è vietato per i minori di 18 anni!\n";

            OutputStream out = socket.getOutputStream();
            out.write(response.getBytes());

            socket.close();

            System.out.println("\u001B[35m(JokerServer) Socket chiusa.\u001B[0m");
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static boolean isMaggiorenne(String dataDiNascitaStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate dataDiNascita = LocalDate.parse(dataDiNascitaStr, formatter);
        
        LocalDate oggi = LocalDate.now();
        
        Period periodo = Period.between(dataDiNascita, oggi);
        
        return periodo.getYears() >= 18;
    }

}