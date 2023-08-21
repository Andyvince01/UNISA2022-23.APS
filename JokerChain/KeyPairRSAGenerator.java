package JokerChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

public class KeyPairRSAGenerator {

    public static KeyPair generate() {
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
}
