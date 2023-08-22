package JokerChain;

public class Giocata {
    private String numero;
    private String colore;
    private String parita;
    private String bit;

    public Giocata(String numero, String colore, String parita) throws Exception {
        this.numero = numero;
        this.colore = colore;
        this.parita = parita;
        this.bit = this.toBit();
    }

    public String getNumero() {
        return numero;
    }

    public String getColore() {
        return colore;
    }

    public String getParita() {
        return parita;
    }

    public String getBit() {
        return bit;
    }

    private String toBit() throws Exception{
        String sogliaNumero = (this.numero.isEmpty() == false)? "1" : "0";
        String numero = (sogliaNumero == "1") ? Integer.toBinaryString(Integer.valueOf(this.numero)) : "000000";
        String sogliaColore = (this.colore.isEmpty() == false)? "1" : "0";
        String colore = (sogliaColore == "1") ? (this.colore.equals("rosso")? "1" : (this.colore.equals("nero")? "0" : null)) : "0";
        String sogliaParita = (this.parita.isEmpty() == false)? "1" : "0";
        String parita = (sogliaParita == "1") ? ((this.parita.equals("pari"))? "1" : (this.parita.equals("dipari")? "0" : null)) : "0";

        while(numero.length() != 6)
            numero = "0" + numero;

        return sogliaNumero + numero + sogliaColore + colore + sogliaParita + parita;
    }

    public void fromString(Giocata g){
        this.numero = g.bit.substring(1, 7);
        this.colore = String.valueOf(g.bit.charAt(8));
        this.parita = String.valueOf(g.bit.charAt(10));
    }
    
}
