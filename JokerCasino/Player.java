package JokerCasino;

import java.util.Date;

public class Player {

    private final String codiceFiscale;
    private final Date dataDiNascita;
    private String nickname;

    public Player(String codiceFiscale, Date dataDiNascita, String nickname) {
        this.codiceFiscale = codiceFiscale;
        this.dataDiNascita = dataDiNascita;
        this.nickname = nickname;
    }

    public Player(String codiceFiscale, Date dataDiNascita) {
        this.codiceFiscale = codiceFiscale;
        this.dataDiNascita = dataDiNascita;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public Date getDataDiNascita() {
        return dataDiNascita;
    }

    public String getNickname() {
        return nickname;
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
