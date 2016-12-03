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

import com.exynos5420.deathlyadiutor.ads.utils.Constants;
import com.exynos5420.deathlyadiutor.ads.utils.Utils;
import com.exynos5420.deathlyadiutor.ads.utils.root.Control;

/**
 * Created by willi on 27.12.14.
 */
public class VM implements Constants {

    public static void setDirtyRatio(int value, Context context) {
        Control.runCommand(String.valueOf(value), VM_DIRTY_RATIO, Control.CommandType.GENERIC, context);
    }

    public static int getDirtyRatio() {
        return Utils.stringToInt(Utils.readFile(VM_DIRTY_RATIO));
    }

    public static void setDirtyBackgroundRatio(int value, Context context) {
        Control.runCommand(String.valueOf(value), VM_DIRTY_BACKGROUND_RATIO, Control.CommandType.GENERIC, context);
    }

    public static int getDirtyBackgroundRatio() {
        return Utils.stringToInt(Utils.readFile(VM_DIRTY_BACKGROUND_RATIO));
    }

    public static void setDirtyExpire(int value, Context context) {
        Control.runCommand(String.valueOf(value), VM_DIRTY_EXPIRE_CENTISECS, Control.CommandType.GENERIC, context);
    }

    public static int getDirtyExpire() {
        return Utils.stringToInt(Utils.readFile(VM_DIRTY_EXPIRE_CENTISECS));
    }

    public static void setDirtyWriteback(int value, Context context) {
        Control.runCommand(String.valueOf(value), VM_DIRTY_WRITEBACK_CENTISECS, Control.CommandType.GENERIC, context);
    }

    public static int getDirtyWriteback() {
        return Utils.stringToInt(Utils.readFile(VM_DIRTY_WRITEBACK_CENTISECS));
    }

    public static void setOverCommitRatio(int value, Context context) {
        Control.runCommand(String.valueOf(value), VM_OVERCOMMIT_RATIO, Control.CommandType.GENERIC, context);
    }

    public static int getOverCommitRatio() {
        return Utils.stringToInt(Utils.readFile(VM_OVERCOMMIT_RATIO));
    }

    public static void setSwappiness(int value, Context context) {
        Control.runCommand(String.valueOf(value), VM_SWAPPINESS, Control.CommandType.GENERIC, context);
    }

    public static int getSwappiness() {
        return Utils.stringToInt(Utils.readFile(VM_SWAPPINESS));
    }

    public static void setVFSCachePressure(int value, Context context) {
        Control.runCommand(String.valueOf(value), VM_VFS_CACHE_PRESSURE, Control.CommandType.GENERIC, context);
    }

    public static int getVFSCachePressure() {
        return Utils.stringToInt(Utils.readFile(VM_VFS_CACHE_PRESSURE));
    }

    public static void activateLaptopMode(boolean active, Context context) {
        Control.runCommand(active ? "1" : "0", VM_LAPTOP_MODE, Control.CommandType.GENERIC, context);
    }

    public static boolean isLaptopModeActive() {
        return Utils.readFile(VM_LAPTOP_MODE).equals("1");
    }

    public static void setMinFreeKbytes(String value, Context context) {
        Control.runCommand(value, VM_MIN_FREE_KBYTES, Control.CommandType.GENERIC, context);
    }

    public static String getMinFreeKbytes() {
        String value = Utils.readFile(VM_MIN_FREE_KBYTES);
        if (value != null) return value;
        return null;
    }

}
