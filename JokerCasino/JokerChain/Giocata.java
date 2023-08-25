package JokerCasino.JokerChain;

import java.util.Hashtable;

public class Giocata {

    private String numero;
    private String colore;
    private String pariDispari;
    private String bit;

    /**
     * Metodo costruttore di Giocata.
     * @param numero
     * @param colore
     * @param pariDispari
     */
    public Giocata(String numero, String colore, String pariDispari) {
        this.numero = numero;
        this.colore = colore;
        this.pariDispari = pariDispari;
        this.bit = this.toBit();
    }

    public String getNumero() {
        return numero;
    }

    public String getColore() {
        return colore;
    }

    public String getpariDispari() {
        return pariDispari;
    }

    public String getBit() {
        return bit;
    }

    /**
     * Permette di poter rappresentare una giocata in termini di bit. In totale, si hanno 11 bit, di cui il primo, l'ottavo e il decimo
     * sono bit di soglia che servono a stabilire se il giocatore ha effettuato rispettivamente una giocata su 'numero', 'colore' e
     * 'pariDispari'.
     * @return Stringa che rappresenta la giocata in termini di bit.
     * @throws Exception
     */
    public String toBit(){
        String sogliaNumero = (this.numero.isEmpty() == false)? "1" : "0";
        String numero = (sogliaNumero == "1") ? Integer.toBinaryString(Integer.valueOf(this.numero)) : "000000";
        String sogliaColore = (this.colore.isEmpty() == false)? "1" : "0";
        String colore = (sogliaColore == "1") ? (this.colore.equals("rosso")? "1" : (this.colore.equals("nero")? "0" : null)) : "0";
        String sogliaPariDispari = (this.pariDispari.isEmpty() == false)? "1" : "0";
        String pariDispari = (sogliaPariDispari == "1") ? ((this.pariDispari.equals("pari"))? "1" : (this.pariDispari.equals("dispari")? "0" : null)) : "0";

        while(numero.length() != 6)
            numero = "0" + numero;

        return sogliaNumero + numero + sogliaColore + colore + sogliaPariDispari + pariDispari;
    }

    /**
     * Tale metodo permette di poter ricavare i parametri (numero | colore | pariDispari) a partire da una giocata espressa in bit.
     * @param g - Giocata espressa in bit.
     * @return Parametri (numero | colore | pariDispari) della giocata 'g'.
     */
    public static String fromString(String g){
        String numero, colore, pariDispari;
        if(String.valueOf(g.charAt(0)).equals("0"))
            numero = null;
        else
            numero = g.substring(1, 7);
        if(String.valueOf(g.charAt(7)).equals("0"))
            colore = null;
        else
            colore = String.valueOf(g.charAt(8));
        if(String.valueOf(g.charAt(9)).equals("0"))
            pariDispari = null;
        else
            pariDispari = String.valueOf(g.charAt(10));

        return numero + "," + colore + "," + pariDispari;
    }
    
}

class Roulette{
    
    static Hashtable<String, String> hashtable = new Hashtable<String, String>();

    /**
     * Metodo Costruttore di Roulette. Crea un Hash Table che associa ad ogni numero della roulette un colore.
     * Per semplicità, abbiamo considerato lo zero di colore nero, sebbene sia di colore verde.
     */
    public Roulette(){
        hashtable.put("0", "nero");
        hashtable.put("1", "rosso");
        hashtable.put("2", "nero");
        hashtable.put("3", "rosso");
        hashtable.put("4", "nero");
        hashtable.put("5", "rosso");
        hashtable.put("6", "nero");
        hashtable.put("7", "rosso");
        hashtable.put("8", "nero");
        hashtable.put("9", "rosso");
        hashtable.put("10", "nero");
        hashtable.put("11", "nero");
        hashtable.put("12", "rosso");
        hashtable.put("13", "nero");
        hashtable.put("14", "rosso");
        hashtable.put("15", "nero");
        hashtable.put("16", "rosso");
        hashtable.put("17", "nero");
        hashtable.put("18", "rosso");
        hashtable.put("19", "rosso");
        hashtable.put("20", "nero");
        hashtable.put("21", "rosso");
        hashtable.put("22", "nero");
        hashtable.put("23", "rosso");
        hashtable.put("24", "nero");
        hashtable.put("25", "rosso");
        hashtable.put("26", "nero");
        hashtable.put("27", "rosso");
        hashtable.put("28", "nero");
        hashtable.put("29", "nero");
        hashtable.put("30", "rosso");
        hashtable.put("31", "nero");
        hashtable.put("32", "rosso");
        hashtable.put("33", "nero");
        hashtable.put("34", "rosso");
        hashtable.put("35", "nero");
        hashtable.put("36", "rosso");
    }

    /**
     * Tale metodo permette di poter stabilire se la giocata 'g' di un giocatore è risultate Vincente o Perdente.
     * @param risultato - Risultato estratto dal nodo banco.
     * @param g - Giocata di un giocatore.
     * @return Stringa che indica l'esito della giocata: Vincente o Perdente.
     */
    public String esito(String risultato, String g){
        // Giocata
        String[] gString = Giocata.fromString(g).split(","); 
        // Risultato
        String colore = hashtable.get(risultato);
        int value = Integer.parseInt(risultato) % 2;
        String pariDispari = (value == 0)? "pari" : "dispari";
        String rGiocata = new Giocata(risultato, colore, pariDispari).toBit();
        String[] rString = Giocata.fromString(rGiocata).split(",");

        if(gString[0].equals(rString[0]) || gString[1].equals(rString[1]) || gString[2].equals(rString[2]))
            return "Vincente!";
        else
            return "Perdente!";

    }
    


}