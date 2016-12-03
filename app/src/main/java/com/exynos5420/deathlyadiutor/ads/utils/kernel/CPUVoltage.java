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

package com.exynos5420.deathlyadiutor.ads.utils.kernel;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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
public class CPUVoltage implements Constants {

    private static String CPU_VOLTAGE_FILE;
    private static String[] mCpuFreqs;

    public static void setGlobalOffset(String voltage, Context context) {
        int adjust = Utils.stringToInt(voltage);
        String command = "";
        for (String volt : getVoltages())
            if (volt != null)
                command += command.isEmpty() ? (Utils.stringToInt(volt) + adjust) :
                        " " + (Utils.stringToInt(volt) + adjust);

        Control.runCommand(command, CPU_VOLTAGE_FILE, Control.CommandType.GENERIC, context);
    }

    public static void setVoltage(String freq, String voltage, Context context) {
        String command = "";
        int position = 0;

        for (int i = 0; i < getFreqs().size(); i++) {
            if (freq.equals(getFreqs().get(i)))
                position = i;
        }

        List<String> voltages = getVoltages();
        for (int i = 0; i < voltages.size(); i++)
            if (i == position)
                command += command.isEmpty() ? voltage : " " + voltage;
            else
                command += command.isEmpty() ? voltages.get(i) : " " + voltages.get(i);

        Control.runCommand(command, CPU_VOLTAGE_FILE, Control.CommandType.GENERIC, context);
    }

    public static List<String> getVoltages() {
        String value = Utils.readFile(CPU_VOLTAGE_FILE);
        if (value != null) {
            String[] lines;
            value = value.replace(" ", "");
            lines = value.split("mV");

            String[] voltages = new String[lines.length];
            for (int i = 0; i < voltages.length; i++) {
                String[] voltageLine;
                voltageLine = lines[i].split("mhz:");
                if (voltageLine.length > 1) {
                    voltages[i] = voltageLine[1].trim();
                }
            }
            return new ArrayList<>(Arrays.asList(voltages));
        }
        return Collections.emptyList();
    }

    public static List<String> getFreqs() {

        if (mCpuFreqs == null) {

            String value = Utils.readFile(CPU_VOLTAGE_FILE);

            if (value != null) {
                String[] lines;
                value = value.replace(" ", "");
                lines = value.split("mV");

                mCpuFreqs = new String[lines.length];
                for (int i = 0; i < lines.length; i++) {
                    mCpuFreqs[i] = lines[i].split("mhz:")[0].trim();

                }
            }
            return new ArrayList<>(Arrays.asList(mCpuFreqs));
        } else {
            return new ArrayList<>(Arrays.asList(mCpuFreqs));
        }
    }

    public static boolean hasCpuVoltage() {
        for (String file : CPU_VOLTAGE_ARRAY)
            if (Utils.existFile(file)) {
                CPU_VOLTAGE_FILE = file;
                return true;
            }
        return false;
    }

    public static boolean storeVoltageTable(Context context) {
        // Have to call this function to pre-load variables
        if (CPUVoltage.hasCpuVoltage() && !CPUVoltage.getFreqs().isEmpty() && !CPUVoltage.getVoltages().isEmpty()) {
            List<String> freqs = CPUVoltage.getFreqs();
            List<String> voltages = CPUVoltage.getVoltages();

            // Store Kernel's Stock Freq/Voltage table
            SharedPreferences.Editor preferences = context.getSharedPreferences("voltage_table", 0).edit();
            for (int i = 0; i < freqs.size(); i++) {
                preferences.putString(freqs.get(i), voltages.get(i));
            }
            preferences.apply();
            return true;
        } else {
            Log.w(TAG, "hasCpuVoltage() is false");
            return false;
        }
    }
}
