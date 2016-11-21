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

package com.exynos5420.deathlyadiutor.utils;

import com.exynos5420.deathlyadiutor.BuildConfig;
import com.exynos5420.deathlyadiutor.elements.DAdapter;

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
    String GAMMA_URL = "https://raw.githubusercontent.com/Grarak/KernelAdiutor/master/gamma_profiles.json";
    List<DAdapter.DView> ITEMS = new ArrayList<>();
    List<DAdapter.DView> VISIBLE_ITEMS = new ArrayList<>();

    // Kernel Informations
    String PROC_VERSION = "/proc/version";
    String PROC_CPUINFO = "/proc/cpuinfo";
    String PROC_MEMINFO = "/proc/meminfo";

    // CPU
    String CPU_CUR_FREQ = "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_cur_freq";
    String CPU_TEMP_ZONE0 = "/sys/class/thermal/thermal_zone0/temp";
    String CPU_TEMP_ZONE1 = "/sys/class/thermal/thermal_zone1/temp";
    String CPU_CORE_ONLINE = "/sys/devices/system/cpu/cpu%d/online";
    String CPU_MAX_FREQ = "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_max_freq";
    String CPU_MAX_FREQ_KT = "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_max_freq_kt";
    String CPU_ENABLE_OC = "/sys/devices/system/cpu/cpu%d/cpufreq/enable_oc";
    String CPU_MIN_FREQ = "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_min_freq";
    String CPU_MAX_SCREEN_OFF_FREQ = "/sys/devices/system/cpu/cpu%d/cpufreq/screen_off_max_freq";
    String CPU_MSM_CPUFREQ_LIMIT = "/sys/kernel/msm_cpufreq_limit/cpufreq_limit";
    String CPU_AVAILABLE_FREQS = "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_available_frequencies";
    String[] CPU_TIME_IN_STATE_ARRAY = {"/sys/devices/system/cpu/cpufreq/stats/cpu%d/time_in_state",
            "/sys/devices/system/cpu/cpu%d/cpufreq/stats/time_in_state"};
    String CPU_SCALING_GOVERNOR = "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_governor";
    String CPU_AVAILABLE_GOVERNORS = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors";

    String CPU_GOVERNOR_TUNABLES = "/sys/devices/system/cpu/cpufreq";
    String CPU_GOVERNOR_TUNABLES_CORE = "/sys/devices/system/cpu/cpu%d/cpufreq";

    String CPU_MC_POWER_SAVING = "/sys/devices/system/cpu/sched_mc_power_savings";
    String CPU_WQ_POWER_SAVING = "/sys/module/workqueue/parameters/power_efficient";
    String CPU_AVAILABLE_CFS_SCHEDULERS = "/sys/devices/system/cpu/sched_balance_policy/available_sched_balance_policy";
    String CPU_CURRENT_CFS_SCHEDULER = "/sys/devices/system/cpu/sched_balance_policy/current_sched_balance_policy";

    String CPU_QUIET = "/sys/devices/system/cpu/cpuquiet";
    String CPU_QUIET_ENABLE = CPU_QUIET + "/cpuquiet_driver/enabled";
    String CPU_QUIET_AVAILABLE_GOVERNORS = CPU_QUIET + "/available_governors";
    String CPU_QUIET_CURRENT_GOVERNOR = CPU_QUIET + "/current_governor";

    String CPU_BOOST = "/sys/module/cpu_boost/parameters";
    String CPU_BOOST_ENABLE = CPU_BOOST + "/cpu_boost";
    String CPU_BOOST_ENABLE_2 = CPU_BOOST + "/cpuboost_enable";
    String CPU_BOOST_DEBUG_MASK = CPU_BOOST + "/debug_mask";
    String CPU_BOOST_MS = CPU_BOOST + "/boost_ms";
    String CPU_BOOST_SYNC_THRESHOLD = CPU_BOOST + "/sync_threshold";
    String INPUT_BOOST_ENABLE = CPU_BOOST + "/input_boost_enabled";
    String CPU_BOOST_INPUT_MS = CPU_BOOST + "/input_boost_ms";
    String CPU_BOOST_INPUT_BOOST_FREQ = CPU_BOOST + "/input_boost_freq";
    String CPU_BOOST_WAKEUP = CPU_BOOST + "/wakeup_boost";
    String CPU_BOOST_HOTPLUG = CPU_BOOST + "/hotplug_boost";

    String CPU_TOUCH_BOOST = "/sys/module/msm_performance/parameters/touchboost";

    // Alu_Touch_Boost : Added By Eliminater74 //
    String ALU_T_BOOST = "/sys/module/alu_t_boost/parameters";
    String ALU_T_BOOST_FREQ = ALU_T_BOOST + "/input_boost_freq";
    String ALU_T_BOOST_MS = ALU_T_BOOST + "/input_boost_ms";
    String ALU_T_BOOST_INTERVAL = ALU_T_BOOST + "/min_input_interval";
    String ALU_T_BOOST_CPUS = ALU_T_BOOST + "/nr_boost_cpus";

    String CPU_MSM_LIMITER = "/sys/kernel/msm_limiter";
    String CPU_MSM_LIMITER_ENABLE = "/sys/kernel/msm_limiter/limiter_enabled";
    String CPU_MSM_LIMITER_ENABLE_NEW = "/sys/kernel/msm_limiter/freq_control";
    String CPU_MSM_LIMITER_RESUME_MAX = "/sys/kernel/msm_limiter/resume_max_freq";
    String CPU_MSM_LIMITER_SUSPEND_MIN = "/sys/kernel/msm_limiter/suspend_min_freq";
    String CPU_MSM_LIMITER_SUSPEND_MAX = "/sys/kernel/msm_limiter/suspend_max_freq";
    String CPU_MAX_FREQ_PER_CORE = "/sys/kernel/msm_limiter/resume_max_freq_%d";
    String CPU_MIN_FREQ_PER_CORE = "/sys/kernel/msm_limiter/suspend_min_freq_%d";
    String CPU_MSM_LIMITER_SCALING_GOVERNOR = "/sys/kernel/msm_limiter/scaling_governor";
    String CPU_MSM_LIMITER_SCALING_GOVERNOR_PER_CORE = "/sys/kernel/msm_limiter/scaling_governor_%d";
    String CPU_MSM_LIMITER_VERSION = "/sys/kernel/msm_limiter/msm_limiter_version";

    // Big.Little Part Numbers
    // A15: 0xc0f , A57: 0xd07
    String[] CPU_BIG_PARTS = {"0xc0f", "0xd07"};
    // A53: 0xd03
    String[] CPU_LITTLE_PARTS = {"0xd03"};

    String[] CPU_ARRAY = {CPU_CUR_FREQ, CPU_TEMP_ZONE0, CPU_TEMP_ZONE1, CPU_CORE_ONLINE, CPU_MAX_FREQ, CPU_MAX_FREQ_KT, CPU_ENABLE_OC,
            CPU_MIN_FREQ, CPU_MAX_SCREEN_OFF_FREQ, CPU_MSM_CPUFREQ_LIMIT, CPU_AVAILABLE_FREQS, CPU_SCALING_GOVERNOR,
            CPU_AVAILABLE_GOVERNORS, CPU_GOVERNOR_TUNABLES, CPU_GOVERNOR_TUNABLES_CORE, CPU_MC_POWER_SAVING, CPU_WQ_POWER_SAVING,
            CPU_AVAILABLE_CFS_SCHEDULERS, CPU_CURRENT_CFS_SCHEDULER, CPU_QUIET, CPU_BOOST, CPU_TOUCH_BOOST, CPU_MSM_LIMITER_ENABLE, CPU_MSM_LIMITER_ENABLE_NEW,
            CPU_MSM_LIMITER_RESUME_MAX, CPU_MSM_LIMITER_SUSPEND_MIN, CPU_MSM_LIMITER_SUSPEND_MAX, CPU_MSM_LIMITER_SCALING_GOVERNOR,
            CPU_MSM_LIMITER_SCALING_GOVERNOR_PER_CORE, CPU_MIN_FREQ_PER_CORE, CPU_MAX_FREQ_PER_CORE, ALU_T_BOOST};

    //Core Control
    String MINBIG = "/sys/devices/system/cpu/cpu4/core_ctl/min_cpus";
    String MINLITTLE = "/sys/devices/system/cpu/cpu0/core_ctl/min_cpus";

    String MAXBIG = "/sys/devices/system/cpu/cpu4/core_ctl/max_cpus";
    String MAXLITTLE = "/sys/devices/system/cpu/cpu0/core_ctl/max_cpus";

    String[] CORECONTROL_ARRAY = {MINBIG, MINLITTLE, MAXBIG, MAXLITTLE};

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
    String MDNIE_MODE = "/sys/class/mdnie/mdnie/mode";
    String TOUCHKEY_GLOVE_MODE = "/sys/class/mdnie/mdnie/mode";

    String[] SCREEN_ARRAY = {COMMAND_PATH, COMMAND_RESULT_PATH, POWER_REDUCE, MDNIE_MODE};

    // Sound

    String[] SOUND_ARRAY = {};

    // Battery
    String FORCE_FAST_CHARGE = "/sys/kernel/fast_charge/force_fast_charge";
    String FORCE_FAST_CHARGE_CURRENT = "/sys/kernel/fast_charge_current/force_fast_charge_current";
    String BLX = "/sys/devices/virtual/misc/batterylifeextender/charging_limit";
    String CHARGE_LEVEL = "/sys/kernel/charge_levels";
    String AC_CHARGE_LEVEL = CHARGE_LEVEL + "/charge_level_ac";
    String USB_CHARGE_LEVEL = CHARGE_LEVEL + "/charge_level_usb";

    String CHARGE_RATE = "sys/kernel/thundercharge_control";
    String CHARGE_RATE_ENABLE = CHARGE_RATE + "/enabled";
    String CUSTOM_CHARGING_RATE = CHARGE_RATE + "/custom_current";
    String LOW_POWER_VALUE = "sys/module/battery_current_limit/parameters/low_battery_value";

    // C-States
    String C0STATE = "/sys/module/msm_pm/modes/cpu0/wfi/idle_enabled";
    String C1STATE = "/sys/module/msm_pm/modes/cpu0/retention/idle_enabled";
    String C2STATE = "/sys/module/msm_pm/modes/cpu0/standalone_power_collapse/idle_enabled";
    String C3STATE = "/sys/module/msm_pm/modes/cpu0/power_collapse/idle_enabled";

    // State Notifier added By Eliminater74
    String STATE_NOTIFIER = "/sys/module/state_notifier/parameters";
    String STATE_NOTIFIER_ENABLED = STATE_NOTIFIER + "/enabled";

    // Power suspend
    String POWER_SUSPEND = "/sys/kernel/power_suspend";
    String POWER_SUSPEND_MODE = POWER_SUSPEND + "/power_suspend_mode";
    String POWER_SUSPEND_STATE = POWER_SUSPEND + "/power_suspend_state";
    String POWER_SUSPEND_VERSION = POWER_SUSPEND + "/power_suspend_version";

    // Arch power
    String ARCH_POWER = "/sys/kernel/sched/arch_power";

    String[] BATTERY_ARRAY = {POWER_SUSPEND_MODE, POWER_SUSPEND_STATE, C0STATE, C1STATE, C2STATE, C3STATE, FORCE_FAST_CHARGE,
            CHARGE_RATE, BLX, CHARGE_RATE, ARCH_POWER, STATE_NOTIFIER, CHARGE_LEVEL, LOW_POWER_VALUE};

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

    String GETENFORCE = "getenforce";
    String SETENFORCE = "setenforce";

    // Switch Buttons
    String SWITCH_BUTTONS = "/proc/s1302/key_rep";

    String[][] MISC_ARRAY = {
            VIBRATION_ARRAY,
            {VIB_LIGHT, VIB_ENABLE, LOGGER_ENABLED, DYNAMIC_FSYNC, GENTLE_FAIR_SLEEPERS, BCL_HOTPLUG,
                    POWER_SUSPEND_STATE, TCP_AVAILABLE_CONGESTIONS, HOSTNAME_KEY, ADB_OVER_WIFI, GETENFORCE, SETENFORCE, LED_SPEED_GREEN, LED_ACTIVE, SWITCH_BUTTONS},
            LOGGER_ARRAY, CRC_ARRAY, FSYNC_ARRAY, BCL_ARRAY};

    // Build prop
    String BUILD_PROP = "/system/build.prop";

    // Init.d
    String INITD = "/system/etc/init.d";

}
