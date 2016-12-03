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

package com.exynos5420.deathlyadiutor.ads.utils;

import com.exynos5420.deathlyadiutor.ads.BuildConfig;
import com.exynos5420.deathlyadiutor.ads.elements.DAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willi on 30.11.14.
 */
public interface Constants {

    String TAG = "Deathly Adiutor";
    String VERSION_NAME = BuildConfig.VERSION_NAME;
    int VERSION_CODE = BuildConfig.VERSION_CODE;
    String PREF_NAME = "prefs";
    List<DAdapter.DView> ITEMS = new ArrayList<>();
    List<DAdapter.DView> VISIBLE_ITEMS = new ArrayList<>();

    // Kernel Informations
    String PROC_VERSION = "/proc/version";
    String PROC_CPUINFO = "/proc/cpuinfo";
    String PROC_MEMINFO = "/proc/meminfo";

    // CPU
    String CPU_CUR_FREQ = "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_cur_freq";
    String CPU_TEMP_ZONE0 = "/sys/class/thermal/thermal_zone0/temp";
    String CPU_CORE_ONLINE = "/sys/devices/system/cpu/cpu%d/online";
    String CPU_MAX_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq";
    String CPU_MIN_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq";
    String CPU_AVAILABLE_FREQS = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies";
    String[] CPU_TIME_IN_STATE_ARRAY = {"/sys/devices/system/cpu/cpufreq/stats/cpu%d/time_in_state",
            "/sys/devices/system/cpu/cpu%d/cpufreq/stats/time_in_state"};
    String CPU_SCALING_GOVERNOR = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor";
    String CPU_AVAILABLE_GOVERNORS = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors";

    String CPU_GOVERNOR_TUNABLES = "/sys/devices/system/cpu/cpufreq";

    String CPU_MC_POWER_SAVING = "/sys/devices/system/cpu/sched_mc_power_savings";
    String CPU_WQ_POWER_SAVING = "/sys/module/workqueue/parameters/power_efficient";


    String[] TOUCHSCREEN_BOOSTER_ENABLED = {"/sys/module/synaptics_i2c_rmi/parameters/tsp_booster_enabled",
            "/sys/module/mxts/parameters/tsp_booster_enabled",
            "/sys/module/mxts_n1/parameters/tsp_booster_enabled",
            "/sys/module/mxtt/parameters/tsp_booster_enabled"};
    String[] TOUCHKEY_BOOSTER_ENABLED = {"/sys/module/cypress_touchkey/parameters/touchkey_booster_enabled",
            "/sys/module/cypress_touchkey_h/parameters/touchkey_booster_enabled",
            "/sys/module/mxts/parameters/touchkey_booster_enabled",
            "/sys/module/mxts_n1/parameters/touchkey_booster_enabled",
            "/sys/module/mxtt/parameters/touchkey_booster_enabled"};
    String WACOM_BOOSTER_ENABLED = "/sys/module/wacom_i2c_func/parameters/wacom_booster_enabled";

    String[][] CPU_ARRAY = {{CPU_CUR_FREQ, CPU_TEMP_ZONE0, CPU_CORE_ONLINE, CPU_MAX_FREQ, CPU_MIN_FREQ, CPU_AVAILABLE_FREQS, CPU_SCALING_GOVERNOR,
            CPU_AVAILABLE_GOVERNORS, CPU_GOVERNOR_TUNABLES, CPU_MC_POWER_SAVING, CPU_WQ_POWER_SAVING, WACOM_BOOSTER_ENABLED},
            TOUCHSCREEN_BOOSTER_ENABLED, TOUCHKEY_BOOSTER_ENABLED};

    // CPU Voltage
    String CPU_VOLTAGE = "/sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table";

    String[] CPU_VOLTAGE_ARRAY = {CPU_VOLTAGE};

    //CPU CPUThermal

    String CPU_THERMAL_ONESHOT_TEMPS = "/sys/class/thermal/thermal_zone0/oneshot_trip_temp";
    String CPU_THERMAL_ONESHOT_FREQS = "/sys/class/thermal/thermal_zone0/oneshot_trip_freq";

    String[] CPU_THERMAL_ARRAY = {CPU_THERMAL_ONESHOT_TEMPS, CPU_THERMAL_ONESHOT_FREQS};

    // GPU

    String GPU_EXYNOS5_DVFS = "/sys/devices/platform/mali.0/dvfs";
    String GPU_EXYNOS5_UTILIZATION = "/sys/devices/platform/mali.0/utilization";
    String GPU_CUR_EXYNOS5_FREQ = "/sys/devices/platform/mali.0/clock";
    String GPU_MAX_EXYNOS5_FREQ = "/sys/devices/platform/mali.0/max_clock";
    String GPU_MIN_EXYNOS5_FREQ = "/sys/devices/platform/mali.0/min_clock";
    String GPU_AVALIBLE_EXYNOS5_FREQS = "/sys/devices/platform/mali.0/freq_table";
    String GPU_AVALIBLE_EXYNOS5_GOVS = "/sys/devices/platform/mali.0/dvfs_governor";
    String GPU_AVALIBLE_EXYNOS5_POWERP = "/sys/devices/platform/mali.0/power_policy";
    String GPU_TIME_IN_STATE = "/sys/devices/platform/mali.0/time_in_state";

    String[] GPU_ARRAY = {GPU_EXYNOS5_DVFS, GPU_EXYNOS5_UTILIZATION, GPU_CUR_EXYNOS5_FREQ, GPU_MAX_EXYNOS5_FREQ,
            GPU_MIN_EXYNOS5_FREQ, GPU_AVALIBLE_EXYNOS5_FREQS, GPU_AVALIBLE_EXYNOS5_GOVS, GPU_AVALIBLE_EXYNOS5_POWERP,
            GPU_TIME_IN_STATE};

    //GPU Voltage

    String GPU_VOLTAGE_EXYNOS5_FILE = "/sys/devices/platform/mali.0/volt_table";

    String[] GPU_VOLTAGE_ARRAY = {GPU_VOLTAGE_EXYNOS5_FILE};

    //GPU Thermal
    String GPU_THERMAL_THROTTLING5_FREQ = "/sys/devices/platform/mali.0/tripping";
    String GPU_THERMAL_THROTTLING4_FREQ = "/sys/devices/platform/mali.0/throttling4";
    String GPU_THERMAL_THROTTLING3_FREQ = "/sys/devices/platform/mali.0/throttling3";
    String GPU_THERMAL_THROTTLING2_FREQ = "/sys/devices/platform/mali.0/throttling2";
    String GPU_THERMAL_THROTTLING1_FREQ = "/sys/devices/platform/mali.0/throttling1";

    String GPU_THERMAL_THROTTLING5_TEMP = "/sys/module/exynos_thermal/parameters/tmu_gpu_temp5";
    String GPU_THERMAL_THROTTLING4_TEMP = "/sys/module/exynos_thermal/parameters/tmu_gpu_temp4";
    String GPU_THERMAL_THROTTLING3_TEMP = "/sys/module/exynos_thermal/parameters/tmu_gpu_temp3";
    String GPU_THERMAL_THROTTLING2_TEMP = "/sys/module/exynos_thermal/parameters/tmu_gpu_temp2";
    String GPU_THERMAL_THROTTLING1_TEMP = "/sys/module/exynos_thermal/parameters/tmu_gpu_temp1";

    String[] GPU_THRORRLING_FREQS_ARRAY = {GPU_THERMAL_THROTTLING1_FREQ, GPU_THERMAL_THROTTLING2_FREQ, GPU_THERMAL_THROTTLING3_FREQ,
                                            GPU_THERMAL_THROTTLING4_FREQ, GPU_THERMAL_THROTTLING5_FREQ};

    String[] GPU_THRORRLING_TEMPS_ARRAY =  {GPU_THERMAL_THROTTLING1_TEMP, GPU_THERMAL_THROTTLING2_TEMP, GPU_THERMAL_THROTTLING3_TEMP,
                                            GPU_THERMAL_THROTTLING4_TEMP, GPU_THERMAL_THROTTLING5_TEMP};

    String[][] GPU_THERMAL_ARRAY = {GPU_THRORRLING_FREQS_ARRAY, GPU_THRORRLING_TEMPS_ARRAY};

    // Screen
    String COMMAND_PATH = "/sys/class/sec/tsp/cmd";
    String COMMAND_RESULT_PATH = "/sys/class/sec/tsp/cmd_result";
    String POWER_REDUCE = "/sys/class/lcd/panel/power_reduce";
    String EPEN_SAVING_MODE = "/sys/devices/virtual/sec/sec_epen/epen_saving_mode";
    String MDNIE_MODE = "/sys/class/mdnie/mdnie/mode";
    String TOUCHKEY_GLOVE_MODE = "/sys/devices/virtual/sec/sec_touchkey/glove_mode";

    String[] SCREEN_ARRAY = {COMMAND_PATH, COMMAND_RESULT_PATH, POWER_REDUCE, EPEN_SAVING_MODE, MDNIE_MODE, TOUCHKEY_GLOVE_MODE};

    // Speaker
    String EARPICE_VOLUME = "/sys/class/misc/wolfson_control/earpiece_volume";
    String SP_GAIN_LEFT = "/sys/class/misc/wolfson_control/speaker_gain_left";
    String SP_GAIN_RIGHT = "/sys/class/misc/wolfson_control/speaker_gain_right";
    String SP_PRIVACY_MODE = "/sys/class/misc/wolfson_control/switch_sp_privacy";
    String SP_EQ_ENABLE = "/sys/class/misc/wolfson_control/switch_eq_speaker";
    String SP_EQ_BAND1_GAIN = "/sys/class/misc/wolfson_control/eq_sp_gain_1";
    String SP_EQ_BAND2_GAIN = "/sys/class/misc/wolfson_control/eq_sp_gain_2";
    String SP_EQ_BAND3_GAIN = "/sys/class/misc/wolfson_control/eq_sp_gain_3";
    String SP_EQ_BAND4_GAIN = "/sys/class/misc/wolfson_control/eq_sp_gain_4";
    String SP_EQ_BAND5_GAIN = "/sys/class/misc/wolfson_control/eq_sp_gain_5";

    String[] SPEAKER_EQ_GAINS_ARRAY = {SP_EQ_BAND1_GAIN, SP_EQ_BAND2_GAIN, SP_EQ_BAND3_GAIN, SP_EQ_BAND4_GAIN, SP_EQ_BAND5_GAIN};

    String[] SPEAKER_ARRAY = {EARPICE_VOLUME, SP_GAIN_LEFT, SP_GAIN_RIGHT, SP_PRIVACY_MODE, SP_EQ_ENABLE, SP_EQ_BAND1_GAIN, SP_EQ_BAND2_GAIN,
            SP_EQ_BAND3_GAIN, SP_EQ_BAND4_GAIN, SP_EQ_BAND5_GAIN};

    // HeadPhone
    String HP_GAIN_LEFT = "/sys/class/misc/wolfson_control/headphone_left";
    String HP_GAIN_RIGHT = "/sys/class/misc/wolfson_control/headphone_right";
    String HP_SWITCH_MONO = "/sys/class/misc/wolfson_control/switch_hp_mono";
    String HP_EQ_ENABLE = "/sys/class/misc/wolfson_control/switch_eq_headphone";
    String HP_EQ_BAND1_GAIN = "/sys/class/misc/wolfson_control/eq_hp_gain_1";
    String HP_EQ_BAND2_GAIN = "/sys/class/misc/wolfson_control/eq_hp_gain_2";
    String HP_EQ_BAND3_GAIN = "/sys/class/misc/wolfson_control/eq_hp_gain_3";
    String HP_EQ_BAND4_GAIN = "/sys/class/misc/wolfson_control/eq_hp_gain_4";
    String HP_EQ_BAND5_GAIN = "/sys/class/misc/wolfson_control/eq_hp_gain_5";

    String[] HEADPHONE_EQ_GAINS_ARRAY = {HP_EQ_BAND1_GAIN, HP_EQ_BAND2_GAIN, HP_EQ_BAND3_GAIN, HP_EQ_BAND4_GAIN, HP_EQ_BAND5_GAIN};

    String[] HEADPHONE_ARRAY = {HP_GAIN_LEFT, HP_GAIN_RIGHT, HP_SWITCH_MONO, HP_EQ_ENABLE, SP_EQ_BAND1_GAIN, HP_EQ_BAND1_GAIN,
            HP_EQ_BAND2_GAIN, HP_EQ_BAND3_GAIN, HP_EQ_BAND4_GAIN, HP_EQ_BAND5_GAIN};

    // Battery
    String BATT_CHARGERATE = "/sys/devices/platform/sec-battery/power_supply/battery/capacity";
    String BATT_CURRENT_NOW = "/sys/devices/platform/sec-battery/power_supply/battery/current_now";
    String BATT_CURRENT_AVG = "/sys/devices/platform/sec-battery/power_supply/battery/current_avg";
    String BATT_CURRENT_MAX = "/sys/devices/platform/sec-battery/power_supply/battery/current_max";
    String BATT_CHARGING_SOURCE = "/sys/devices/platform/sec-battery/power_supply/battery/batt_charging_source";
    String BATT_TEMP = "/sys/devices/platform/sec-battery/power_supply/battery/temp";
    String BATT_VOLTAGE = "/sys/devices/platform/sec-battery/power_supply/battery/voltage_now";

    String UNSTABLE_POWER_DETECTION = "/sys/devices/platform/sec-battery/unstable_power_detection";
    String SIOP_LEVEL = "/sys/devices/platform/sec-battery/power_supply/battery/siop_level";

    String AC_INPUT_CURR = "/sys/devices/platform/sec-battery/ac_input_curr";
    String AC_CHRG_CURR = "/sys/devices/platform/sec-battery/ac_chrg_curr";
    String SIOP_INPUT_CURR = "/sys/devices/platform/sec-battery/siop_input_limit";
    String SIOP_CHRG_CURR = "/sys/devices/platform/sec-battery/siop_charge_limit";
    String SDP_INPUT_CURR = "/sys/devices/platform/sec-battery/sdp_input_curr";
    String SDP_CHRG_CURR = "/sys/devices/platform/sec-battery/sdp_chrg_curr";
    String DCP_INPUT_CURR = "/sys/devices/platform/sec-battery/dcp_input_curr";
    String DCP_CHRG_CURR = "/sys/devices/platform/sec-battery/dcp_chrg_curr";
    String CDP_INPUT_CURR = "/sys/devices/platform/sec-battery/cdp_input_curr";
    String CDP_CHRG_CURR = "/sys/devices/platform/sec-battery/cdp_chrg_curr";
    String ACA_INPUT_CURR = "/sys/devices/platform/sec-battery/aca_input_curr";
    String ACA_CHRG_CURR = "/sys/devices/platform/sec-battery/aca_chrg_curr";
    String MISC_INPUT_CURR = "/sys/devices/platform/sec-battery/misc_input_curr";
    String MISC_CHRG_CURR = "/sys/devices/platform/sec-battery/misc_chrg_curr";

    String[] BATTERY_ARRAY = {BATT_CHARGERATE, BATT_CURRENT_NOW, BATT_CURRENT_AVG, BATT_CURRENT_MAX,
            BATT_CHARGING_SOURCE, BATT_TEMP, BATT_VOLTAGE, UNSTABLE_POWER_DETECTION, SIOP_LEVEL, AC_INPUT_CURR, AC_CHRG_CURR,
            SIOP_INPUT_CURR, SIOP_CHRG_CURR, SDP_INPUT_CURR, SDP_CHRG_CURR, DCP_INPUT_CURR, DCP_CHRG_CURR,
            CDP_INPUT_CURR, CDP_CHRG_CURR, ACA_INPUT_CURR, ACA_CHRG_CURR, MISC_INPUT_CURR, MISC_CHRG_CURR};

    // I/O
    String IO_INTERNAL_SCHEDULER = "/sys/block/mmcblk0/queue/scheduler";
    String IO_EXTERNAL_SCHEDULER = "/sys/block/mmcblk1/queue/scheduler";
    String IO_INTERNAL_SCHEDULER_TUNABLE = "/sys/block/mmcblk0/queue/iosched";
    String IO_EXTERNAL_SCHEDULER_TUNABLE = "/sys/block/mmcblk1/queue/iosched";
    String IO_INTERNAL_READ_AHEAD = "/sys/block/mmcblk0/queue/read_ahead_kb";
    String IO_EXTERNAL_READ_AHEAD = "/sys/block/mmcblk1/queue/read_ahead_kb";

    String IO_ROTATIONAL = "/sys/block/mmcblk0/queue/rotational";
    String IO_STATS = "/sys/block/mmcblk0/queue/iostats";
    String IO_RANDOM = "/sys/block/mmcblk0/queue/add_random";
    String IO_AFFINITY = "/sys/block/mmcblk0/queue/rq_affinity";

    String[] IO_ARRAY = {IO_INTERNAL_SCHEDULER, IO_EXTERNAL_SCHEDULER, IO_INTERNAL_SCHEDULER_TUNABLE,
            IO_EXTERNAL_SCHEDULER_TUNABLE, IO_INTERNAL_READ_AHEAD, IO_EXTERNAL_READ_AHEAD, IO_ROTATIONAL,
            IO_STATS, IO_RANDOM, IO_AFFINITY};

    // Kernel Samepage Merging
    String UKSM_FOLDER = "/sys/kernel/mm/uksm";
    String FULL_SCANS = "full_scans";
    String PAGES_SHARED = "pages_shared";
    String PAGES_SHARING = "pages_sharing";
    String PAGES_UNSHARED = "pages_unshared";
    String RUN = "run";
    String DEFERRED_TIMER = "deferred_timer";
    String SLEEP_MILLISECONDS = "sleep_millisecs";
    String UKSM_CPU_USE = "max_cpu_percentage";

    String[] KSM_INFOS = {FULL_SCANS, PAGES_SHARED, PAGES_SHARING, PAGES_UNSHARED};

    // Low Memory Killer
    String LMK_MINFREE = "/sys/module/lowmemorykiller/parameters/minfree";

    // Virtual Memory
    String VM_PATH = "/proc/sys/vm";
    String VM_DIRTY_RATIO = VM_PATH + "/dirty_ratio";
    String VM_DIRTY_BACKGROUND_RATIO = VM_PATH + "/dirty_background_ratio";
    String VM_DIRTY_EXPIRE_CENTISECS = VM_PATH + "/dirty_expire_centisecs";
    String VM_DIRTY_WRITEBACK_CENTISECS = VM_PATH + "/dirty_writeback_centisecs";
    String VM_MIN_FREE_KBYTES = VM_PATH + "/min_free_kbytes";
    String VM_OVERCOMMIT_RATIO = VM_PATH + "/overcommit_ratio";
    String VM_SWAPPINESS = VM_PATH + "/swappiness";
    String VM_VFS_CACHE_PRESSURE = VM_PATH + "/vfs_cache_pressure";
    String VM_LAPTOP_MODE = VM_PATH + "/laptop_mode";

    String[] VM_ARRAY = {VM_DIRTY_RATIO, VM_DIRTY_BACKGROUND_RATIO,
            VM_DIRTY_EXPIRE_CENTISECS, VM_DIRTY_WRITEBACK_CENTISECS, VM_MIN_FREE_KBYTES, VM_OVERCOMMIT_RATIO,
            VM_SWAPPINESS, VM_VFS_CACHE_PRESSURE, VM_LAPTOP_MODE, LMK_MINFREE};

    // Entropy
    String PROC_RANDOM = "/proc/sys/kernel/random";
    String PROC_RANDOM_ENTROPY_AVAILABLE = PROC_RANDOM + "/entropy_avail";
    String PROC_RANDOM_ENTROPY_POOLSIZE = PROC_RANDOM + "/poolsize";
    String PROC_RANDOM_ENTROPY_READ = PROC_RANDOM + "/read_wakeup_threshold";
    String PROC_RANDOM_ENTROPY_WRITE = PROC_RANDOM + "/write_wakeup_threshold";

    String[] ENTROPY_ARRAY = {PROC_RANDOM};

    //LED
    String LED_SPEED_GREEN = "/sys/class/leds/green/rate";

    String LED_ACTIVE = "/sys/class/leds/green/blink";
    // Misc

    // Vibration
    String[] VIBRATION_ARRAY = {
            "/sys/module/qpnp_vibrator/parameters/vib_voltage",
            "/sys/vibrator/pwmvalue",
            "/sys/class/timed_output/vibrator/amp",
            "/sys/class/timed_output/vibrator/level",
            "/sys/class/timed_output/vibrator/vtg_level",
            "/sys/devices/platform/tspdrv/nforce_timed",
            "/sys/class/timed_output/vibrator/pwm_value",
            "/sys/devices/i2c-3/3-0033/vibrator/vib0/vib_duty_cycle",
            "/sys/devices/virtual/timed_output/vibrator/voltage_level",
            "/sys/devices/virtual/timed_output/vibrator/pwm_value_1p",
            "/sys/devices/virtual/timed_output/vibrator/vmax_mv_strong",
            "/sys/devices/virtual/timed_output/vibrator/vmax_mv"
    };

    int[][] VIBRATION_MAX_MIN_ARRAY = {
            {31, 12},
            {127, 0},
            {100, 0},
            {31, 12},
            {31, 12}, // Read MAX MIN from sys
            {127, 1},
            {100, 0}, // Read MAX MIN from sys
            {100, 25}, // Needs enable path
            {3199, 1200},
            {99, 53},
            {3596, 116}, // Needs VIB_LIGHT path
            {3596, 116}
    };

    String VIB_LIGHT = "/sys/devices/virtual/timed_output/vibrator/vmax_mv_light";
    String VIB_ENABLE = "/sys/devices/i2c-3/3-0033/vibrator/vib0/vib_enable";

    // Logging
    String LOGGER_MODE = "/sys/kernel/logger_mode/logger_mode";
    String LOGGER_ENABLED = "/sys/module/logger/parameters/enabled";
    String LOGGER_LOG_ENABLED = "/sys/module/logger/parameters/log_enabled";
    String LOGD = "/system/bin/logd";

    String[] LOGGER_ARRAY = {LOGGER_MODE, LOGGER_ENABLED, LOGGER_LOG_ENABLED, LOGD, "start", "stop"};

    // BCL
    String[] BCL_ARRAY = {"/sys/devices/qcom,bcl.38/mode", "/sys/devices/qcom,bcl.39/mode", "/sys/devices/soc.0/qcom,bcl.60/mode"};

    String BCL_HOTPLUG = "/sys/module/battery_current_limit/parameters/bcl_hotplug_enable";

    // CRC
    String[] CRC_ARRAY = {
            "/sys/module/mmc_core/parameters/crc",
            "/sys/module/mmc_core/parameters/use_spi_crc"
    };

    // Fsync
    String[] FSYNC_ARRAY = {
            "/sys/devices/virtual/misc/fsynccontrol/fsync_enabled",
            "/sys/module/sync/parameters/fsync_enabled"
    };
    String DYNAMIC_FSYNC = "/sys/kernel/dyn_fsync/Dyn_fsync_active";

    // Sched Features
    // Gentle fair sleepers
    String GENTLE_FAIR_SLEEPERS = "/sys/kernel/sched/gentle_fair_sleepers";

    // MSM USB OTG
    String MSM_USB_OTG = "/sys/module/msm_otg/parameters/otg_hack_enable";

    // Network
    String TCP_AVAILABLE_CONGESTIONS = "/proc/sys/net/ipv4/tcp_available_congestion_control";
    String HOSTNAME_KEY = "net.hostname";
    String ADB_OVER_WIFI = "service.adb.tcp.port";

    // Switch Buttons
    String SWITCH_BUTTONS = "/proc/s1302/key_rep";

    String[][] MISC_ARRAY = {
            VIBRATION_ARRAY,
            {VIB_LIGHT, VIB_ENABLE, LOGGER_ENABLED, DYNAMIC_FSYNC, GENTLE_FAIR_SLEEPERS, BCL_HOTPLUG,
                    TCP_AVAILABLE_CONGESTIONS, HOSTNAME_KEY, ADB_OVER_WIFI, LED_SPEED_GREEN, LED_ACTIVE, SWITCH_BUTTONS},
            LOGGER_ARRAY, CRC_ARRAY, FSYNC_ARRAY, BCL_ARRAY};

    // Build prop
    String BUILD_PROP = "/system/build.prop";

    // Init.d
    String INITD = "/system/etc/init.d";

}
