package JokerCasino.Autentication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import JokerCasino.Player;

public class SSLJoker extends SSLBase implements SSLClientServer {

    private static final String TRUSTSTOREPATH = "Certs/truststore.jks";
    private static final int PORT = 4001;
    private static final int PLAYER_PORT = 3999; 
    private static final String PLAYER_HOST = "localhost";
    private static final String PASSWORD = "aps2023";
    private String message = "";
    private String response;
    private SSLContext sslContext;
    private Player p;

    public SSLJoker(String keystorePath, Player p) throws Exception {
        super(keystorePath);
        this.sslContext = getSslContext();
        this.p = p;
    }
    
    @Override
    public void startConnection() {
        
        try {
            SSLServerSocketFactory ssf = this.sslContext.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(PORT);
            serverSocket.setNeedClientAuth(true);

            System.out.println("\u001B[35m(SSLJoker) In attesa di connessioni ...\u001B[0m");
            SSLSocket socket = (SSLSocket) serverSocket.accept();
            System.out.println("\u001B[35m(SSLJoker) Connesso con l'Identity Provier del Ministero della Salute ...\u001B[0m");
                
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            message = reader.readLine();

            socket.close();

            Thread.sleep(500);
            System.out.println("\u001B[35m(SSLJoker) Socket chiusa.\n---\u001B[0m");

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        this.sendToPlayer();

    }

    @Override
    public String getResponse() {
        return this.response;
    }

    @Override
    public void setResponse(String response) {
        this.response = response;
    }

    public void sendToPlayer() {

        try {
            SSLSocketFactory ssf = this.sslContext.getSocketFactory();
            SSLSocket socket = (SSLSocket) ssf.createSocket(PLAYER_HOST, PLAYER_PORT);
            socket.startHandshake();
            
            // Estrapolazione dati
            String codiceFiscale = this.message.split(";")[0];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate dataDiNascita = LocalDate.parse(this.message.split(";")[1], formatter); 

            if(isMaggiorenne(dataDiNascita)){
                this.setResponse("Accesso Consentito. Benvenuto " + codiceFiscale);
                this.p.setCodiceFiscale(codiceFiscale);
                this.p.setDataDiNascita(dataDiNascita);
            }
            else
                this.setResponse("Accesso Negato. Il gioco è vietato per i minori di 18 anni!\n");

            OutputStream out = socket.getOutputStream();         
            out.write(this.getResponse().getBytes());
            
            socket.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private static boolean isMaggiorenne(LocalDate dataDiNascita) {

        LocalDate oggi = LocalDate.now();
    
        Period periodo = Period.between(dataDiNascita, oggi);
        
        return periodo.getYears() >= 18;
    }



}
