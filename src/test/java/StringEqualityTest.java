import junit.framework.TestCase;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class StringEqualityTest extends TestCase {

    public void testEquality(String a, String b) throws Exception {
        assertTrue(a.equals(b));
        assertFalse(a == b);
        assertEquals(a, b);
    }

}