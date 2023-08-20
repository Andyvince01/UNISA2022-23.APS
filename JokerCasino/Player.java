package JokerCasino;

import java.time.LocalDate;

public class Player {

    private String codiceFiscale;
    private LocalDate dataDiNascita;
    private String nickname;

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

    @Override
    public String toString() {
        return "Player [codiceFiscale=" + codiceFiscale + ", dataDiNascita=" + dataDiNascita + ", nickname=" + nickname
                + "]";
    } 

    
}
