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

/**
 * Created by willi on 26.12.14.
 */
public class Screen implements Constants {
    public static String[] mdnie_modes = {"Dynamic", "Standard", "Natural", "Cinema", "Adaptative"};

    public static void activateGloveMode(boolean active, Context context) {
        Control.runCommand(active ? "glove_mode,1" : "glove_mode,0", COMMAND_PATH, Control.CommandType.GENERIC, context);
    }

    public static boolean isGloveModeActive() {
        return Utils.readFile(COMMAND_RESULT_PATH).equals("glove_mode,1:OK");
    }

    public static void activatePowerReduce(boolean active, Context context) {
        Control.runCommand(active ? "1" : "0", POWER_REDUCE, Control.CommandType.GENERIC, context);
    }

    public static boolean isPowerReduceActive() {
        return Utils.readFile(POWER_REDUCE).equals("1");
    }

    public static String getmdnieMode() {
        String value = Utils.readFile(MDNIE_MODE);
        for (int i = 0; i < mdnie_modes.length; i++) {
            if (i == Utils.stringToInt(value)) {
                return mdnie_modes[i];
            }
        }
        return "";
    }

    public static void setmdnieMode(String mode, Context context){
        for (int i = 0; i < mdnie_modes.length; i++) {
            if (mode.equals(mdnie_modes[i]))
            {
                Control.runCommand(Integer.toString(i), MDNIE_MODE, Control.CommandType.GENERIC, context);
            }
        }
    }
}
