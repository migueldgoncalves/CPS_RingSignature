import java.security.PublicKey;
import java.util.HashMap;

public class Message {

    public String message;
    public HashMap<Integer, PublicKey> publicKeys;
    public byte[] randomBytes;
    public HashMap<Integer, byte[]> randomValues;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HashMap<Integer, PublicKey> getPublicKeys() {
        return publicKeys;
    }

    public void setPublicKeys(HashMap<Integer, PublicKey> publicKeys) {
        this.publicKeys = publicKeys;
    }

    public byte[] getRandomBytes() {
        return randomBytes;
    }

    public void setRandomBytes(byte[] randomBytes) {
        this.randomBytes = randomBytes;
    }

    public HashMap<Integer, byte[]> getRandomValues() {
        return randomValues;
    }

    public void setRandomValues(HashMap<Integer, byte[]> randomValues) {
        this.randomValues = randomValues;
    }
}
