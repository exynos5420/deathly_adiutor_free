package com.exynos5420.deathlyadiutor.utils.kernel;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.exynos5420.deathlyadiutor.utils.Constants;
import com.exynos5420.deathlyadiutor.utils.Utils;
import com.exynos5420.deathlyadiutor.utils.root.Control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by tinch on 9/11/2016.
 */

public class GPUVoltage implements Constants {
    private static String GPU_VOLTAGE_FILE = GPU_VOLTAGE_EXYNOS5_FILE;
    private static String[] mGpuFreqs;

    public static void setGlobalOffset(String voltage, Context context) {
        double adjust = Utils.stringtodouble(voltage) * 1000;
        String final_voltage;
        String command;
        for (int i=0; i < getVoltages().size(); i++){
                final_voltage = Double.toString(Integer.parseInt(getVoltages().get(i)) + adjust);
                final_voltage = final_voltage.substring(0, final_voltage.length() - 2);
                command = getFreqs().get(i) + " " + final_voltage;
                Control.runCommand(command, GPU_VOLTAGE_FILE, Control.CommandType.GENERIC, Integer.toString(i), context);
            }

    }

    public static void setVoltage(String freq, String voltage, Context context) {
        String command;
        voltage = voltage.substring(0, voltage.length() - 2);
        for (int i = 0; i < getVoltages().size(); i++)
            if (getFreqs().get(i).equals(freq)) {
                command = freq + " " + voltage;
                Control.runCommand(command, GPU_VOLTAGE_FILE, Control.CommandType.GENERIC, Integer.toString(i), context);
                return;
            }

    }

    public static List<String> getVoltages() {
        String value = Utils.readFile(GPU_VOLTAGE_FILE);
        if (value != null) {
            String[] lines;
            lines = value.split("\n");

            String[] voltages = new String[lines.length];
            for (int i = 0; i < voltages.length; i++) {
                String[] voltageLine;
                voltageLine = lines[i].split(" ");
                if (voltageLine.length > 1) {
                    voltages[i] = voltageLine[1].trim();
                }
            }
            return new ArrayList<>(Arrays.asList(voltages));
        }
        return Collections.emptyList();
    }

    public static List<String> getFreqs() {

        if (mGpuFreqs == null) {

            String value = Utils.readFile(GPU_VOLTAGE_FILE);

            if (value != null) {
                String[] lines;
                lines = value.split("\n");

                mGpuFreqs = new String[lines.length];
                for (int i = 0; i < lines.length; i++) {
                    mGpuFreqs[i] = lines[i].split(" ")[0].trim();

                }
            }
            return new ArrayList<>(Arrays.asList(mGpuFreqs));
        } else {
            return new ArrayList<>(Arrays.asList(mGpuFreqs));
        }
    }

    public static boolean storeVoltageTable (Context context) {
        // Have to call this function to pre-load variables
        if(!GPUVoltage.getFreqs().isEmpty() && !GPUVoltage.getVoltages().isEmpty()){
            List<String> freqs = GPUVoltage.getFreqs();
            List<String> voltages = GPUVoltage.getVoltages();

            // Store Kernel's Stock Freq/Voltage table
            SharedPreferences.Editor preferences = context.getSharedPreferences("gpu_voltage_table", 0).edit();
            for (int i = 0; i < freqs.size(); i++) {
                preferences.putString(freqs.get(i), voltages.get(i));
            }
            preferences.apply();
            return true;
        } else {
            return false;
        }
    }
}
