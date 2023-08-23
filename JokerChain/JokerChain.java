package JokerChain;

import java.io.*;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JokerChain {

    private static File blockchainFile = new File("JokerChain/jokerchain.txt");
    private List<Transaction> transactions = new ArrayList<>();
    private int currentID;

    /**
     * Costruttore di JokerChain. Inizializza la blockchain, leggendo le transazioni dal file se esiste.
     * 
     * @param reset Se true, resetta la blockchain.
     * @throws Exception Se si verifica un errore durante la lettura del file.
     */
    public JokerChain(boolean reset) throws Exception {
        if (blockchainFile.exists() && blockchainFile.length() != 0 && !reset) {
            try (BufferedReader reader = new BufferedReader(new FileReader(blockchainFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    int id = Integer.parseInt(parts[0]);
                    int type = Integer.parseInt(parts[1]);
                    String data = parts[2];
                    String hash = parts[3];
                    String sender = parts[4];
                    transactions.add(new Transaction(id, type, data, hash, sender));
                }
                currentID = transactions.get(transactions.size() - 1).getId() + 1;
                if(!this.checkGenesis())
                    throw new Exception("Errore: è presente più di un blocco genesi!");
            }
        } else {
            currentID = 0;
            if (reset)
                blockchainFile.delete();
            if (!blockchainFile.exists())
                blockchainFile.createNewFile();
        }
    }

    public int getCurrentID() {
        return currentID;
    }

    public void setCurrentID(int currentID) {
        this.currentID = currentID;
    }

    // Metodi Pubblici

    /**
     * Aggiungendo una transazione a questa JokerChain.
     * 
     * @param transaction - Transazione da inserire nella JokerChain.
     * @throws IOException Se si verifica un errore di I/O durante l'esecuzione sul file.
     */
    public void addTransaction(Transaction transaction) throws IOException {
        transaction.setId(currentID);
        transactions.add(transaction);
        if (transaction.getType() == 5)
            currentID++;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(blockchainFile, true))) {
            writer.write(transaction.toCSV());
            writer.newLine();
        }
    }

    /**
     * Calcola la Merkle Root per un determinato tipo di transazione e lo fa chiamando il metodo privato computeMerkleRoot(hashes).
     * 
     * @param type Il tipo di transazione.
     * @return La Merkle Root calcolata.
     * @throws Exception Se si verifica un errore durante il calcolo della Merkle Root.
     */
    public String calculateMerkleRootForType(int type) throws Exception {
        List<Transaction> transactionsOfType = filterTransaction(type, this.currentID);
        List<String> hashes = transactionsOfType.stream()
                                .map(Transaction::getHash)
                                .collect(Collectors.toList());
    
        return computeMerkleRoot(hashes);
    }    

    /**
     * Crea il blocco genesi di questa JokerChain.
     * 
     * @param admPublicKey - Chiave pubblica del certificato del nodo ADM (adm_cert.pem)
     * @param jokerPublicKey - Chiave pubblica del certificato del banco (joker_cert.pem)
     * @throws IOException - Se si verifica un errore di I/O durante l'esecuzione sul file.
     */
    public void createGenesisBlock(PublicKey admPublicKey, PublicKey jokerPublicKey) throws IOException {
        String admPK = Base64.getEncoder().encodeToString(admPublicKey.getEncoded());
        String jokerPK = Base64.getEncoder().encodeToString(jokerPublicKey.getEncoded());
        this.addTransaction(new Transaction(1, admPK, "ADM"));
        this.addTransaction(new Transaction(1, jokerPK, "Joker"));
        this.currentID++;
    }

    /**
     * Restituisce la lista di transazioni che costituiscono questa JokerChain.
     * 
     * @return Lista di transazioni della JokerChain.
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Filtra le transazioni in base al tipo e all'ID.
     * 
     * @param type - Il tipo di transazione da filtrare.
     * @param id - L'ID della transazione da filtrare.
     * @return Una lista di transazioni filtrate.
     */
    public List<Transaction> filterTransaction(int type, int id){
        List<Transaction> transactionsOfType = new ArrayList<Transaction>();
        transactionsOfType = this.transactions.stream()
                                .filter(t -> t.getType() == type && t.getId() == id)
                                .collect(Collectors.toList());
        return transactionsOfType;
    }

    // Metodi Privati

    /**
     * Verifica se il blocco genesis è valido: è presente un solo blocco genesi e coincide con l'id '0'.
     * 
     * @return true se il blocco genesis è valido, altrimenti false.
     */
    private boolean checkGenesis() {
        int[] totalID = IntStream.range(0, this.currentID).toArray();
        List<Transaction> genesisTransactions = new ArrayList<Transaction>();
        for(int id: totalID){
            genesisTransactions = filterTransaction(1, id);
            if(id != 0 && !genesisTransactions.isEmpty())
                return false;
        }
        return true;
    }

    /**
     * Calcola il Merkle Root sulla lista di hash che identificano le transazioni di questa JokerChain.
     * 
     * @param hashes - Hash delle transazioni della JokerChain.
     * @return Il valore del Merkle Root.
     * @throws Exception - Se si verifica un errore durante il calcolo dell'hash con SHA256.
     */
    private String computeMerkleRoot(List<String> hashes) throws Exception {
        if (hashes.size() == 1)
            return hashes.get(0);

        List<String> newHashes = new ArrayList<>();
        for (int i = 0; i < hashes.size(); i += 2) {
            if (i + 1 < hashes.size())
                newHashes.add(Transaction.sha256(hashes.get(i) + hashes.get(i + 1)));
            else
                newHashes.add(Transaction.sha256(hashes.get(i)));
            }
        return computeMerkleRoot(newHashes);
    }

}

/**
 * Classe Transaction
 */
class Transaction {

    /**
     * Calcola l'hash di una stringa di input con SHA-256.
     * 
     * @param input La stringa da cui calcolare l'hash.
     * @return L'hash calcolato con SHA-256.
     * @throws Exception Se si verifica un errore durante il calcolo.
     */
    public static String sha256(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes("UTF-8"));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    } 
    private int id;
    private int type;
    private String data;

    private String hash;

    private String sender;   

    /**
     * Costruttore di Transaction con tutti i dettagli.
     * 
     * @param id - L'ID della transazione.
     * @param type - Il tipo di transazione.
     * @param data - I dati della transazione.
     * @param hash - L'hash della transazione.
     * @param sender - Il mittente della transazione.
     */
    public Transaction(int id, int type, String data, String hash, String sender) {
        this.id = id;
        this.type = type;
        this.data = data;
        this.hash = hash;
        this.sender = sender;
    }

    /**
     * Costruttore di Transaction con calcolo automatico dell'hash.
     * 
     * @param id - L'ID della transazione.
     * @param type - Il tipo di transazione.
     * @param data - I dati della transazione.
     * @param sender - Il mittente della transazione.
     */
    public Transaction(int id, int type, String data, String sender) {
        this.id = id;        
        this.type = type;
        this.data = data;
        try {
            this.hash = sha256(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.sender = sender;
    }

    /**
     * Costruttore di Transaction senza ID specificato e con calcolo automatico dell'hash.
     * 
     * @param type - Il tipo di transazione.
     * @param data - I dati della transazione.
     * @param sender - Il mittente della transazione.
     */
    public Transaction(int type, String data, String sender) {
        this.type = type;
        this.data = data;
        try {
            this.hash = sha256(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.sender = sender;
    }
    
    /**
     * Restituisce l'istanza dell'Id associata a questa Transaction.
     * 
     * @return Il valore id della transazione 'id'.
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta un'istanza dell'Id per questa transazione.
     * 
     * @param p - L'istanza dell'Id 'id' da impostare.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Restituisce il tipo di transazione associata a questa Transaction.
     * 
     * @return Il tipo della transazione 'type'.
     */
    public int getType() {
        return type;
    }

    /**
     * Imposta un'istanza del tipo di transazione.
     * 
     * @param p - L'istanza del tipo di transazione 'type' da impostare.
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Restituisce i dati associati a questa Transaction.
     * 
     * @return I dati della transazione 'data'.
     */
    public String getData() {
        return data;
    }

    /**
     * Imposta un'istanza dei dati 'data' per questa transazione.
     * 
     * @param p - L'istanza dei dati 'data' da impostare.
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Restituisce l'hash di questa Transaction.
     * 
     * @return L'hash della transazione 'sender'.
     */
    public String getHash() {
        return hash;
    }

    /**
     * Imposta un'istanza dell'hash 'hash' per questa transazione.
     * 
     * @param p - L'istanza dell'hash 'hash' da impostare.
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * Restituisce il mittente di questa Transaction.
     * 
     * @return Il mittente della transazione 'sender'.
     */
    public String getSender() {
        return sender;
    }

    /**
     * Imposta un'istanza di colui che ha richiesto la generazione della transazioni 'sender'.
     * 
     * @param p - L'istanza del mittente della transazione 'sender' da impostare.
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * Rappresentazione CSV degli attributi di questa Transaction.
     * @return Una stringa che rappresenta gli attributi della classe Transaction nel formato CSV.
     */
    public String toCSV() {
        return id + ";" + type + ";" + data + ";" + hash + ";" + sender;
    }

    @Override
    public String toString() {
        return "Transaction [hash=" + hash + ", data=" + data + ", type=" + type + ", id=" + id + "]";
    }

}
