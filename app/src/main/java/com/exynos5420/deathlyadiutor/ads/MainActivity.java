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

package com.exynos5420.deathlyadiutor.ads;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.exynos5420.deathlyadiutor.ads.R;
import com.exynos5420.deathlyadiutor.ads.elements.DAdapter;
import com.exynos5420.deathlyadiutor.ads.elements.ScrimInsetsFrameLayout;
import com.exynos5420.deathlyadiutor.ads.elements.SplashView;
import com.exynos5420.deathlyadiutor.ads.fragments.BaseFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.information.KernelInformationFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.information.UsageStatisticsFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.BatteryFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.CPUFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.CPUThermalFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.CPUVoltageFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.EntropyFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.GPUFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.GPUThermalFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.GPUVoltageFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.HeadphoneFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.IOFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.KSMFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.LMKFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.MiscFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.ScreenFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.SpeakerFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.VMFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.other.HateAdsFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.other.AboutusFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.other.SettingsFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.tools.BuildpropFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.tools.InitdFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.tools.ProfileFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.tools.StartUpCommandsFragment;
import com.exynos5420.deathlyadiutor.ads.services.ProfileTileReceiver;
import com.exynos5420.deathlyadiutor.ads.utils.Constants;
import com.exynos5420.deathlyadiutor.ads.utils.Utils;
import com.exynos5420.deathlyadiutor.ads.utils.database.ProfileDB;
import com.exynos5420.deathlyadiutor.ads.utils.kernel.CPUVoltage;
import com.exynos5420.deathlyadiutor.ads.utils.kernel.Info;
import com.exynos5420.deathlyadiutor.ads.utils.kernel.Sound;
import com.kerneladiutor.library.root.RootUtils;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by willi on 01.12.14.
 */
public class MainActivity extends BaseActivity implements Constants {

    private static final String TAG = "MainActivity";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    /**
     * Views
     */
    private Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ScrimInsetsFrameLayout mScrimInsetsFrameLayout;
    private RecyclerView mDrawerList;
    private SplashView mSplashView;
    private DAdapter.Adapter mAdapter;
    private boolean pressAgain = true;
    /**
     * Current Fragment position
     */
    private int cur_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setView();
        String password;
        if (!(password = Utils.getString("password", "", this)).isEmpty()) {
            askPassword(password);
        }
        else // Use an AsyncTask to initialize everything
            new Task().execute();

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public int getParentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public View getParentView() {
        return null;
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar == null ? toolbar = (Toolbar) findViewById(R.id.toolbar) : toolbar;
    }

    @Override
    public void setStatusBarColor() {
    }

    /**
     * Dialog which asks the user to enter his password
     *
     * @param password current encoded password
     */
    private void askPassword(final String password) {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setPadding(30, 20, 30, 20);

        final AppCompatEditText mPassword = new AppCompatEditText(this);
        mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mPassword.setHint(getString(R.string.password));
        linearLayout.addView(mPassword);

        new AlertDialog.Builder(this).setView(linearLayout).setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mPassword.getText().toString().equals(Utils.decodeString(password)))
                            new Task().execute();
                        else {
                            Utils.toast(getString(R.string.password_wrong), MainActivity.this);
                            finish();
                        }
                    }
                }).show();
    }

    /**
     * Gets called when there is an input on the navigation drawer
     *
     * @param position position of the fragment
     */
    private void selectItem(int position) {
        Fragment fragment = VISIBLE_ITEMS.get(position).getFragment();

        if (mScrimInsetsFrameLayout != null) mDrawerLayout.closeDrawer(mScrimInsetsFrameLayout);
        if (fragment == null || cur_position == position) return;
        cur_position = position;

        try {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ActionBar actionBar;
        if ((actionBar = getSupportActionBar()) != null)
            actionBar.setTitle(VISIBLE_ITEMS.get(position).getTitle());
        mAdapter.setItemChecked(position, true);
    }

    /**
     * Add all fragments in a list
     */
    private void setList() {
        ITEMS.clear();
        ITEMS.add(new DAdapter.MainHeader());
        ITEMS.add(new DAdapter.Header(getString(R.string.information)));
        ITEMS.add(new DAdapter.Item(getString(R.string.kernel_information), new KernelInformationFragment()));
        ITEMS.add(new DAdapter.Item(getString(R.string.frequency_table), new UsageStatisticsFragment()));
        ITEMS.add(new DAdapter.Header(getString(R.string.cpu)));
        ITEMS.add(new DAdapter.Item(getString(R.string.cpu_core_control), new CPUFragment()));
        if (CPUVoltage.hasCpuVoltage())
            ITEMS.add(new DAdapter.Item(getString(R.string.cpu_voltage), new CPUVoltageFragment()));
        ITEMS.add(new DAdapter.Item(getString(R.string.cpu_thermal), new CPUThermalFragment()));
//      We are adding this GPU check in alpha stage so you can use the app on N!
//      This will be gone for public release
        if (Utils.existFile(GPU_AVALIBLE_EXYNOS5_FREQS)) {
            ITEMS.add(new DAdapter.Header(getString(R.string.gpu)));
            ITEMS.add(new DAdapter.Item(getString(R.string.gpu_core_control), new GPUFragment()));
            ITEMS.add(new DAdapter.Item(getString(R.string.gpu_voltage), new GPUVoltageFragment()));
            ITEMS.add(new DAdapter.Item(getString(R.string.gpu_thermal), new GPUThermalFragment()));}
        ITEMS.add(new DAdapter.Header(getString(R.string.other_hardware)));
        ITEMS.add(new DAdapter.Item(getString(R.string.battery), new BatteryFragment()));
        ITEMS.add(new DAdapter.Item(getString(R.string.screen), new ScreenFragment()));
        if (Sound.hasWolfsonChip()){
            ITEMS.add(new DAdapter.Item(getString(R.string.speaker), new SpeakerFragment()));
            ITEMS.add(new DAdapter.Item(getString(R.string.headphone), new HeadphoneFragment()));}
        ITEMS.add(new DAdapter.Header(getString(R.string.kernel)));
        ITEMS.add(new DAdapter.Item(getString(R.string.io_scheduler), new IOFragment()));
        ITEMS.add(new DAdapter.Item(getString(R.string.ksm), new KSMFragment()));
        ITEMS.add(new DAdapter.Item(getString(R.string.low_memory_killer), new LMKFragment()));
        ITEMS.add(new DAdapter.Item(getString(R.string.virtual_memory), new VMFragment()));
        ITEMS.add(new DAdapter.Item(getString(R.string.entropy), new EntropyFragment()));
        ITEMS.add(new DAdapter.Header(getString(R.string.misc_controls)));
        ITEMS.add(new DAdapter.Item(getString(R.string.misc_controls), new MiscFragment()));
        ITEMS.add(new DAdapter.Header(getString(R.string.tools)));
        ITEMS.add(new DAdapter.Item(getString(R.string.build_prop_editor), new BuildpropFragment()));
        ITEMS.add(new DAdapter.Item(getString(R.string.profile), new ProfileFragment()));
        ITEMS.add(new DAdapter.Item(getString(R.string.initd), new InitdFragment()));
        ITEMS.add(new DAdapter.Item(getString(R.string.startup_commands), new StartUpCommandsFragment()));
        ITEMS.add(new DAdapter.Header(getString(R.string.other)));
        ITEMS.add(new DAdapter.Item(getString(R.string.settings), new SettingsFragment()));
        ITEMS.add(new DAdapter.Item(getString(R.string.about_us), new AboutusFragment()));
        ITEMS.add(new DAdapter.Item(getString(R.string.hate_ads), new HateAdsFragment()));
    }

    /**
     * Define all views
     */
    private void setView() {
        mScrimInsetsFrameLayout = (ScrimInsetsFrameLayout) findViewById(R.id.scrimInsetsFrameLayout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout != null) {
            mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.statusbar_color));
            mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        }
        mDrawerList = (RecyclerView) findViewById(R.id.drawer_list);
        mSplashView = (SplashView) findViewById(R.id.splash_view);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mLayoutManager.setSmoothScrollbarEnabled(true);
        mDrawerList.setLayoutManager(mLayoutManager);
        mDrawerList.setHasFixedSize(true);
        mDrawerList.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            }

            @Override
            public int getItemCount() {
                return 0;
            }
        });
    }

    public void setItems(BaseFragment fragment) {
        List<DAdapter.DView> tmpViews = new ArrayList<>();
        for (DAdapter.DView item : ITEMS)
            if (item.getFragment() == null
                    || Utils.getBoolean(item.getFragment().getClass().getSimpleName() + "visible", true, this))
                tmpViews.add(item);

        VISIBLE_ITEMS.clear();
        // Sort out headers without any sections
        for (int i = 0; i < tmpViews.size(); i++)
            if ((tmpViews.get(i).getFragment() == null && i < tmpViews.size() && tmpViews.get(i + 1).getFragment() != null)
                    || tmpViews.get(i).getFragment() != null
                    || tmpViews.get(i) instanceof DAdapter.MainHeader)
                VISIBLE_ITEMS.add(tmpViews.get(i));

        mAdapter = new DAdapter.Adapter(VISIBLE_ITEMS);
        mDrawerList.setAdapter(mAdapter);
        mAdapter.setItemOnly(true);
        mAdapter.setOnItemClickListener(new DAdapter.Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                selectItem(position);
            }
        });
        if (fragment != null) for (int i = 0; i < VISIBLE_ITEMS.size(); i++)
            if (VISIBLE_ITEMS.get(i).getFragment() != null && VISIBLE_ITEMS.get(i).getFragment() == fragment) {
                cur_position = i;
                mAdapter.setItemChecked(i, true);
            }

    }

    /**
     * Setup the views
     */
    private void setInterface() {
        if (mScrimInsetsFrameLayout != null) {
            mScrimInsetsFrameLayout.setLayoutParams(getDrawerParams());
            if (Utils.LIGHTTHEME)
                mScrimInsetsFrameLayout.setBackgroundColor(getResources().getColor(R.color.navigationdrawer_background_light));
        }

        setItems(null);
        if (mDrawerLayout != null) {
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, 0, 0);
            mDrawerLayout.setDrawerListener(mDrawerToggle);

            mDrawerLayout.post(new Runnable() {
                @Override
                public void run() {
                    if (mDrawerToggle != null) mDrawerToggle.syncState();
                }
            });
        }
    }

    @Override
    public boolean getDisplayHomeAsUpEnabled() {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mScrimInsetsFrameLayout != null)
            mScrimInsetsFrameLayout.setLayoutParams(getDrawerParams());
        if (mDrawerToggle != null) mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * This makes onBackPressed function work in Fragments
     */
    @Override
    public void onBackPressed() {
        try {
            if (!VISIBLE_ITEMS.get(cur_position).getFragment().onBackPressed())
                if (mDrawerLayout == null || !mDrawerLayout.isDrawerOpen(mScrimInsetsFrameLayout)) {
                    if (pressAgain) {
                        Utils.toast(getString(R.string.press_back_again), this);
                        pressAgain = false;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pressAgain = true;
                            }
                        }, 2000);
                    } else super.onBackPressed();
                } else mDrawerLayout.closeDrawer(mScrimInsetsFrameLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Exit SU
     */
    @Override
    protected void onDestroy() {
        RootUtils.closeSU();
        super.onDestroy();
    }

    /**
     * A function to calculate the width of the Navigation Drawer
     * Phones and Tablets have different sizes
     *
     * @return the LayoutParams for the Drawer
     */
    private DrawerLayout.LayoutParams getDrawerParams() {
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) mScrimInsetsFrameLayout.getLayoutParams();
        int width = getResources().getDisplayMetrics().widthPixels;

        boolean tablet = Utils.isTablet(this);
        int actionBarSize = Utils.getActionBarHeight(this);
        if (Utils.getScreenOrientation(this) == Configuration.ORIENTATION_LANDSCAPE) {
            params.width = width / 2;
            if (tablet)
                params.width -= actionBarSize + (35 * getResources().getDisplayMetrics().density);
        } else params.width = tablet ? width / 2 : width - actionBarSize;

        return params;
    }

    @TargetApi(23)
    private void check_writeexternalstorage() {
        if (Build.VERSION.SDK_INT >= 23) {
            int hasWriteExternalPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWriteExternalPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }
        return;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Interface to make onBackPressed function work in Fragments
     */
    public interface OnBackButtonListener {
        boolean onBackPressed();
    }

    private class Task extends AsyncTask<Void, Void, Void> {

        private boolean hasRoot;
        private boolean hasBusybox;

        @Override
        protected Void doInBackground(Void... params) {
            // Check root access and busybox installation
            if (RootUtils.rooted()) hasRoot = RootUtils.rootAccess();
            if (hasRoot) hasBusybox = RootUtils.hasAppletSupport();
            if (hasRoot && hasBusybox) {
                // Set permissions to specific files which are not readable by default
                String[] writePermission = {LMK_MINFREE};
                for (String file : writePermission)
                    RootUtils.runCommand("chmod 644 " + file);

                setList();
            }
            check_writeexternalstorage();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!hasRoot || !hasBusybox) {
                Intent i = new Intent(MainActivity.this, TextActivity.class);
                Bundle args = new Bundle();
                if (!hasRoot) args.putString(TextActivity.ARG_TEXT, getString(R.string.no_root));
                else if (!hasBusybox) args.putString(TextActivity.ARG_TEXT, getString(R.string.no_busybox));
                i.putExtras(args);
                startActivity(i);

                if (hasRoot) {
                    // Root is there, platform is compatible but busybox is missing
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=stericson.busybox")));
                    } catch (ActivityNotFoundException ignored) {
                    }
                }
                cancel(true);
                finish();
                return;
            }
            mSplashView.finish();
            setInterface();

            try {
                if (Info.getKernelVersion().contains("Deathly") || Info.getKernelVersion().contains("ShEV")) {
                    if (!Info.getKernelVersion().contains("1.0"))
                        new AlertDialog.Builder(MainActivity.this)
                                .setMessage(getString(R.string.kernel_outdated_message))
                                .setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
                }
                else {
                        new AlertDialog.Builder(MainActivity.this)
                                .setMessage(getString(R.string.no_deathly_kernel))
                                .setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
                    }
            } catch (Exception ignored) {
            }

            // Start with the very first fragment on the list
            for (int i = 0; i < VISIBLE_ITEMS.size(); i++) {
                if (VISIBLE_ITEMS.get(i).getFragment() != null) {
                    selectItem(i);
                    break;
                }
            }

            ProfileTileReceiver.publishProfileTile(new ProfileDB(MainActivity.this).getAllProfiles(),
                    MainActivity.this);
        }
    }
}
