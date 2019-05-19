import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ManagerTest {

    @Before
    public void setUp() {
    }

    @Test
    public void groupSetterTest() {
        Manager.groupSetter(9);
        Assert.assertEquals(9, Manager.publicKeys.size());
        Assert.assertEquals(9, Manager.privateKeys.size());
        for(int i=1; i<=Manager.MEMBER_NUMBER; i++) {
            Assert.assertNotNull(Manager.publicKeys.get(i));
            Assert.assertNotNull(Manager.privateKeys.get(i));
        }
        Assert.assertEquals(9, Manager.members.size());
        for(int i=1; i<=Manager.MEMBER_NUMBER; i++) {
            Assert.assertNotNull(Manager.members.get(i));
            Assert.assertEquals(i, Manager.members.get(i).id);
        }
    }

    @Test
    public void keyLoaderTest() {
        Assert.assertEquals(0, Manager.publicKeys.size());
        Assert.assertEquals(0, Manager.privateKeys.size());
        Manager.keyLoader();
        Assert.assertEquals(9, Manager.publicKeys.size());
        Assert.assertEquals(9, Manager.privateKeys.size());
        for(int i=1; i<=Manager.MEMBER_NUMBER; i++) {
            Assert.assertNotNull(Manager.publicKeys.get(i));
            Assert.assertNotNull(Manager.privateKeys.get(i));
        }
    }

    @Test
    public void cleanManagerTest() {
        Manager.privateKeys.put(1, null);
        Manager.publicKeys.put(1, null);
        for(int i=1; i<=Manager.MEMBER_NUMBER; i++) {
            Manager.members.put(1, new GroupMember(i));
        }
        Assert.assertEquals(1, Manager.privateKeys.size());
        Assert.assertEquals(1, Manager.publicKeys.size());
        Assert.assertEquals(1, Manager.members.size());
        Manager.cleanManager();
        Assert.assertEquals(0, Manager.privateKeys.size());
        Assert.assertEquals(0, Manager.publicKeys.size());
        Assert.assertEquals(0, Manager.members.size());
    }

    @After
    public void tearDown() {
        Manager.cleanManager();
    }
}
