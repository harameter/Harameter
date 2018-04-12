package com.example.harameter.harameter;

import org.junit.Test;
import java.util.regex.Pattern;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class Iteration2UnitTest {
    @Test
    public void calibration_is_correct() throws Exception {
        assertEquals(BluetoothActivity.adjust(10, 0, 5), 5, 0);
    }
}