import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;

public class Manager {

    protected static final int MEMBER_NUMBER = 9;

    public static HashMap<Integer, PublicKey> publicKeys = new HashMap<>();
    public static HashMap<Integer, PrivateKey> privateKeys = new HashMap<>();

    public static HashMap<Integer, GroupMember> members = new HashMap<>();

    public static Message message = null;

    public static void groupSetter(int size) {
        Manager.cleanManager();
        Manager.keyLoader();
        for(int i=1; i<=size; i++) {
            members.put(i, new GroupMember(i));
        }
        System.out.println("Created group of " + members.size() + " members");
    }

    public static void keyLoader() {
        try {
            // Load public keys
            for(int i=1; i<=MEMBER_NUMBER; i++) {
                byte[] keyBytes = Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "\\src\\main\\resources\\User" + i + ".pub"));
                X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
                KeyFactory kf = KeyFactory.getInstance("RSA");
                publicKeys.put(i, kf.generatePublic(spec));
            }
            System.out.println("Public keys loaded");

            // Load private keys
            for(int i=1; i<=MEMBER_NUMBER; i++) {
                byte[] keyBytes = Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "\\src\\main\\resources\\User" + i + ".key"));
                PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
                KeyFactory kf = KeyFactory.getInstance("RSA");
                privateKeys.put(i, kf.generatePrivate(spec));
            }
            System.out.println("Private keys loaded");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cleanManager() {
        publicKeys = new HashMap<>();
        privateKeys = new HashMap<>();
        members = new HashMap<>();
        message = null;
        System.out.println("Manager cleaned");
    }
}
