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
import android.util.Log;

import com.exynos5420.deathlyadiutor.R;
import com.exynos5420.deathlyadiutor.utils.Constants;
import com.exynos5420.deathlyadiutor.utils.Utils;
import com.exynos5420.deathlyadiutor.utils.root.Control;
import com.kerneladiutor.library.root.RootUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by willi on 02.12.14.
 */
public class CPU implements Constants {

    private static int cores;
    private static Integer[] mCpuFreqs;

    private static String[] mMcPowerSavingItems;

    public static String[] getMcPowerSavingItems(Context context) {
        if (mMcPowerSavingItems == null && context != null)
            mMcPowerSavingItems = context.getResources().getStringArray(R.array.mc_power_saving_items);
        return mMcPowerSavingItems;
    }

    public static int getCurMcPowerSaving() {
        return Utils.stringToInt(Utils.readFile(CPU_MC_POWER_SAVING));
    }

    public static void activatePowerSavingWq(boolean active, Context context) {
        Control.runCommand(active ? "Y" : "N", CPU_WQ_POWER_SAVING, Control.CommandType.GENERIC, context);
    }

    public static boolean isPowerSavingWqActive() {
        return Utils.readFile(CPU_WQ_POWER_SAVING).equals("Y");
    }

    public static List<String> getAvailableGovernors() {
        String[] mAvailableGovernors;
        String value = Utils.readFile(CPU_AVAILABLE_GOVERNORS);
        mAvailableGovernors = value.split(" ");
        Collections.sort(Arrays.asList(mAvailableGovernors), String.CASE_INSENSITIVE_ORDER);
        return new ArrayList<>(Arrays.asList(mAvailableGovernors));
    }

    public static void setGovernor(String governor, Context context) {
        Control.runCommand(governor, CPU_SCALING_GOVERNOR, Control.CommandType.CPU, context);
    }

    public static String getCurGovernor() {
        return Utils.readFile(CPU_SCALING_GOVERNOR);
    }

    public static List<Integer> getFreqs() {
        if (mCpuFreqs == null) {
            String value = Utils.readFile(CPU_AVAILABLE_FREQS);
            if (value != null) {
                String[] freqs = value.split(" ");
                mCpuFreqs = new Integer[freqs.length];
                for (int i = 0; i < mCpuFreqs.length; i++) {
                    mCpuFreqs[i] = Utils.stringToInt(freqs[i]);
                }
            }
        }
        ArrayList<Integer> freqslist = new ArrayList<>(Arrays.asList(mCpuFreqs));
        Collections.sort(freqslist);
        return freqslist;
    }

    public static void setMinFreq(int freq, Context context) {
        Control.runCommand(String.valueOf(freq), CPU_MIN_FREQ, Control.CommandType.CPU, context);
    }

    public static int getMinFreq() {
            return Utils.stringToInt(Utils.readFile(CPU_MIN_FREQ));
    }

    public static void setMaxFreq(int freq, Context context) {
        Control.runCommand(String.valueOf(freq), CPU_MAX_FREQ, Control.CommandType.CPU, context);
    }

    public static int getMaxFreq() {
        return Utils.stringToInt(Utils.readFile(CPU_MAX_FREQ));
    }

    public static int getCurFreq(int core) {
        if (Utils.existFile(String.format(CPU_CUR_FREQ, core))) {
            String value = Utils.readFile(String.format(CPU_CUR_FREQ, core));
            if (value != null) return Utils.stringToInt(value);
        }
        return 0;
    }

    public static void activateCore(int core, boolean active, Context context) {
        if (context != null)
            Control.runCommand(active ? "1" : "0", String.format(CPU_CORE_ONLINE, core), Control.CommandType.GENERIC, context);
        else
            RootUtils.runCommand(String.format("echo %s > " + String.format(CPU_CORE_ONLINE, core), active ? "1" : "0"));
    }

    public static List<Integer> getCoreRange() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < getCoreCount(); i++) list.add(i);
        return list;
    }

    public static int getCoreCount() {
        return cores == 0 ? cores = Runtime.getRuntime().availableProcessors() : cores;
    }

    public static String getTemp() {
        double temp = Utils.stringToLong(Utils.readFile(CPU_TEMP_ZONE0));
        if (temp > 1000) temp /= 1000;
        else if (temp > 200) temp /= 10;
        return Utils.formatCelsius(temp) + " " + Utils.celsiusToFahrenheit(temp);
    }

    public static float[] getCpuUsage() {
        try {
            Usage[] usage1 = getUsages();
            Thread.sleep(1000);
            Usage[] usage2 = getUsages();

            if (usage1 != null && usage2 != null) {
                float[] pers = new float[usage1.length];
                for (int i = 0; i < usage1.length; i++) {
                    long idle1 = usage1[i].getIdle();
                    long up1 = usage1[i].getUptime();

                    long idle2 = usage2[i].getIdle();
                    long up2 = usage2[i].getUptime();

                    float cpu = -1f;
                    if (idle1 >= 0 && up1 >= 0 && idle2 >= 0 && up2 >= 0) {
                        if ((up2 + idle2) > (up1 + idle1) && up2 >= up1) {
                            cpu = (up2 - up1) / (float) ((up2 + idle2) - (up1 + idle1));
                            cpu *= 100.0f;
                        }
                    }

                    pers[i] = cpu > -1 ? cpu : 0;
                }
                return pers;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Usage[] getUsages() {
        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
            Usage[] usage = new Usage[getCoreCount() + 1];
            for (int i = 0; i < usage.length; i++)
                usage[i] = new Usage(reader.readLine());
            reader.close();
            return usage;
        } catch (FileNotFoundException e) {
            Log.i(TAG, "/proc/stat does not exist");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class Usage {

        private long[] stats;

        public Usage(String stats) {
            if (stats == null) return;

            String[] values = stats.replace("  ", " ").split(" ");
            this.stats = new long[values.length - 1];
            for (int i = 0; i < this.stats.length; i++)
                this.stats[i] = Utils.stringToLong(values[i + 1]);
        }

        public long getUptime() {
            if (stats == null) return -1L;
            long l = 0L;
            for (int i = 0; i < stats.length; i++)
                if (i != 3) l += stats[i];
            return l;
        }

        public long getIdle() {
            try {
                return stats == null ? -1L : stats[3];
            } catch (ArrayIndexOutOfBoundsException e) {
                return -1L;
            }
        }
    }


    public static String getTSPBoosterPath(){
        for (int i = 0; i < TOUCHSCREEN_BOOSTER_ENABLED.length; i++)
            if (Utils.existFile(TOUCHSCREEN_BOOSTER_ENABLED[i])) return TOUCHSCREEN_BOOSTER_ENABLED[i];

        return "";
    }

    public static String getTKBoosterPath(){
        for (int i = 0; i < TOUCHKEY_BOOSTER_ENABLED.length; i++)
            if (Utils.existFile(TOUCHKEY_BOOSTER_ENABLED[i])) return TOUCHKEY_BOOSTER_ENABLED[i];

        return "";
    }

    public static void activateTSPBooster(boolean active, Context context) {
        Control.runCommand(active ? "1" : "0", getTSPBoosterPath(), Control.CommandType.GENERIC, context);
    }

    public static boolean isTSPBoosterActive() {
        return Utils.readFile(getTSPBoosterPath()).equals("1");
    }

    public static void activateTKBooster(boolean active, Context context) {
        Control.runCommand(active ? "1" : "0", getTKBoosterPath(), Control.CommandType.GENERIC, context);
    }

    public static boolean isTKBoosterActive() {
        return Utils.readFile(getTKBoosterPath()).equals("1");
    }

    public static void activateWacomBooster(boolean active, Context context) {
        Control.runCommand(active ? "1" : "0", WACOM_BOOSTER_ENABLED, Control.CommandType.GENERIC, context);
    }

    public static boolean isWacomBoosterActive() {
        return Utils.readFile(WACOM_BOOSTER_ENABLED).equals("1");
    }

}
