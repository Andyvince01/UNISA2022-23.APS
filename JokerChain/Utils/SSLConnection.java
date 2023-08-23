package JokerChain.Utils;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import JokerCasino.Autentication.SSLBase;
import JokerChain.SSLADM;

public abstract class SSLConnection extends SSLBase{

    private static final int ADM_PORT = 4002; 
    private static final String ADM_HOST = "localhost";

    /**
     * Costruttore di SSLConnection.
     * @throws Exception - Lancia un'eccezione in caso di errori durante l'inizializzazione del contesto SSL. 
     */
    public SSLConnection() throws Exception{
        super();
    }

    /**
     * Costruttore di SSLConnection con parametro 'keystorePath'.
     * @param keystorePath - Path del keystore contenente la chiave privata e il certificato del player che si deve identificare (i.e., banco).
     * @throws Exception - Lancia un'eccezione in caso di errori durante l'inizializzazione del contesto SSL.
     */
    public SSLConnection(String keystorePath) throws Exception {
        super(keystorePath);
    }

    /**
     * Stabilisce una connessione con SSLADM e gestisce vari blocchi di dati.
     * @param ssladm - L'istanza di SSLADM con cui interagire.
     * @param nickname - Il nome utente da utilizzare per la connessione.
     */
    public void connectTo(SSLADM ssladm, String nickname) {
        try {
            SSLSocketFactory ssf = getSslContext().getSocketFactory();
            SSLSocket socket = (SSLSocket) ssf.createSocket(ADM_HOST, ADM_PORT);
            socket.startHandshake();

            // Connessione
            sendData(socket, nickname);

            // Blocco Chiavi
            handleChiavi(socket, ssladm);

            // Blocco Giocate
            handleGiocate(socket, ssladm);

            // Blocco Stringhe Casuali
            handleStringheCasuali(socket, ssladm);

            // Blocco Risultato
            handleRisultato(socket, ssladm);

            socket.close();

        } catch (Exception ex) {
            ex.printStackTrace();            
        }
    }

    /**
     * Gestisce il blocco relativo alle chiavi durante la connessione.
     * @param socket - Il socket SSL utilizzato per la connessione.
     * @param ssladm - L'istanza di SSLADM con cui interagire.
     * @throws Exception Se si verifica un errore durante la gestione del blocco chiavi.
     */
    protected abstract void handleChiavi(SSLSocket socket, SSLADM ssladm) throws Exception;
    
    /**
     * Gestisce il blocco relativo alle giocate durante la connessione.
     * @param socket - Il socket SSL utilizzato per la connessione.
     * @param ssladm - L'istanza di SSLADM con cui interagire.
     * @throws Exception Se si verifica un errore durante la gestione del blocco giocate.
     */
    protected abstract void handleGiocate(SSLSocket socket, SSLADM ssladm) throws Exception;
    
    /**
     * Gestisce il blocco relativo alle stringhe casuali durante la connessione.
     * @param socket - Il socket SSL utilizzato per la connessione.
     * @param ssladm - L'istanza di SSLADM con cui interagire.
     * @throws Exception Se si verifica un errore durante la gestione del blocco stringhe casuali.
     */
    protected abstract void handleStringheCasuali(SSLSocket socket, SSLADM ssladm) throws Exception;
    
    /**
     * Gestisce il blocco relativo ai risultati durante la connessione.
     * @param socket - Il socket SSL utilizzato per la connessione.
     * @param ssladm - L'istanza di SSLADM con cui interagire.
     * @throws Exception Se si verifica un errore durante la gestione del blocco risultati.
     */
    protected abstract void handleRisultato(SSLSocket socket, SSLADM ssladm) throws Exception;

}
