package JokerCasino.JokerChain.Utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

public class Generator {

    /**
     * Funzione per la generazione di una coppia di chiavi, pubblica e privata, utilizzando RSA come algoritmo.
     * @return Una coppia di chiavi, pubblica e privata.
     */
    public static KeyPair generateKeyPair() {
        try {
            SecureRandom secureRandom = new SecureRandom();
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048, secureRandom); // Utilizzo di SecureRandom
            KeyPair keyPair = keyGen.generateKeyPair();
            return keyPair;            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Funzione per la generazione di una stringa casuale.
     * @return Stringa casuale di 32 bit.
     */
    public static byte[] generatePRG() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes); 
        return randomBytes;           
    }

}
