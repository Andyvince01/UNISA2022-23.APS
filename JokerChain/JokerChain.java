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
                    throw new Exception("Errore: è presente più di un blocco genesis!");
            }
        } else {
            currentID = 0;
            if (reset)
                blockchainFile.delete();
            if (!blockchainFile.exists())
                blockchainFile.createNewFile();
        }
    }

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

    public String calculateMerkleRootForType(int type) throws Exception {
        List<Transaction> transactionsOfType = filterTransaction(type, this.currentID);
        List<String> hashes = transactionsOfType.stream()
                                .map(Transaction::getHash)
                                .collect(Collectors.toList());
    
        return computeMerkleRoot(hashes);
    }    

    public void createGenesisBlock(PublicKey admPublicKey, PublicKey jokerPublicKey) throws IOException {
        String admPK = Base64.getEncoder().encodeToString(admPublicKey.getEncoded());
        String jokerPK = Base64.getEncoder().encodeToString(jokerPublicKey.getEncoded());
        this.addTransaction(new Transaction(1, admPK, "ADM"));
        this.addTransaction(new Transaction(1, jokerPK, "Joker"));
        this.currentID++;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    /* Metodi Privati */
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

    private String computeMerkleRoot(List<String> hashes) throws Exception {
        if (hashes.size() == 1)
            return hashes.get(0);

        List<String> newHashes = new ArrayList<>();
        for (int i = 0; i < hashes.size(); i += 2) {
            if (i + 1 < hashes.size())
                newHashes.add(sha256(hashes.get(i) + hashes.get(i + 1)));
            else
                newHashes.add(sha256(hashes.get(i)));
            }
        return computeMerkleRoot(newHashes);
    }

    private List<Transaction> filterTransaction(int type, int id){
        List<Transaction> transactionsOfType = new ArrayList<Transaction>();
        transactionsOfType = this.transactions.stream()
                                .filter(t -> t.getType() == type && t.getId() == id)
                                .collect(Collectors.toList());
        return transactionsOfType;
    }

    private String sha256(String input) throws Exception {
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

}

class Transaction {
    private int id;
    private int type; 
    private String data;
    private String hash;
    private String sender;

    public Transaction(int id, int type, String data, String hash, String sender) {
        this.id = id;
        this.type = type;
        this.data = data;
        this.hash = hash;
        this.sender = sender;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String toCSV() {
        return id + ";" + type + ";" + data + ";" + hash + ";" + sender;
    }

    @Override
    public String toString() {
        return "Transaction [hash=" + hash + ", data=" + data + ", type=" + type + ", id=" + id + "]";
    }

    // Metodi Privati
    private String sha256(String input) throws Exception {
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

}
