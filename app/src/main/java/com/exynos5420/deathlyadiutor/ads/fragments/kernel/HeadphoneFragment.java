/*
 * Copyright (C) 2016 Martin Bouchet
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
import com.exynos5420.deathlyadiutor.ads.elements.cards.SeekBarCardView;
import com.exynos5420.deathlyadiutor.ads.elements.cards.SwitchCardView;
import com.exynos5420.deathlyadiutor.ads.fragments.RecyclerViewFragment;
import com.exynos5420.deathlyadiutor.ads.utils.Constants;
import com.exynos5420.deathlyadiutor.ads.utils.Utils;
import com.exynos5420.deathlyadiutor.ads.utils.kernel.Sound;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin Bouchet on 21/11/2016.
 */

public class HeadphoneFragment extends RecyclerViewFragment implements Constants, SwitchCardView.DSwitchCard.OnDSwitchCardListener,
        SeekBarCardView.DSeekBarCard.OnDSeekBarCardListener{

    private SeekBarCardView.DSeekBarCard mHeadphoneGainLeft, mHeadphoneGainRight;
    private SwitchCardView.DSwitchCard mEQenabled, mHeadphoneMono;
    private SeekBarCardView.DSeekBarCard[] mEQgains = new SeekBarCardView.DSeekBarCard[5];

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        HeadphoneGainLeftInit();
        HeadphoneGainRightInit();
        switchHeadphoneMono();
        switchEQonoff();
        addDivider(getString(R.string.headphone_equalizer));
        HeadphoneEQgainsInit();

    }

    private void HeadphoneGainLeftInit(){
        List<String> list_headphone_volumes = new ArrayList<>();
        for (int i = 80; i < 151; i++){
            list_headphone_volumes.add(i + getString(R.string.dB));
            if (i!= 150) list_headphone_volumes.add((i + 0.5) + getString(R.string.dB));
        }
        mHeadphoneGainLeft = new SeekBarCardView.DSeekBarCard(list_headphone_volumes);
        mHeadphoneGainLeft.setTitle(getString(R.string.left_headphone_gain));
        mHeadphoneGainLeft.setDescription(getString(R.string.left_headphone_gain_summary));
        mHeadphoneGainLeft.setProgress(Utils.stringToInt(Sound.getHeadphoneGainLeft()));
        mHeadphoneGainLeft.setOnDSeekBarCardListener(this);

        addView(mHeadphoneGainLeft);
    }

    private void HeadphoneGainRightInit(){
        List<String> list_headphone_volumes = new ArrayList<>();
        for (int i = 80; i < 151; i++){
            list_headphone_volumes.add(i + getString(R.string.dB));
            if (i!= 150) list_headphone_volumes.add((i + 0.5) + getString(R.string.dB));
        }
        mHeadphoneGainRight = new SeekBarCardView.DSeekBarCard(list_headphone_volumes);
        mHeadphoneGainRight.setTitle(getString(R.string.right_headphone_gain));
        mHeadphoneGainRight.setDescription(getString(R.string.right_headphone_gain_summary));
        mHeadphoneGainRight.setProgress(Utils.stringToInt(Sound.getHeadphoneGainRight()));
        mHeadphoneGainRight.setOnDSeekBarCardListener(this);

        addView(mHeadphoneGainRight);
    }

    private void switchHeadphoneMono(){
        mHeadphoneMono = new SwitchCardView.DSwitchCard();
        mHeadphoneMono.setTitle(getString(R.string.headphone_mono));
        mHeadphoneMono.setDescription(getString(R.string.headphone_mono_summary));
        mHeadphoneMono.setChecked(Sound.isHeadphoneMono());
        mHeadphoneMono.setOnDSwitchCardListener(this);

        addView(mHeadphoneMono);
    }

    private void switchEQonoff(){
        mEQenabled = new SwitchCardView.DSwitchCard();
        mEQenabled.setTitle(getString(R.string.headphone_equalizer));
        mEQenabled.setDescription(getString(R.string.headphone_equalizer_toggle_summary));
        mEQenabled.setChecked(Sound.isHeadphoneEqEnabled());
        mEQenabled.setOnDSwitchCardListener(this);

        addView(mEQenabled);
    }

    private void HeadphoneEQgainsInit(){
        List<String> list_eq_gains = new ArrayList<>();
        for (int i = -12; i < 13; i++) {
            list_eq_gains.add(i + getString(R.string.dB));
            if (i != 12) list_eq_gains.add((i + 0.5) + getString(R.string.dB));
        }
        for (int i = 0; i < 5; i++){
            mEQgains[i] = new SeekBarCardView.DSeekBarCard(list_eq_gains);
            mEQgains[i].setTitle(getString(R.string.equalizer_band) + " " + (i+1));
            mEQgains[i].setDescription(getBandDescription(i));
            mEQgains[i].setProgress(Utils.stringToInt(Sound.getHeadphoneEqGain(i)));
            mEQgains[i].setOnDSeekBarCardListener(this);
            addView(mEQgains[i]);
        }
    }

    public void addDivider(String text){
        DDivider mDivider = new DDivider();
        mDivider.setText(text);

        addView(mDivider);
    }

    @Override
    public void onChecked(SwitchCardView.DSwitchCard dSwitchCard, boolean checked) {
        if (dSwitchCard == mEQenabled)
            Sound.switchHeadphoneEq(checked, getActivity());
        else if (dSwitchCard == mHeadphoneMono)
            Sound.switchHeadphonMono(checked, getActivity());
    }

    @Override
    public void onChanged(SeekBarCardView.DSeekBarCard dSeekBarCard, int position) {
    }

    @Override
    public void onStop(SeekBarCardView.DSeekBarCard dSeekBarCard, int position) {
        if (dSeekBarCard == mHeadphoneGainLeft)
            Sound.setHeadphoneGainLeft(position, getActivity());
        else if (dSeekBarCard == mHeadphoneGainRight)
            Sound.setHeadphoneGainRight(position, getActivity());
        else {
            for (int i = 0; i < 5; i++){
                if (dSeekBarCard == mEQgains[i]){
                    Sound.setHeadphoneEqGain(position, i, getActivity());
                }
            }
        }
    }

    private String getBandDescription(int i){
        switch (i) {
            case 0:
                return getString(R.string.hp_eq_band1_description);
            case 1:
                return getString(R.string.hp_eq_band2_description);
            case 2:
                return getString(R.string.hp_eq_band3_description);
            case 3:
                return getString(R.string.hp_eq_band4_description);
            case 4:
                return getString(R.string.hp_eq_band5_description);
        }
        return "";
    }

}
