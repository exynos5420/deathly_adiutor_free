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

package com.exynos5420.deathlyadiutor.utils.kernel;

import android.content.Context;

import com.exynos5420.deathlyadiutor.utils.Constants;
import com.exynos5420.deathlyadiutor.utils.Utils;
import com.exynos5420.deathlyadiutor.utils.root.Control;
import com.exynos5420.deathlyadiutor.utils.tools.Buildprop;

import java.util.LinkedHashMap;

/**
 * Created by Martin Bouchet on 25.11.16.
 */
public class Battery implements Constants {
    
    private static String devicemodel;

    public static int getChargeLevel(){
        return Utils.stringToInt(Utils.readFile(BATT_CHARGERATE));
    }

    public static String getVoltage(){
        return Utils.readFile(BATT_VOLTAGE);
    }

    public static String getDevice(){
        if (devicemodel == null) {
            LinkedHashMap<String, String> buildpropItem;
            buildpropItem = Buildprop.getProps();
            Object[] keys = buildpropItem.keySet().toArray();
            Object[] values = buildpropItem.values().toArray();
            for (int i = 0; i < keys.length; i++) {
                if (((String) keys[i]).contains("ro.product.name"))
                    devicemodel = values[i].toString();
            }
        }
        return devicemodel;
    }
    public static int getBatteryCapacity(){
        if (devicemodel == null) getDevice();
        if (devicemodel.contains("ha"))
            return 3200;
        else if (devicemodel.contains("klimt"))
            return 4900;
        else if (devicemodel.contains("chagall"))
            return 7900;
        else if (devicemodel.contains("n1a") || devicemodel.contains("lt03"))
            return 8220;
        else if (devicemodel.contains("v1a") || devicemodel.contains("v2a"))
            return 9500;

        return 0;
    }

    public static String getTemperature(){
        return Utils.readFile(BATT_TEMP);
    }

    public static String getChargingSource(){
        return Utils.readFile(BATT_CHARGING_SOURCE);
    }

    public static String getCurrent(String desired_current){
        switch (desired_current){
            case "now":
                return Utils.readFile(BATT_CURRENT_NOW);
            case "avg":
                if (!Utils.readFile(SIOP_LEVEL).equals("100") && Utils.readFile(BATT_CHARGING_SOURCE).equals("3")){
                    return Utils.readFile(SIOP_CHRG_CURR);
                }
                else return Utils.readFile(BATT_CURRENT_AVG);
            case "max":
                return Utils.readFile(BATT_CURRENT_MAX);
        }
        return "";
    }

    public static void activateUnstablePowerDetection(boolean active, Context context) {
        Control.runCommand(active ? "1" : "0", UNSTABLE_POWER_DETECTION, Control.CommandType.GENERIC, context);
    }

    public static boolean isUnstablePowerDetectionActive() {
        return Utils.readFile(UNSTABLE_POWER_DETECTION).equals("1");
    }

    public static String getACmainsInputcurr(){
        double value = Utils.stringtodouble(Utils.readFile(AC_INPUT_CURR));
        return Double.toString((value - 450) /25);
    }

    public static void setACmainsInputcurr(int curr, Context context){
        curr = (curr*25) + 450;
        Control.runCommand(Integer.toString(curr), AC_INPUT_CURR, Control.CommandType.GENERIC, context);
    }

    public static String getACmainschrgcurr(){
        double value = Utils.stringtodouble(Utils.readFile(AC_CHRG_CURR));
        return Double.toString((value - 450) /25);
    }

    public static void setACmainschrgcurr(int curr, Context context){
        curr = (curr*25) + 450;
        Control.runCommand(Integer.toString(curr), AC_CHRG_CURR, Control.CommandType.GENERIC, context);
    }

    public static void activateSIOP(boolean active, Context context) {
        Control.runCommand(active ? "99" : "100", SIOP_LEVEL, Control.CommandType.GENERIC, context);
    }

    public static boolean isSIOPActive() {
        return !Utils.readFile(SIOP_LEVEL).equals("100");
    }

    public static String getSIOPInputcurr(){
        double value = Utils.stringtodouble(Utils.readFile(SIOP_INPUT_CURR));
        return Double.toString((value - 450) /50);
    }

    public static void setSIOPInputcurr(int curr, Context context){
        curr = (curr*50) + 450;
        Control.runCommand(Integer.toString(curr), SIOP_INPUT_CURR, Control.CommandType.GENERIC, context);
    }

    public static String getSIOPchrgcurr(){
        double value = Utils.stringtodouble(Utils.readFile(SIOP_CHRG_CURR));
        return Double.toString((value - 450) /50);
    }

    public static void setSIOPchrgcurr(int curr, Context context){
        curr = (curr*50) + 450;
        Control.runCommand(Integer.toString(curr), SIOP_CHRG_CURR, Control.CommandType.GENERIC, context);
    }

    public static String getSDPInputcurr(){
        double value = Utils.stringtodouble(Utils.readFile(SDP_INPUT_CURR));
        return Double.toString((value - 450) /25);
    }

    public static void setSDPInputcurr(int curr, Context context){
        curr = (curr*25) + 450;
        Control.runCommand(Integer.toString(curr), SDP_INPUT_CURR, Control.CommandType.GENERIC, context);
    }

    public static String getSDPchrgcurr(){
        double value = Utils.stringtodouble(Utils.readFile(SDP_CHRG_CURR));
        return Double.toString((value - 450) /25);
    }

    public static void setSDPchrgcurr(int curr, Context context){
        curr = (curr*25) + 450;
        Control.runCommand(Integer.toString(curr), SDP_CHRG_CURR, Control.CommandType.GENERIC, context);
    }

}
