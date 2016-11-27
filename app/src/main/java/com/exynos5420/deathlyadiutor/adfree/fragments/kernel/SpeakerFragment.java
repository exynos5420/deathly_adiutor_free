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

package com.exynos5420.deathlyadiutor.fragments.kernel;

import android.os.Bundle;
import android.os.Handler;

import com.exynos5420.deathlyadiutor.R;
import com.exynos5420.deathlyadiutor.elements.DDivider;
import com.exynos5420.deathlyadiutor.elements.cards.SeekBarCardView;
import com.exynos5420.deathlyadiutor.elements.cards.SwitchCardView;
import com.exynos5420.deathlyadiutor.fragments.RecyclerViewFragment;
import com.exynos5420.deathlyadiutor.utils.Constants;
import com.exynos5420.deathlyadiutor.utils.Utils;
import com.exynos5420.deathlyadiutor.utils.kernel.Sound;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin Bouchet on 21.11.16.
 */
public class SpeakerFragment extends RecyclerViewFragment implements Constants, SwitchCardView.DSwitchCard.OnDSwitchCardListener,
        SeekBarCardView.DSeekBarCard.OnDSeekBarCardListener{

    private SeekBarCardView.DSeekBarCard mEarpieceVolume, mSpeakerGainLeft, mSpeakerGainRight;
    private SwitchCardView.DSwitchCard mEQenabled, mPrivacyMode;
    private SeekBarCardView.DSeekBarCard[] mEQgains = new SeekBarCardView.DSeekBarCard[5];

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        if (Sound.hasEarpiece()) EarpieceInit();
        SpeakerGainLeftInit();
        SpeakerGainRightInit();
        switchPrivateModeonoff();
        switchEQonoff();
        addDivider(getString(R.string.speaker_equalizer));
        SpeakerEQgainsInit();

    }

    private void EarpieceInit(){
        List<String> list_earpiece_volumes = new ArrayList<>();
        for (int i = -8; i < 17; i++){
            list_earpiece_volumes.add(i + getString(R.string.dB));
            if (i!= 16) list_earpiece_volumes.add((i + 0.5) + getString(R.string.dB));
        }
        mEarpieceVolume = new SeekBarCardView.DSeekBarCard(list_earpiece_volumes);
        mEarpieceVolume.setTitle(getString(R.string.earpiece_volume));
        mEarpieceVolume.setDescription(getString(R.string.earpiece_volume_summary));
        mEarpieceVolume.setProgress(Utils.stringToInt(Sound.getEarpieceVolume()));
        mEarpieceVolume.setOnDSeekBarCardListener(this);

        addView(mEarpieceVolume);
    }

    private void SpeakerGainLeftInit(){
        List<String> list_speaker_volumes = new ArrayList<>();
        for (int i = -8; i < 17; i++){
            list_speaker_volumes.add(i + getString(R.string.dB));
            if (i!= 16) list_speaker_volumes.add((i + 0.5) + getString(R.string.dB));
        }
        mSpeakerGainLeft = new SeekBarCardView.DSeekBarCard(list_speaker_volumes);
        mSpeakerGainLeft.setTitle(getString(R.string.left_speaker_gain));
        mSpeakerGainLeft.setDescription(getString(R.string.left_speaker_gain_summary));
        mSpeakerGainLeft.setProgress(Utils.stringToInt(Sound.getSpeakerGainLeft()));
        mSpeakerGainLeft.setOnDSeekBarCardListener(this);

        addView(mSpeakerGainLeft);
    }

    private void SpeakerGainRightInit(){
        List<String> list_speaker_volumes = new ArrayList<>();
        for (int i = -8; i < 17; i++){
            list_speaker_volumes.add(i + getString(R.string.dB));
            if (i!= 16) list_speaker_volumes.add((i + 0.5) + getString(R.string.dB));
        }
        mSpeakerGainRight = new SeekBarCardView.DSeekBarCard(list_speaker_volumes);
        mSpeakerGainRight.setTitle(getString(R.string.right_speaker_gain));
        mSpeakerGainRight.setDescription(getString(R.string.right_speaker_gain_summary));
        mSpeakerGainRight.setProgress(Utils.stringToInt(Sound.getSpeakerGainRight()));
        mSpeakerGainRight.setOnDSeekBarCardListener(this);

        addView(mSpeakerGainRight);
    }

    private void switchPrivateModeonoff(){
        mPrivacyMode = new SwitchCardView.DSwitchCard();
        mPrivacyMode.setTitle(getString(R.string.speaker_privacy));
        mPrivacyMode.setDescription(getString(R.string.speaker_privacy_summary));
        mPrivacyMode.setChecked(Sound.isSpeakerPrivacyEnabled());
        mPrivacyMode.setOnDSwitchCardListener(this);

        addView(mPrivacyMode);
    }

    private void switchEQonoff(){
        mEQenabled = new SwitchCardView.DSwitchCard();
        mEQenabled.setTitle(getString(R.string.speaker_equalizer));
        mEQenabled.setDescription(getString(R.string.speaker_equalizer_toggle_summary));
        mEQenabled.setChecked(Sound.isSpeakerEqEnabled());
        mEQenabled.setOnDSwitchCardListener(this);

        addView(mEQenabled);
    }

    private void SpeakerEQgainsInit(){
        List<String> list_eq_gains = new ArrayList<>();
        for (int i = -12; i < 13; i++) {
            list_eq_gains.add(i + getString(R.string.dB));
            if (i != 12) list_eq_gains.add((i + 0.5) + getString(R.string.dB));
        }
        for (int i = 0; i < 5; i++){
            mEQgains[i] = new SeekBarCardView.DSeekBarCard(list_eq_gains);
            mEQgains[i].setTitle(getString(R.string.equalizer_band) + " " + (i+1));
            mEQgains[i].setDescription(getBandDescription(i));
            mEQgains[i].setProgress(Utils.stringToInt(Sound.getSpeakerEqGain(i)));
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
            Sound.switchSpeakerEq(checked, getActivity());
        else if (dSwitchCard == mPrivacyMode) {
            Handler handler = new Handler();
            Sound.switchSpeakerPrivacy(checked, getActivity());
            Sound.switchSpeakerEq(!Sound.isSpeakerEqEnabled(), getActivity());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Sound.switchSpeakerEq(!Sound.isSpeakerEqEnabled(), getActivity());
                }
            }, 500);
        }
    }

    @Override
    public void onChanged(SeekBarCardView.DSeekBarCard dSeekBarCard, int position) {
    }

    @Override
    public void onStop(SeekBarCardView.DSeekBarCard dSeekBarCard, int position) {
        if (dSeekBarCard == mEarpieceVolume)
            Sound.setEarpieceVolume(position, getActivity());
        else if (dSeekBarCard == mSpeakerGainLeft)
            Sound.setSpeakerGainLeft(position, getActivity());
        else if (dSeekBarCard == mSpeakerGainRight)
            Sound.setSpeakerGainRight(position, getActivity());
        else {
            for (int i = 0; i < 5; i++){
                if (dSeekBarCard == mEQgains[i]){
                    Sound.setSpeakerEqGain(position, i, getActivity());
                }
            }
        }
    }

    private String getBandDescription(int i){
        switch (i) {
            case 0:
                return getString(R.string.sp_eq_band1_description);
            case 1:
                return getString(R.string.sp_eq_band2_description);
            case 2:
                return getString(R.string.sp_eq_band3_description);
            case 3:
                return getString(R.string.sp_eq_band4_description);
            case 4:
                return getString(R.string.sp_eq_band5_description);
        }
        return "";
    }

}
