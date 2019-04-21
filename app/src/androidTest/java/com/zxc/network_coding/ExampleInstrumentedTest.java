package com.zxc.network_coding;

import android.content.Context;
import android.util.Log;

import com.zxc.jni.Matrix;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import static com.zxc.network_coding.utils.DataUtils.M;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    public static final String TAG = "ExampleInstrumentedTest" ;
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.zxc.network_coding", appContext.getPackageName());
        byte[] testM1 = {
                0x0F, 0x1F, 0x2F, 0x3F, 0x4F, 0x5F, 0x6F,//15, 31, 47, 63, 79, 95, 111
                0x0F, 0x1F, 0x2F, 0x3F, 0x4F, 0x5F, 0x6F //15, 31, 47, 63, 79, 95, 111
        };
        byte[] testM2 = {
                0x0F, 0x1F, 0x2F, 0x3F, 0x4F, 0x5F, 0x6F,//15, 31, 47, 63, 79, 95, 111
                0x0F, 0x1F, 0x2F, 0x3F, 0x4F, 0x5F, 0x6F //15, 31, 47, 63, 79, 95, 111
        };
        Matrix m = new Matrix(8);
        byte[] multiply = m.multiply(testM1, 5, 2, testM2, 2, 5);
        Log.d(TAG, Arrays.toString(multiply)) ;
    }
}
