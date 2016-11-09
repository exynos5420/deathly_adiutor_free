/*
 * Copyright (C) 2015 Willi Ye
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.exynos5420.deathlyadiutor.utils.kernel;

import android.content.Context;

import com.exynos5420.deathlyadiutor.utils.Constants;
import com.exynos5420.deathlyadiutor.utils.Utils;
import com.exynos5420.deathlyadiutor.utils.root.Control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by willi on 26.12.14.
 */
public class GPU implements Constants {

    private static String GPU_SCALING_GOVERNOR;
    private static String[] GPU_AVAILABLE_GOVERNORS;

    private static Integer[] mGpuFreqs;

    public static void setGpuGovernor(String governor, Context context) {
        if (GPU_SCALING_GOVERNOR != null)
            Control.runCommand(governor, GPU_SCALING_GOVERNOR, Control.CommandType.GENERIC, context);
    }

    public static List<String> getGpuGovernors() {
        if (GPU_AVAILABLE_GOVERNORS == null)
            for (String file : GPU_AVAILABLE_GOVERNORS_ARRAY)
                if (GPU_AVAILABLE_GOVERNORS == null)
                    if (Utils.existFile(file)) {
                        String value = Utils.readFile(file);
                        if (value != null)
                            GPU_AVAILABLE_GOVERNORS = value.split(" ");
                        Collections.sort(Arrays.asList(GPU_AVAILABLE_GOVERNORS), String.CASE_INSENSITIVE_ORDER);
                    }
        return new ArrayList<>(Arrays.asList(GPU_AVAILABLE_GOVERNORS == null ? GPU_GENERIC_GOVERNORS
                .split(" ") : GPU_AVAILABLE_GOVERNORS));
    }

    public static String getGpuGovernor() {
        if (GPU_SCALING_GOVERNOR != null)
            if (Utils.existFile(GPU_SCALING_GOVERNOR)) {
                String value = Utils.readFile(GPU_SCALING_GOVERNOR);
                if (value != null) return value;
            }
        return "";
    }

    public static boolean hasGpuGovernor() {
        if (GPU_SCALING_GOVERNOR == null)
            for (String file : GPU_SCALING_GOVERNOR_ARRAY)
                if (Utils.existFile(file)) GPU_SCALING_GOVERNOR = file;
        return GPU_SCALING_GOVERNOR != null;
    }

    public static void setGpuMinFreq(int freq, Context context) {
            Control.runCommand("0", GPU_EXYNOS5_DVFS, Control.CommandType.GENERIC, context);
            Control.runCommand(String.valueOf(freq), GPU_MIN_EXYNOS5_FREQ, Control.CommandType.GENERIC, context);
            Control.runCommand("1", GPU_EXYNOS5_DVFS, Control.CommandType.GENERIC, context);
    }

    public static void setGpuMaxFreq(int freq, Context context) {
        Control.runCommand("0", GPU_EXYNOS5_DVFS, Control.CommandType.GENERIC, context);
        Control.runCommand(String.valueOf(freq), GPU_MAX_EXYNOS5_FREQ, Control.CommandType.GENERIC, context);
        Control.runCommand("1", GPU_EXYNOS5_DVFS, Control.CommandType.GENERIC, context);
    }

    public static List<Integer> getGpuFreqs() {
            if (mGpuFreqs == null) {
                String value = Utils.readFile(GPU_AVALIBLE_EXYNOS5_FREQS);
                if (value != null) {
                    String[] freqs = value.split(" ");
                    mGpuFreqs = new Integer[freqs.length];
                    for (int i = 0; i < mGpuFreqs.length; i++) {
                            mGpuFreqs[i] = Utils.stringToInt(freqs[i]);
                    }
                }
            }
        return new ArrayList<>(Arrays.asList(mGpuFreqs));
    }

    public static int getGpuMinFreq() {
            String value = Utils.readFile(GPU_MIN_EXYNOS5_FREQ);
            if (value != null) return Utils.stringToInt(value);
        return 0;
    }

    public static int getGpuMaxFreq() {
            String value = Utils.readFile(GPU_MAX_EXYNOS5_FREQ);
            if (value != null) return Utils.stringToInt(value);
        return 0;
    }

    public static int getGpuCurFreq() {
            String value = Utils.readFile(GPU_CUR_EXYNOS5_FREQ);
            if (value != null && Utils.stringToInt(value) != 0) {
                return Utils.stringToInt(value);
            }
            else if (value != null) {
                return getGpuMinFreq();
            }
          return 0;
    }

    public static int getGpuThrottling(int step) {
        if (step <= 4 && step >= 0){
            String value;
            value = Utils.readFile(GPU_THERMAL_THRORRLING_ARRAY[step]);
            if (value != null){
                return Utils.stringToInt(value);
            }
            return 0;
        }
        return 0;
    }

    public static void setGpuThrottling(int freq, int step, Context context) {
        if (GPU_THERMAL_THRORRLING_ARRAY != null)
        Control.runCommand("0", GPU_EXYNOS5_DVFS, Control.CommandType.GENERIC, context);
        Control.runCommand(String.valueOf(freq), GPU_THERMAL_THRORRLING_ARRAY[step], Control.CommandType.GENERIC, context);
        Control.runCommand("1", GPU_EXYNOS5_DVFS, Control.CommandType.GENERIC, context);

    }


}
