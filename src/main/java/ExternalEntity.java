import javax.crypto.Cipher;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;

public class ExternalEntity {

    public static void verifySignature() {
        try {
            if (Manager.message == null) {
                System.out.println("No message with signature to verify");
                return;
            }

            // Step 1 - Decipher all random values

            HashMap<Integer, byte[]> cipheredRandomValues = Manager.message.getRandomValues();

            HashMap<Integer, byte[]> randomValues = new HashMap<>();

            for (int i = 1; i <= cipheredRandomValues.size(); i++) {
                if (cipheredRandomValues.get(i) != null) {
                    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                    cipher.init(Cipher.DECRYPT_MODE, Manager.publicKeys.get(i));

                    byte[] input = cipheredRandomValues.get(i);
                    cipher.update(input);

                    byte[] cipherValue = cipher.doFinal();
                    System.out.println(Arrays.toString(cipherValue));
                    randomValues.put(i, cipherValue);
                }
            }

            System.out.println("Step 1 completed");

            // Step 2 - Get the hash of the message

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(Manager.message.getMessage().getBytes());

            System.out.println(Arrays.toString(messageDigest));
            System.out.println("Step 2 completed");

            // Step 3 - Verify that the ring equation holds

            byte[] toCipher = Manager.message.randomBytes;
            //First cycle is different
            for(int j=0; j<245; j++) {
                toCipher[j] = (byte) (Manager.message.randomBytes[j] ^ randomValues.get(1)[j]);
            }
            toCipher = cipher(toCipher, messageDigest);

            for(int i=2; i<=cipheredRandomValues.size(); i++) {
                for(int j=0; j<245; j++) {
                    toCipher[i] = (byte) (toCipher[i] ^ randomValues.get(i)[j]);
                }
                toCipher = cipher(toCipher, messageDigest);
            }

            if(Arrays.equals(toCipher, Manager.message.randomBytes)) {
                System.out.println("Signature successfully verified");
            } else {
                System.out.println("Signature did not match message");
            }

            System.out.println("Actual");
            System.out.println(Arrays.toString(toCipher));
            System.out.println("Expected");
            System.out.println(Arrays.toString(Manager.message.randomBytes));

            System.out.println("Step 3 completed");
            System.out.println("Verification complete");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] cipher(byte[] toCipher, byte[] key) {
        try {
            byte[] cipheredBytes = new byte[245];
            for(int i=0; i<245; i++) {
                cipheredBytes[i] = (byte) (toCipher[i] ^ key[i%64]);
            }
            return cipheredBytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
