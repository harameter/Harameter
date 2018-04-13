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
        double weights [] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        assertEquals(BluetoothActivity.movingWindowWeightedAverage(weights, numbers, 55.0), 1.0, 0.0);
    }
}