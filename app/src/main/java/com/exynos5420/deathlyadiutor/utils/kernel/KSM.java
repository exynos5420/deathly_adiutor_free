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

/**
 * Created by willi on 27.12.14.
 */
public class KSM implements Constants {

    public static void setSleepMilliseconds(int ms, Context context) {
        Control.runCommand(String.valueOf(ms), getKsmFile(SLEEP_MILLISECONDS), Control.CommandType.GENERIC, context);
    }

    public static int getSleepMilliseconds() {
        return Utils.stringToInt(Utils.readFile(getKsmFile(SLEEP_MILLISECONDS)));
    }

    public static void setCpuUse(int percent, Context context) {
        Control.runCommand(String.valueOf(percent), getKsmFile(UKSM_CPU_USE), Control.CommandType.GENERIC, context);
    }

    public static int getCpuUse() {
        return Utils.stringToInt(Utils.readFile(getKsmFile(UKSM_CPU_USE)));
    }

    public static void activateDeferredTimer(boolean active, Context context) {
        Control.runCommand(active ? "1" : "0", getKsmFile(DEFERRED_TIMER), Control.CommandType.GENERIC, context);
    }

    public static boolean isDeferredTimerActive() {
        return Utils.readFile(getKsmFile(DEFERRED_TIMER)).equals("1");
    }

    public static void activateKsm(boolean active, Context context) {
        Control.runCommand(active ? "1" : "0", getKsmFile(RUN), Control.CommandType.GENERIC, context);
    }

    public static boolean isKsmActive() {
        return Utils.readFile(getKsmFile(RUN)).equals("1");
    }

    public static String getInfo(int position) {
        return Utils.readFile(getKsmFile(KSM_INFOS[position]));
    }

    public static boolean hasInfo(int position) {
        return Utils.existFile(getKsmFile(KSM_INFOS[position]));
    }

    public static int getInfoLength() {
        return KSM_INFOS.length;
    }

    private static String getKsmFile(String file) {
        return UKSM_FOLDER + "/" + file;
    }

}
