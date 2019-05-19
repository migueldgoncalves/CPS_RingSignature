import javax.crypto.Cipher;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;

public class GroupMember {

    public int id;

    public GroupMember(int id) {
        this.id = id;
    }

    public void sign(String message) {
        try {

            // Step 1 - Obtain hash of the message

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(message.getBytes());

            System.out.println(Arrays.toString(messageDigest));
            System.out.println("Step 1 completed");

            // Step 2 - Get random value v

            SecureRandom random = new SecureRandom();
            byte[] randomBytes = new byte[245];
            random.nextBytes(randomBytes);

            System.out.println(Arrays.toString(randomBytes));
            System.out.println("Step 2 completed");

            // Step 3 - Get random values for all other group members and cipher them with their private keys

            HashMap<Integer, byte[]> randomValues = new HashMap<>(); //Size = Members - 1
            byte[] randomBytesForOtherMembers = new byte[245];
            for(int i=1; i<=Manager.members.size(); i++) {
                if(i!=id) {
                    random.nextBytes(randomBytesForOtherMembers);
                    randomValues.put(i, randomBytesForOtherMembers);
                }
            }

            HashMap<Integer, byte[]> cipheredRandomValues = new HashMap<>();

            for(int i=1; i<=Manager.members.size(); i++) {
                if (randomValues.get(i) != null) {
                    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                    cipher.init(Cipher.ENCRYPT_MODE, Manager.privateKeys.get(i));

                    byte[] input = randomValues.get(i);
                    cipher.update(input);

                    byte[] cipherValue = cipher.doFinal(); //Adds padding
                    System.out.println(Arrays.toString(cipherValue));
                    cipheredRandomValues.put(i, cipherValue);
                }
            }

            System.out.println("Step 3 completed");

            // Step 4 - Solve the ring equation to get the random value for this member

            byte[] toDecipher = randomBytes;
            for(int i=1; i<=Manager.members.size(); i++) {
                if(randomValues.get(i)!=null) {
                    byte[] decipheredValue = decipher(toDecipher, messageDigest);
                    for (int j = 0; j < 245; j++) {
                        toDecipher[j] = (byte) (decipheredValue[j] ^ randomValues.get(i)[j]);
                    }
                }
            }
            //One cycle remaining
            byte[] randomValueForThisMember = new byte[245];
            byte[] decipheredValue = decipher(toDecipher, messageDigest);
            for(int j=0; j<245; j++) {
                randomValueForThisMember[j] = (byte) (decipheredValue[j] ^ randomBytes[j]);
            }

            System.out.println(Arrays.toString(randomValueForThisMember));
            System.out.println("Step 4 completed");

            // Step 5 - Cipher random value for this member using its private key

            Cipher decipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            decipher.init(Cipher.ENCRYPT_MODE, Manager.privateKeys.get(id));

            byte[] cipheredRandomValueForThisMember = decipher.doFinal(randomValueForThisMember);
            cipheredRandomValues.put(id, cipheredRandomValueForThisMember);

            System.out.println(Arrays.toString(cipheredRandomValueForThisMember));
            System.out.println("Step 5 completed");

            // Step 6 - Create the ring signature

            Message signedMessage = new Message();
            signedMessage.setMessage(message);
            signedMessage.setPublicKeys(Manager.publicKeys);
            signedMessage.setRandomBytes(randomBytes);
            signedMessage.setRandomValues(cipheredRandomValues);

            Manager.message = signedMessage;

            System.out.println("Step 6 completed");
            System.out.println("Message signed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] decipher(byte[] toDecipher, byte[] key) {
        try {
            byte[] decipheredBytes = new byte[245];
            for(int i=0; i<245; i++) {
                decipheredBytes[i] = (byte) (toDecipher[i] ^ key[i%64]);
            }
            return decipheredBytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
