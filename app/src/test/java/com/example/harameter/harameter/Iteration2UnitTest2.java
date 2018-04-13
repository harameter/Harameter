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



public class Iteration2UnitTest2 {
    @Test

    public void movingWindowCorrect() throws Exception {
        double numbers [] = new double[10];
        double weights [] = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        assertEquals(BluetoothActivity.movingWindowWeightedAverage(weights, numbers, 55.0), 10.0, 0.0);
    }
}