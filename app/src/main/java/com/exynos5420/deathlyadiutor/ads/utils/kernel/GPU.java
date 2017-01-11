/*
 *  Copyright (C) 2016 Martin Bouchet
 *  Copyright (C) 2015 Willi Ye
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

package com.exynos5420.deathlyadiutor.ads.utils.kernel;

import android.content.Context;

import com.exynos5420.deathlyadiutor.ads.utils.Constants;
import com.exynos5420.deathlyadiutor.ads.utils.Utils;
import com.exynos5420.deathlyadiutor.ads.utils.root.Control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by willi on 26.12.14.
 */
public class GPU implements Constants {

    private static String GPU_CURRENT_POWERPOLICY;
    private static String GPU_CURRENT_GOVERNOR;

    private static Integer[] mGpuFreqs;

    public static void setGpuPowerPolicy(String governor, Context context) {
        Control.runCommand("0", GPU_EXYNOS5_DVFS, Control.CommandType.GENERIC, context);
        Control.runCommand(governor, GPU_AVALIBLE_EXYNOS5_POWERP, Control.CommandType.GENERIC, context);
        Control.runCommand("1", GPU_EXYNOS5_DVFS, Control.CommandType.GENERIC, context);
    }

    public static List<String> getGpuPowerPolicies() {
        String value = Utils.readFile(GPU_AVALIBLE_EXYNOS5_POWERP);
        String[] AvailiblePowerPolicies = value.split(" ");
        for (int i = 0; i < AvailiblePowerPolicies.length; i++) {
            if (AvailiblePowerPolicies[i].contains("[")) {
                AvailiblePowerPolicies[i] = AvailiblePowerPolicies[i].substring(1, AvailiblePowerPolicies[i].length() - 1);
                GPU_CURRENT_POWERPOLICY = AvailiblePowerPolicies[i];
            }
        }
        Collections.sort(Arrays.asList(AvailiblePowerPolicies), String.CASE_INSENSITIVE_ORDER);
        return new ArrayList<>(Arrays.asList(AvailiblePowerPolicies));
    }

    public static String getGpuPowerPolicy() {
        if (Utils.existFile(GPU_AVALIBLE_EXYNOS5_POWERP)) {
            if (GPU_CURRENT_POWERPOLICY != null) {
                return GPU_CURRENT_POWERPOLICY;
            } else {
                getGpuPowerPolicies();
                return GPU_CURRENT_POWERPOLICY;
            }
        } else return "-1";
    }

    public static void setGpuGovernor(String governor, Context context) {
        Control.runCommand("0", GPU_EXYNOS5_DVFS, Control.CommandType.GENERIC, context);
        Control.runCommand(Integer.toString(getGpuAvailibleGovernors().indexOf(governor)), GPU_AVALIBLE_EXYNOS5_GOVS, Control.CommandType.GENERIC, context);
        Control.runCommand("1", GPU_EXYNOS5_DVFS, Control.CommandType.GENERIC, context);
    }

    public static List<String> getGpuAvailibleGovernors() {
        String value = Utils.readFile(GPU_AVALIBLE_EXYNOS5_GOVS);
        String[] tempGovs = value.split("\n");
        String[] AvailibleGovernors = new String[tempGovs.length - 1];
        for (int i = 0; i < AvailibleGovernors.length; i++) {
            AvailibleGovernors[i] = tempGovs[i];
        }
        GPU_CURRENT_GOVERNOR = tempGovs[tempGovs.length - 1].split("] ")[1];
        return new ArrayList<>(Arrays.asList(AvailibleGovernors));
    }

    public static String getGpuGovernor() {
        if (Utils.existFile(GPU_CURRENT_GOVERNOR)) {
            if (GPU_CURRENT_GOVERNOR != null) {
                return GPU_CURRENT_GOVERNOR;
            } else {
                getGpuPowerPolicies();
                return GPU_CURRENT_GOVERNOR;
            }
        } else return "-1";
    }

    public static void setGpuMinFreq(int freq, Context context) {
        String path = getNodePath(GPU_MIN_EXYNOS5_FREQ);
        //MM r7p0 gpu driver wont accept anything higher than 350mhz for min freq.
        if (android.os.Build.VERSION.SDK_INT < 24 && freq < 350) freq = 350;
        Control.runCommand("0", GPU_EXYNOS5_DVFS, Control.CommandType.GENERIC, context);
        Control.runCommand(String.valueOf(freq), path, Control.CommandType.GENERIC, context);
        Control.runCommand("1", GPU_EXYNOS5_DVFS, Control.CommandType.GENERIC, context);
    }

    public static void setGpuMaxFreq(int freq, Context context) {
        String path = getNodePath(GPU_MAX_EXYNOS5_FREQ);
        Control.runCommand("0", GPU_EXYNOS5_DVFS, Control.CommandType.GENERIC, context);
        Control.runCommand(String.valueOf(freq), path, Control.CommandType.GENERIC, context);
        Control.runCommand("1", GPU_EXYNOS5_DVFS, Control.CommandType.GENERIC, context);
    }

    public static List<Integer> getGpuFreqs() {
        if (mGpuFreqs == null) {
            if (Utils.existFile(GPU_AVALIBLE_EXYNOS5_FREQS)) {
                String value = Utils.readFile(GPU_AVALIBLE_EXYNOS5_FREQS);
                if (value != null) {
                    String[] freqs = value.split(" ");
                    mGpuFreqs = new Integer[freqs.length];
                    for (int i = 0; i < mGpuFreqs.length; i++) {
                        mGpuFreqs[i] = Utils.stringToInt(freqs[i]);
                    }
                }
            } else if (Utils.existFile(GPU_VOLTAGE_EXYNOS5_FILE[0])){
                String value = Utils.readFile(GPU_VOLTAGE_EXYNOS5_FILE[0]);
                if (value != null) {
                    String[] lines;
                    lines = value.split("\n");
                    mGpuFreqs = new Integer[lines.length];
                    for (int i = 0; i < lines.length; i++) {
                        mGpuFreqs[i] = Utils.stringToInt(lines[i].split(" ")[0].trim());
                    }
                }
            }
        }
        return new ArrayList<>(Arrays.asList(mGpuFreqs));
    }

    public static int getGpuMinFreq() {
        String path = getNodePath(GPU_MIN_EXYNOS5_FREQ);
        if (path.equals("-1")) return -1;
        else {
            String value = Utils.readFile(path);
            if (value != null) return Utils.stringToInt(value);
        }
        return -1;
    }

    public static int getGpuMaxFreq() {
        String path = getNodePath(GPU_MAX_EXYNOS5_FREQ);
        if (path.equals("-1")) return -1;
        else {
            String value = Utils.readFile(path);
            if (value != null) return Utils.stringToInt(value);
        }
        return -1;
    }

    public static int getGpuCurFreq() {
        String path = getNodePath(GPU_CUR_EXYNOS5_FREQ);
        if (path.equals("-1")) return -1;
        else {
            String value = Utils.readFile(path);
            if (value != null && Utils.stringToInt(value) != 0) {
                return Utils.stringToInt(value);
            } else if (value != null) {
                return getGpuMinFreq();
            }
        }
        return -1;
    }

    public static long getGpuVoltageNow(){
        if (Utils.existFile(GPU_EXYNOS5_VOLTAGE_NOW)) {
            String value = Utils.readFile(GPU_EXYNOS5_VOLTAGE_NOW);
            return (Utils.stringToLong(value)/1000);
        } else return -1;
    }

    public static int getGpuUsage() {
        if (Utils.existFile(GPU_EXYNOS5_UTILIZATION)) {
            String value = Utils.readFile(GPU_EXYNOS5_UTILIZATION);
            return Utils.stringToInt(value);
        }
        else return -1;
    }

    public static String getNodePath(String paths[]){
        for (int i=0; i<paths.length; i++) {
            if (Utils.existFile(paths[i])) {
                return paths[i];
            }
        }
        return "-1";
    }

}
