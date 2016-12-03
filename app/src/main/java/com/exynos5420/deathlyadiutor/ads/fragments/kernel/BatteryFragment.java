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

package com.exynos5420.deathlyadiutor.ads.fragments.kernel;

import android.os.Bundle;

import com.exynos5420.deathlyadiutor.ads.R;
import com.exynos5420.deathlyadiutor.ads.elements.DDivider;
import com.exynos5420.deathlyadiutor.ads.elements.cards.CardViewItem;
import com.exynos5420.deathlyadiutor.ads.elements.cards.SeekBarCardView;
import com.exynos5420.deathlyadiutor.ads.elements.cards.SwitchCardView;
import com.exynos5420.deathlyadiutor.ads.elements.cards.UsageCardView;
import com.exynos5420.deathlyadiutor.ads.fragments.RecyclerViewFragment;
import com.exynos5420.deathlyadiutor.ads.utils.Utils;
import com.exynos5420.deathlyadiutor.ads.utils.kernel.Battery;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Martin Bouchet on 25.11.16.
 */
public class BatteryFragment extends RecyclerViewFragment implements SwitchCardView.DSwitchCard.OnDSwitchCardListener,
        SeekBarCardView.DSeekBarCard.OnDSeekBarCardListener{

    private UsageCardView.DUsageCard mBatteryLevelCard;
    private CardViewItem.DCardView mBatteryCapacity, mBatteryVoltageCard, mBatteryTemperature, mChargingSource, mChargingCurrents;
    private SwitchCardView.DSwitchCard mUnstablePowerDetection, mSIOPtoggle;
    private SeekBarCardView.DSeekBarCard mACmainsInputCurr, mACmainschrgCurr, mSIOPInputCurr, mSIOPchrgCurr, mSDPInputCurr, mSDPchrgCurr;

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        batteryLevelInit();
        batteryCapacityInit();
        batteryVoltageInit();
        batteryTemperatureInit();
        chargingSourceInit();
        chargingCurrentsInit();
        unstablePowerDetectionToggleInit();
        AddDivider(getString(R.string.ac_divider), getString(R.string.ac_divider_summary));
        mACmainsCurrnetsInit();
        AddDivider(getString(R.string.siop_divider), getString(R.string.siop_divider_summary));
        mSIOPInit();
        AddDivider(getString(R.string.sdp_divider), getString(R.string.sdp_divider_summary));
        mSDPCurrnetsInit();

    }

    @Override
    public void postInit(Bundle savedInstanceState) {
        super.postInit(savedInstanceState);
        if (getCount() < 4) showApplyOnBoot(false);
    }

    private void batteryLevelInit() {
        mBatteryLevelCard = new UsageCardView.DUsageCard();
        mBatteryLevelCard.setText(getString(R.string.battery_level));
        mBatteryLevelCard.setProgress(Battery.getChargeLevel());

        addView(mBatteryLevelCard);
    }

    private void batteryCapacityInit() {
        mBatteryCapacity = new CardViewItem.DCardView();
        mBatteryCapacity.setTitle(getString(R.string.max_batt_capacity));
        mBatteryCapacity.setDescription(Battery.getBatteryCapacity() + getString(R.string.mah));

        addView(mBatteryCapacity);
    }

    private void batteryVoltageInit() {
        mBatteryVoltageCard = new CardViewItem.DCardView();
        mBatteryVoltageCard.setTitle(getString(R.string.battery_voltage));
        mBatteryVoltageCard.setDescription((Utils.stringToInt(Battery.getVoltage())/1000) + getString(R.string.mv));

        addView(mBatteryVoltageCard);
    }

    private void batteryTemperatureInit() {
        double celsius = (double) Utils.stringToInt(Battery.getTemperature()) / 10;
        mBatteryTemperature = new CardViewItem.DCardView();
        mBatteryTemperature.setTitle(getString(R.string.battery_temperature));
        mBatteryTemperature.setDescription(Utils.formatCelsius(celsius) + " " + Utils.celsiusToFahrenheit(celsius));

        addView(mBatteryTemperature);
    }

    private void chargingSourceInit() {
        mChargingSource = new CardViewItem.DCardView();
        mChargingSource.setTitle(getString(R.string.charging_source));
        mChargingSource.setDescription(getChrgSourceName());

        addView(mChargingSource);
    }
    private void chargingCurrentsInit() {
        mChargingCurrents = new CardViewItem.DCardView();
        mChargingCurrents.setTitle(getString(R.string.charging_curr_limits));
        mChargingCurrents.setDescription(getString(R.string.current_now) + Battery.getCurrent("now") + getString(R.string.ma) + "\n"
                + getString(R.string.current_avg) + Battery.getCurrent("avg") + getString(R.string.ma) + "\n"
                + getString(R.string.current_max) + Battery.getCurrent("max") + getString(R.string.ma)); //This string will have to look more understandable for users.

        addView(mChargingCurrents);
    }
    
    private void unstablePowerDetectionToggleInit(){
            mUnstablePowerDetection = new SwitchCardView.DSwitchCard();
            mUnstablePowerDetection.setTitle(getString(R.string.unstable_power_detection));
            mUnstablePowerDetection.setDescription(getString(R.string.unstable_power_detection_summary));
            mUnstablePowerDetection.setChecked(Battery.isUnstablePowerDetectionActive());
            mUnstablePowerDetection.setOnDSwitchCardListener(this);

            addView(mUnstablePowerDetection);
    }

    private void mACmainsCurrnetsInit(){
        List<String> list_input_curr_options = new ArrayList<>();
        List<String> list_chrg_curr_options = new ArrayList<>();

        for (int i = 450; i <= 3000; i = i + 25){
            list_input_curr_options.add(i + getString(R.string.ma));
            if (i <= 2550){
                list_chrg_curr_options.add(i + getString(R.string.ma));
            }
        }

        mACmainsInputCurr = new SeekBarCardView.DSeekBarCard(list_input_curr_options);
        mACmainsInputCurr.setTitle(getString(R.string.ac_mains_input));
        mACmainsInputCurr.setDescription("");
        mACmainsInputCurr.setProgress(Utils.stringToInt(Battery.getACmainsInputcurr()));
        mACmainsInputCurr.setOnDSeekBarCardListener(this);

        addView(mACmainsInputCurr);

        mACmainschrgCurr = new SeekBarCardView.DSeekBarCard(list_chrg_curr_options);
        mACmainschrgCurr.setTitle(getString(R.string.ac_mains_charge));
        mACmainschrgCurr.setDescription(getString(R.string.ac_mains_charge_summary));
        mACmainschrgCurr.setProgress(Utils.stringToInt(Battery.getACmainschrgcurr()));
        mACmainschrgCurr.setOnDSeekBarCardListener(this);

        addView(mACmainschrgCurr);
    }

    private void mSIOPInit(){
        mSIOPtoggle = new SwitchCardView.DSwitchCard();
        mSIOPtoggle.setTitle(getString(R.string.siop_toggle_tittle));
        mSIOPtoggle.setDescription(getString(R.string.siop_toggle_summary));
        mSIOPtoggle.setChecked(Battery.isSIOPActive());
        mSIOPtoggle.setOnDSwitchCardListener(this);

        addView(mSIOPtoggle);

        List<String> list_input_curr_options = new ArrayList<>();
        List<String> list_chrg_curr_options = new ArrayList<>();

        for (int i = 450; i <= 3000; i = i + 25){
            list_input_curr_options.add(i + getString(R.string.ma));
            if (i <= 2550){
                list_chrg_curr_options.add(i + getString(R.string.ma));
            }
        }

        mSIOPInputCurr = new SeekBarCardView.DSeekBarCard(list_input_curr_options);
        mSIOPInputCurr.setTitle(getString(R.string.siop_input_curr));
        mSIOPInputCurr.setDescription("");
        mSIOPInputCurr.setProgress(Utils.stringToInt(Battery.getSIOPInputcurr()));
        mSIOPInputCurr.setOnDSeekBarCardListener(this);

        addView(mSIOPInputCurr);

        mSIOPchrgCurr = new SeekBarCardView.DSeekBarCard(list_chrg_curr_options);
        mSIOPchrgCurr.setTitle(getString(R.string.siop_charge_curr));
        mSIOPchrgCurr.setDescription(getString(R.string.siop_charge_curr_summary));
        mSIOPchrgCurr.setProgress(Utils.stringToInt(Battery.getSIOPchrgcurr()));
        mSIOPchrgCurr.setOnDSeekBarCardListener(this);

        addView(mSIOPchrgCurr);
    }

    private void mSDPCurrnetsInit(){
        List<String> list_curr_options = new ArrayList<>();
        for (int i = 450; i <= 1500; i = i + 25){
            list_curr_options.add(i + getString(R.string.ma));
        }
        mSDPInputCurr = new SeekBarCardView.DSeekBarCard(list_curr_options);
        mSDPInputCurr.setTitle(getString(R.string.sdp_input_curr));
        mSDPInputCurr.setDescription("");
        mSDPInputCurr.setProgress(Utils.stringToInt(Battery.getSDPInputcurr()));
        mSDPInputCurr.setOnDSeekBarCardListener(this);

        addView(mSDPInputCurr);

        mSDPchrgCurr = new SeekBarCardView.DSeekBarCard(list_curr_options);
        mSDPchrgCurr.setTitle(getString(R.string.sdp_charge_curr));
        mSDPchrgCurr.setDescription(getString(R.string.sdp_charge_curr_summary));
        mSDPchrgCurr.setProgress(Utils.stringToInt(Battery.getSDPchrgcurr()));
        mSDPchrgCurr.setOnDSeekBarCardListener(this);

        addView(mSDPchrgCurr);
    }

    private void AddDivider(String tittle, String description){
        DDivider mDivider = new DDivider();
        mDivider.setText(tittle);
        mDivider.setDescription(description);

        addView(mDivider);
    }

    @Override
    public void onChecked(SwitchCardView.DSwitchCard dSwitchCard, boolean checked) {
        if (dSwitchCard == mUnstablePowerDetection)
            Battery.activateUnstablePowerDetection(checked, getActivity());
        else if (dSwitchCard == mSIOPtoggle)
            Battery.activateSIOP(checked, getActivity());

    }

    @Override
    public void onChanged(SeekBarCardView.DSeekBarCard dSeekBarCard, int position) {
    }

    @Override
    public void onStop(SeekBarCardView.DSeekBarCard dSeekBarCard, int position) {
        if (dSeekBarCard == mACmainsInputCurr)
            Battery.setACmainsInputcurr(position, getActivity());
        else if (dSeekBarCard == mACmainschrgCurr)
            Battery.setACmainschrgcurr(position, getActivity());
        else if (dSeekBarCard == mSIOPInputCurr)
            Battery.setSIOPInputcurr(position, getActivity());
        else if (dSeekBarCard == mSIOPchrgCurr)
            Battery.setSIOPchrgcurr(position, getActivity());
        else if (dSeekBarCard == mSDPInputCurr)
            Battery.setSDPInputcurr(position, getActivity());
        else if (dSeekBarCard == mSDPchrgCurr)
            Battery.setSDPchrgcurr(position, getActivity());

    }

    @Override
    public boolean onRefresh() {

        if (mBatteryLevelCard != null)
            mBatteryLevelCard.setProgress(Battery.getChargeLevel());
        if (mBatteryVoltageCard != null)
            mBatteryVoltageCard.setDescription((Utils.stringToInt(Battery.getVoltage())/1000) + getString(R.string.mv));
        if (mBatteryTemperature != null) {
            double celsius = (double) Utils.stringToInt(Battery.getTemperature()) / 10;
            mBatteryTemperature.setDescription(Utils.formatCelsius(celsius) + " " + Utils.celsiusToFahrenheit(celsius));
        }
        if (mChargingSource != null)
            mChargingSource.setDescription(getChrgSourceName());
        if (mChargingCurrents != null)
            mChargingCurrents.setDescription(getString(R.string.current_now) + Battery.getCurrent("now") + getString(R.string.ma) + "\n"
                    + getString(R.string.current_avg) + Battery.getCurrent("avg") + getString(R.string.ma) + "\n"
                    + getString(R.string.current_max) + Battery.getCurrent("max") + getString(R.string.ma)); //This string will have to look more understandable for users.
        return true;

    }
    
    public String getChrgSourceName(){
        switch (Battery.getChargingSource()){
            case "0":
                return getString(R.string.chrg_src_unknown);
            case "1":
                return getString(R.string.chrg_src_battery);
            case "2":
                return getString(R.string.chrg_src_wtf);
            case "3":
                return getString(R.string.chrg_src_acmains);
            case "4":
                return getString(R.string.chrg_src_sdp);
            case "5":
                return getString(R.string.chrg_src_dcp);
            case "6":
                return getString(R.string.chrg_src_cdp);
            case "7":
                return getString(R.string.chrg_src_aca);
            case "8":
                return getString(R.string.chrg_src_misc);
            case "9":
                return getString(R.string.chrg_src_car);
            case "10":
                return getString(R.string.chrg_src_wireless);
            case "11":
                return getString(R.string.chrg_src_uartoff);
            case "12":
                return getString(R.string.chrg_src_otg);
        }
        return "";
    }

}
