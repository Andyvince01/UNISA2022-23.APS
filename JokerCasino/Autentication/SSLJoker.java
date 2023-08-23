package JokerCasino.Autentication;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import JokerChain.Player;

public class SSLJoker extends SSLBase {

    private static final int PORT = 4001;
    private static final int PLAYER_PORT = 3999; 
    private static final String PLAYER_HOST = "localhost";
    private SSLContext sslContext;
    private Player p;

    public SSLJoker(String keystorePath, Player p) throws Exception {
        super(keystorePath);
        this.sslContext = getSslContext();
        this.p = p;
    }
    
    public void startConnection() {
        String message = "";
        try {
            SSLServerSocketFactory ssf = this.sslContext.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(PORT);
            serverSocket.setNeedClientAuth(true);

            System.out.println("\u001B[35m(SSLJoker) In attesa di connessioni ...\u001B[0m");
            SSLSocket socket = (SSLSocket) serverSocket.accept();
            System.out.println("\u001B[35m(SSLJoker) Connesso con l'Identity Provier del Ministero della Salute ...\u001B[0m");
                
            message = (String) receiveData(socket);

            socket.close();
            Thread.sleep(500);
            System.out.println("\u001B[35m(SSLJoker) Socket chiusa.\n---\u001B[0m");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        sendToPlayer(message);
    }

    private void sendToPlayer(String message) {
        String response = "";
        try {
            SSLSocketFactory ssf = this.sslContext.getSocketFactory();
            SSLSocket socket = (SSLSocket) ssf.createSocket(PLAYER_HOST, PLAYER_PORT);
            socket.startHandshake();
            
            // Estrapolazione dati
            String codiceFiscale = message.split(";")[0];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate dataDiNascita = LocalDate.parse(message.split(";")[1], formatter); 

            if(isMaggiorenne(dataDiNascita)){
                response = "Accesso Consentito. Benvenuto " + codiceFiscale;
                this.p.setCodiceFiscale(codiceFiscale);
                this.p.setDataDiNascita(dataDiNascita);
            }
            else{
                response = "Accesso Negato. Il gioco è vietato per i minori di 18 anni!";
                this.p = new Player();
            }
            sendData(socket, response);            
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
