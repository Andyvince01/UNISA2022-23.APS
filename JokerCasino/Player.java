package JokerCasino;

import java.security.KeyPair;
import java.time.LocalDate;

public class Player {

    private String codiceFiscale;
    private LocalDate dataDiNascita;
    private String nickname;
    private KeyPair keyPair;

    public Player(String nickname) {
        this.nickname = nickname;
    }

    public Player() {
    }

    public Player(String codiceFiscale, LocalDate dataDiNascita) {
        this.codiceFiscale = codiceFiscale;
        this.dataDiNascita = dataDiNascita;
    }

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

    @Override
    public String toString() {
        return "Player [codiceFiscale=" + codiceFiscale + ", dataDiNascita=" + dataDiNascita + ", nickname=" + nickname
                + "]";
    }

}
