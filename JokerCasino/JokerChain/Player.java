package JokerCasino.JokerChain;

import java.security.KeyPair;
import java.time.LocalDate;

public class Player {

    private String codiceFiscale;
    private LocalDate dataDiNascita;
    private String nickname;
    private KeyPair keyPair;
    private Giocata giocata;
    
    /**
     * Costruttore di Player con parametro 'nickname' 
     * @param nickname - Indica il nickname di gioco con cui è conosciuto il giocatore da tutti gli altri giocatori.
     */
    public Player(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Costruttore di Player
     */
    public Player() {
    }

    /**
     * Costruttore di Player con parametri 'codiceFiscale' e 'dataDiNascita'.
     * @param codiceFiscale - Indica il codice fiscale del giocatore.
     * @param dataDiNascita - Indica la data di nascita del giocatore.
     */
    public Player(String codiceFiscale, LocalDate dataDiNascita) {
        this.codiceFiscale = codiceFiscale;
        this.dataDiNascita = dataDiNascita;
    }

    /**
     * Costruttore di Player con parametri 'codiceFiscale', 'dataDiNascita' e 'nickname'.
     * @param codiceFiscale - Indica il codice fiscale del giocatore.
     * @param dataDiNascita - Indica la data di nascita del giocatore.
     * @param nickname - Indica il nikcname di gioco con cui è conosciuto il giocatore da tutti gli altri giocatori. 
     */
    public Player(String codiceFiscale, LocalDate dataDiNascita, String nickname) {
        this.codiceFiscale = codiceFiscale;
        this.dataDiNascita = dataDiNascita;
        this.nickname = nickname;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public LocalDate getDataDiNascita() {
        return dataDiNascita;
    }

    public String getNickname() {
        return nickname;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public void setDataDiNascita(LocalDate dataDiNascita) {
        this.dataDiNascita = dataDiNascita;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }

    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }     

    public void setGiocata(Giocata giocata) {
        this.giocata = giocata;
    }

    public Giocata getGiocata() {
        return giocata;
    }

    @Override
    public String toString() {
        return "Player [codiceFiscale=" + codiceFiscale + ", dataDiNascita=" + dataDiNascita + ", nickname=" + nickname
                + "]";
    }

}
