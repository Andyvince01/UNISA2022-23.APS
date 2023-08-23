package JokerCasino.Autentication;

import javax.net.ssl.*;

public class SSLCitizen extends SSLBase{

    private static final int PORT = 3999; 
    private static final String MS_HOST = "localhost";
    private static final int MS_PORT = 4000; 
    private SSLContext sslContext;
    private boolean authorized;

    /**
     * Metodo Costruttore di SSLCitizen.
     * @param keystorePath - Path al keystore del cittadino che vuole autenticarsi in modo da poter accedere ai servizi.
     * @throws Exception Lancia un'eccezione in caso di errori durante la fase di autenticazione (e.g., CertificateExpiredException).
     */
    public SSLCitizen(String keystorePath) throws Exception {
        super(keystorePath);
        this.sslContext = getSslContext();
    }

    /**
     * Tale metodo permette al Fornitore di Servizi (Mr. Joker) di potersi collegare con il Player che vuole autenticarsi,
     * in modo che questo possa comunicargli se è autorizzato o meno ad accedere ai servizi da lui offerti,
     */
    public void startConnection() {
        try {
            SSLServerSocketFactory ssf = this.sslContext.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(PORT);
            serverSocket.setNeedClientAuth(true);

            System.out.println("\u001B[32m(SSLPlayer) In attesa di connessioni ...\u001B[0m");
            SSLSocket socket = (SSLSocket) serverSocket.accept();
            System.out.println("\u001B[32m(SSLPlayer) Connesso con Mr.Joker!\u001B[0m");

            String response = (String) receiveData(socket);
            System.out.println("\u001B[32m(SSLPlayer) Risposta da Mr.Joker: \u001B[1m" + response + "\u001B[0m");

            if(response.startsWith("Accesso Consentito"))
                setAuthorized(true);
            else
                setAuthorized(false);

            socket.close();
            Thread.sleep(100);
            System.out.println("\u001B[32m(SSLPlayer) Socket chiusa.\n\u001B[0m");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Metodo che avvia la connessione con l'Identity Provider del Ministero della Salute.
     */
    public void connectToMS() {
        try {
            SSLSocketFactory ssf = this.sslContext.getSocketFactory();
            SSLSocket socket = (SSLSocket) ssf.createSocket(MS_HOST, MS_PORT);
            socket.startHandshake();

            socket.close();
            System.out.println("\u001B[32m(SSLPlayer) Socket chiusa.\u001B[0m");

        } catch (Exception ex) {
            ex.printStackTrace();            
        }
    }

    public boolean isAuthorized() {
        return authorized;
    }

    private void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }       

}