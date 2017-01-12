package com.exynos5420.deathlyadiutor.ads.utils.kernel;

import android.content.Context;

import com.exynos5420.deathlyadiutor.ads.utils.Constants;
import com.exynos5420.deathlyadiutor.ads.utils.Utils;
import com.exynos5420.deathlyadiutor.ads.utils.root.Control;

/**
 * Created by Martin Bouchet on 15/11/2016.
 */

public class GPUThermal implements Constants {

    public static int getFreq(int step) {
        String value;
        //I am hardcoding the second index for now...
        if (Utils.existFile(GPU_THRORRLING_FREQS_ARRAY[step][0]))
            value = Utils.readFile(GPU_THRORRLING_FREQS_ARRAY[step][0]);
        else if (Utils.existFile(GPU_THRORRLING_FREQS_ARRAY[step][1]))
            value = Utils.readFile(GPU_THRORRLING_FREQS_ARRAY[step][1]);
        else return -1;

        return Utils.stringToInt(value);

    }

    public static int getTemp(int step) {
        String value = Utils.readFile(GPU_THRORRLING_TEMPS_ARRAY[step]);
        return Utils.stringToInt(value);
    }

    public static void setGPUTripPointTemp(int temp, int step, Context context) {

        Control.runCommand(Integer.toString(temp), GPU_THRORRLING_TEMPS_ARRAY[step], Control.CommandType.GENERIC, context);

    }

    public static void setGPUTripPointFreq(int freq, int step, Context context) {
        //I am hardcoding the second index for now...
        if (Utils.existFile(GPU_THRORRLING_FREQS_ARRAY[step][0]))
            Control.runCommand(Integer.toString(freq), GPU_THRORRLING_FREQS_ARRAY[step][0], Control.CommandType.GENERIC, context);
        else if (Utils.existFile(GPU_THRORRLING_FREQS_ARRAY[step][1]))
            Control.runCommand(Integer.toString(freq), GPU_THRORRLING_FREQS_ARRAY[step][1], Control.CommandType.GENERIC, context);
    }
}
