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
 * Created by willi on 26.12.14.
 */
public class Screen implements Constants {


    public static void activateGloveMode(boolean active, Context context) {
        Control.runCommand(active ? "glove" : "normal", GLOVE_MODE, Control.CommandType.GENERIC, context);
    }

    public static boolean isGloveModeActive() {
        return Utils.readFile(GLOVE_MODE).equals("glove");
    }

    public static boolean hasGloveMode() {
        return Utils.existFile(GLOVE_MODE);
    }


}
