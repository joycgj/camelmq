package camel.tc.cmq.utils;

import org.junit.Assert;
import org.junit.Test;

// done
public class CharsetUtilsTest {

    @Test
    public void testToUTF8Bytes() {
        Assert.assertArrayEquals(new byte[0], CharsetUtils.toUTF8Bytes(""));
        Assert.assertArrayEquals(new byte[0], CharsetUtils.toUTF8Bytes(null));
        Assert.assertArrayEquals(new byte[]{102, 111, 111},
                CharsetUtils.toUTF8Bytes("foo"));
    }

    @Test
    public void testToUTF8String() {
        Assert.assertEquals("", CharsetUtils.toUTF8String(null));
        Assert.assertEquals("", CharsetUtils.toUTF8String(new byte[0]));
        Assert.assertEquals("foo",
                CharsetUtils.toUTF8String(new byte[]{102, 111, 111}));
    }
}
