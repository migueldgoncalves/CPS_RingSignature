import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class CipherTest {

    @Before
    public void setUp() {

    }

    @Test
    public void cipherDecipherTest() {
        try {
            byte[] message = new byte[245];
            for (int i = 0; i < 245; i++)
                message[i] = (byte) i;
            byte[] key = new byte[64];
            new Random().nextBytes(key);

            byte[] cipheredMessage = ExternalEntity.cipher(message, key);
            System.out.println(Arrays.toString(message));
            System.out.println(Arrays.toString(cipheredMessage));
            System.out.println(Arrays.toString(new GroupMember(1).decipher(cipheredMessage, key)));
            Assert.assertArrayEquals(message, new GroupMember(2).decipher(cipheredMessage, key));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @After
    public void tearDown() {

    }
}
