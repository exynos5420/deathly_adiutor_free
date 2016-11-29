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

package com.exynos5420.deathlyadiutor.fragments.kernel;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.exynos5420.deathlyadiutor.R;
import com.exynos5420.deathlyadiutor.elements.DAdapter;
import com.exynos5420.deathlyadiutor.elements.DDivider;
import com.exynos5420.deathlyadiutor.elements.cards.CardViewItem;
import com.exynos5420.deathlyadiutor.elements.cards.PopupCardView;
import com.exynos5420.deathlyadiutor.elements.cards.SeekBarCardView;
import com.exynos5420.deathlyadiutor.elements.cards.SwitchCardView;
import com.exynos5420.deathlyadiutor.elements.cards.UsageCardView;
import com.exynos5420.deathlyadiutor.fragments.PathReaderFragment;
import com.exynos5420.deathlyadiutor.fragments.RecyclerViewFragment;
import com.exynos5420.deathlyadiutor.fragments.ViewPagerFragment;
import com.exynos5420.deathlyadiutor.utils.Constants;
import com.exynos5420.deathlyadiutor.utils.Utils;
import com.exynos5420.deathlyadiutor.utils.kernel.CPU;
import com.exynos5420.deathlyadiutor.utils.root.Control;
import com.kerneladiutor.library.root.RootFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by willi on 07.04.15.
 */
public class CPUFragment extends ViewPagerFragment implements Constants {

    private static CPUFragment cpuFragment;
    private CPUPart cpuPart;
    private GovernorPart governorPart;
    private int core;
    private String cluster = "";

    @Override
    public void preInit(Bundle savedInstanceState) {
        super.preInit(savedInstanceState);
        showTabs(false);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        cpuFragment = this;

        allowSwipe(false);
        addFragment(new ViewPagerItem(cpuPart == null ? cpuPart = new CPUPart() : cpuPart, null));
        addFragment(new ViewPagerItem(governorPart == null ? governorPart = new GovernorPart() : governorPart, null));
    }

    @Override
    public void onSwipe(int page) {
        super.onSwipe(page);
        allowSwipe(page == 1);
    }

    @Override
    public boolean onBackPressed() {
        if (getCurrentPage() == 1) {
            setCurrentItem(0);
            return true;
        }
        return false;
    }

    public static class CPUPart extends RecyclerViewFragment implements View.OnClickListener,
            PopupCardView.DPopupCard.OnDPopupCardListener, CardViewItem.DCardView.OnDCardListener,
            SwitchCardView.DSwitchCard.OnDSwitchCardListener {

        List<DAdapter.DView> views = new ArrayList<>();
        List<String> freqs = new ArrayList<>();

        private UsageCardView.DUsageCard mUsageCard;

        private CardViewItem.DCardView mTempCard;

        private AppCompatCheckBox[] mCoreCheckBox;
        private ProgressBar[] mCoreProgressBar;
        private AppCompatTextView[] mCoreUsageText;
        private AppCompatTextView[] mCoreFreqText;

        private PopupCardView.DPopupCard mMaxFreqCard, mMinFreqCard, mGovernorCard, mMcPowerSavingCard;

        private CardViewItem.DCardView mGovernorTunableNoPerCoreCard;

        private SwitchCardView.DSwitchCard mPowerSavingWqCard;
        private final Runnable cpuUsage = new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final float[] usage = CPU.getCpuUsage();
                        try {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (usage != null) {
                                        if (mUsageCard != null)
                                            mUsageCard.setProgress(Math.round(usage[0]));

                                        if (mCoreUsageText != null) {
                                            List<Integer> cores = CPU.getBigCoreRange();
                                            for (int i = 0; i < mCoreUsageText.length; i++) {
                                                String message = Math.round(usage[cores.get(i) + 1]) + "%";
                                                if (mCoreUsageText[i] != null) {
                                                    if (mCoreProgressBar != null && mCoreProgressBar[i] != null && mCoreProgressBar[i].getProgress() == 0) {
                                                        mCoreUsageText[i].setText("");
                                                    } else {
                                                        mCoreUsageText[i].setText(message);
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }
                            });
                        } catch (NullPointerException ignored) {
                        }
                    }
                }).start();

                getHandler().postDelayed(cpuUsage, 1000);
            }
        };
        @Override
        public String getClassName() {
            return CPUFragment.class.getSimpleName();
        }

        @Override
        public void init(Bundle savedInstanceState) {
            super.init(savedInstanceState);

            usageInit();
            tempInit();
            coreInit();
            freqInit();
            governorInit();
            mcPowerSavingInit();
            powerSavingWqInit();
        }

        private void usageInit() {
            mUsageCard = new UsageCardView.DUsageCard();
            mUsageCard.setText(getString(R.string.cpu_usage));
            addView(mUsageCard);
        }

        private void tempInit() {
            mTempCard = new CardViewItem.DCardView();
            mTempCard.setTitle(getString(R.string.cpu_temp));
            mTempCard.setDescription(CPU.getTemp());

            addView(mTempCard);
        }

        private void coreInit() {
            LinearLayout layout = new LinearLayout(getActivity());
            layout.setOrientation(LinearLayout.VERTICAL);

            mCoreCheckBox = new AppCompatCheckBox[CPU.getBigCoreRange().size()];
            mCoreProgressBar = new ProgressBar[mCoreCheckBox.length];
            mCoreUsageText = new AppCompatTextView[mCoreCheckBox.length];
            mCoreFreqText = new AppCompatTextView[mCoreCheckBox.length];
            for (int i = 0; i < mCoreCheckBox.length; i++) {
                View view = inflater.inflate(R.layout.coreview, container, false);

                mCoreCheckBox[i] = (AppCompatCheckBox) view.findViewById(R.id.core_checkbox);
                mCoreCheckBox[i].setText(String.format(getString(R.string.core), i + 1));
                mCoreCheckBox[i].setOnClickListener(this);

                mCoreProgressBar[i] = (ProgressBar) view.findViewById(R.id.progressbar);
                mCoreProgressBar[i].setMax(CPU.getFreqs().size());

                mCoreUsageText[i] = (AppCompatTextView) view.findViewById(R.id.usage);

                mCoreFreqText[i] = (AppCompatTextView) view.findViewById(R.id.freq);

                layout.addView(view);
            }

            CardViewItem.DCardView coreCard = new CardViewItem.DCardView();
            coreCard.setTitle(getString(R.string.current_freq));
            coreCard.setView(layout);

            addView(coreCard);
        }

        private void freqInit() {
            views.clear();
            freqs.clear();

            for (int freq : CPU.getFreqs())
                freqs.add(freq / 1000 + getString(R.string.mhz));

            mMaxFreqCard = new PopupCardView.DPopupCard(freqs);
            mMaxFreqCard.setTitle(getString(R.string.cpu_max_freq));
            mMaxFreqCard.setDescription(getString(R.string.cpu_max_freq_summary));
            mMaxFreqCard.setItem(CPU.getMaxFreq(true) / 1000 + getString(R.string.mhz));
            mMaxFreqCard.setOnDPopupCardListener(this);

            mMinFreqCard = new PopupCardView.DPopupCard(freqs);
            mMinFreqCard.setTitle(getString(R.string.cpu_min_freq));
            mMinFreqCard.setDescription(getString(R.string.cpu_min_freq_summary));
            mMinFreqCard.setItem(CPU.getMinFreq(true) / 1000 + getString(R.string.mhz));
            mMinFreqCard.setOnDPopupCardListener(this);

            views.add(mMaxFreqCard);
            views.add(mMinFreqCard);

            addAllViews(views);
        }

        private void governorInit() {
            views.clear();

            mGovernorCard = new PopupCardView.DPopupCard(CPU.getAvailableGovernors());
            mGovernorCard.setTitle(getString(R.string.cpu_governor));
            mGovernorCard.setDescription(getString(R.string.cpu_governor_summary));
            mGovernorCard.setItem(CPU.getCurGovernor(true));
            mGovernorCard.setOnDPopupCardListener(this);
            views.add(mGovernorCard);

            mGovernorTunableNoPerCoreCard = new CardViewItem.DCardView();
            mGovernorTunableNoPerCoreCard.setTitle(getString(R.string.cpu_governor_tunables));
            mGovernorTunableNoPerCoreCard.setDescription(getString(R.string.cpu_governor_tunables_summary));
            mGovernorTunableNoPerCoreCard.setOnDCardListener(this);
            views.add(mGovernorTunableNoPerCoreCard);

            addAllViews(views);

        }

        private void mcPowerSavingInit() {
            mMcPowerSavingCard = new PopupCardView.DPopupCard(new ArrayList<>(Arrays.asList(
                    CPU.getMcPowerSavingItems(getActivity()))));
            mMcPowerSavingCard.setTitle(getString(R.string.mc_power_saving));
            mMcPowerSavingCard.setDescription(getString(R.string.mc_power_saving_summary));
            mMcPowerSavingCard.setItem(CPU.getCurMcPowerSaving());
            mMcPowerSavingCard.setOnDPopupCardListener(this);

            addView(mMcPowerSavingCard);
        }

        private void powerSavingWqInit() {
            mPowerSavingWqCard = new SwitchCardView.DSwitchCard();
            mPowerSavingWqCard.setDescription(getString(R.string.power_saving_wq));
            mPowerSavingWqCard.setChecked(CPU.isPowerSavingWqActive());
            mPowerSavingWqCard.setOnDSwitchCardListener(this);

            addView(mPowerSavingWqCard);
        }

        @Override
        public void onClick(View v) {
            for (int i = 0; i < mCoreCheckBox.length; i++)
                if (v == mCoreCheckBox[i]) {
                    List<Integer> range = CPU.getBigCoreRange();
                    if (range.get(i) == 0) {
                        mCoreCheckBox[i].setChecked(true);
                        return;
                    }
                    CPU.activateCore(range.get(i), ((CheckBox) v).isChecked(), getActivity());
                    return;
                }
        }

        @Override
        public void onItemSelected(PopupCardView.DPopupCard dPopupCard, int position) {
            if (dPopupCard == mMaxFreqCard)
                CPU.setMaxFreq(CPU.getFreqs().get(position), getActivity());
            else if (dPopupCard == mMinFreqCard)
                CPU.setMinFreq(CPU.getFreqs().get(position), getActivity());
            else if (dPopupCard == mGovernorCard)
                CPU.setGovernor(CPU.getAvailableGovernors().get(position), getActivity());
        }

        @Override
        public void onClick(CardViewItem.DCardView dCardView) {
            if (dCardView == mGovernorTunableNoPerCoreCard) {
                cpuFragment.cluster = "big";
                cpuFragment.core = CPU.getBigCore();
                cpuFragment.governorPart.reload();
                cpuFragment.setCurrentItem(1);
            }
        }

        @Override
        public void onChecked(SwitchCardView.DSwitchCard dSwitchCard, boolean checked) {
            if (dSwitchCard == mPowerSavingWqCard)
                CPU.activatePowerSavingWq(checked, getActivity());
            }

        @Override
        public boolean onRefresh() {
            if (mTempCard != null) mTempCard.setDescription(CPU.getTemp());

            if (mCoreCheckBox != null && mCoreProgressBar != null && mCoreFreqText != null) {
                List<Integer> range = CPU.getBigCoreRange();
                for (int i = 0; i < mCoreCheckBox.length; i++) {
                    int cur = CPU.getCurFreq(range.get(i));
                    if (mCoreCheckBox[i] != null) mCoreCheckBox[i].setChecked(cur != 0);
                    if (mCoreProgressBar[i] != null)
                        mCoreProgressBar[i].setProgress(CPU.getFreqs().indexOf(cur) + 1);
                    if (mCoreFreqText[i] != null)
                        mCoreFreqText[i].setText(cur == 0 ? getString(R.string.offline) : cur / 1000 +
                                getString(R.string.mhz));
                }
            }

            if (mMaxFreqCard != null) {
                int maxFreq = CPU.getMaxFreq(false);
                if (maxFreq != 0) mMaxFreqCard.setItem(maxFreq / 1000 + getString(R.string.mhz));
            }
            if (mMinFreqCard != null) {
                int minFreq = CPU.getMinFreq(false);
                if (minFreq != 0) mMinFreqCard.setItem(minFreq / 1000 + getString(R.string.mhz));
            }
            if (mGovernorCard != null) {
                String governor = CPU.getCurGovernor(false);
                if (!governor.isEmpty()) mGovernorCard.setItem(governor);
            }
            return true;
        }
        @Override
        public void onResume() {
            super.onResume();
            getHandler().post(cpuUsage);
        }

        @Override
        public void onPause() {
            super.onPause();
            getHandler().removeCallbacks(cpuUsage);
        }
    }

    public static class GovernorPart extends PathReaderFragment {

        @Override
        public String getName() {
                return CPU.getCurGovernor(cpuFragment.core, true);
        }

        @Override
        public String getPath() {
            return getPath(CPU_GOVERNOR_TUNABLES, CPU.getCurGovernor(cpuFragment.core, true));
        }

        private String getPath(String path, String governor) {
            if (Utils.existFile(path + "/" + governor)) return path + "/" + governor;
            else for (String file : new RootFile(path).list())
                if (governor.contains(file))
                    return path + "/" + file;
            return null;
        }

        @Override
        public PATH_TYPE getType() {
            return PATH_TYPE.GOVERNOR;
        }

        @Override
        public String getError(Context context) {
                return context.getString(R.string.not_tunable, CPU.getCurGovernor(cpuFragment.core, true));
        }
    }
}
