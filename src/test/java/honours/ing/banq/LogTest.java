package honours.ing.banq;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Jeffrey Bakker
 */
public class LogTest {

    @Before
    public void setUp() {
        try {
            Log.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testStackTraceString() {
        Exception e = new Exception("Test");
        Log.e(this.getClass().getSimpleName(), "An error occurred", e);
    }

}
