/*
 * Copyright (C) 2016 Martin Bouchet
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

import com.exynos5420.deathlyadiutor.ads.utils.Constants;
import com.exynos5420.deathlyadiutor.ads.utils.Utils;
import com.exynos5420.deathlyadiutor.ads.utils.root.Control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Martin Bouchet on 15.11.16.
 */
public class CPUThermal implements Constants {

    public static List<Integer> getFreqs() {
        Integer[] ThermalFreqs;
        String value = Utils.readFile(CPU_THERMAL_ONESHOT_FREQS);
        String[] freqs = value.split(" ");
        ThermalFreqs = new Integer[freqs.length];
        for (int i = 0; i < ThermalFreqs.length; i++) {
            ThermalFreqs[i] = Utils.stringToInt(freqs[i]) / 1000;
        }
        return new ArrayList<>(Arrays.asList(ThermalFreqs));
    }

    public static List<Integer> getTemps() {
        Integer[] ThermalTemps;
        String value = Utils.readFile(CPU_THERMAL_ONESHOT_TEMPS);
        String[] temps = value.split(" ");
        ThermalTemps = new Integer[temps.length];
        for (int i = 0; i < ThermalTemps.length; i++) {
            ThermalTemps[i] = Utils.stringToInt(temps[i]) / 1000;
        }

        return new ArrayList<>(Arrays.asList(ThermalTemps));
    }

    public static void setCPUTripPointTemp(int temp, int step, Context context) {
        String command = "";
        for (int i=0; i < getTemps().size(); i++){
            if (i == step) command += temp + " ";
            else command += getTemps().get(i) + " ";
        }
        Control.runCommand(command, CPU_THERMAL_ONESHOT_TEMPS, Control.CommandType.GENERIC, context);
    }

    public static void setCPUTripPointFreq(int freq, int step, Context context) {
        String command = "";
        for (int i=0; i < getFreqs().size(); i++){
            if (i == step) command += freq + " ";
            else command += getFreqs().get(i) + " ";
        }
        Control.runCommand(command, CPU_THERMAL_ONESHOT_FREQS, Control.CommandType.GENERIC, context);
    }

    public static boolean hasCpuThermalControl (){
        if (Utils.existFile(CPU_THERMAL_ONESHOT_FREQS) && Utils.existFile(CPU_THERMAL_ONESHOT_TEMPS)) return true;
        else return false;
    }

}
