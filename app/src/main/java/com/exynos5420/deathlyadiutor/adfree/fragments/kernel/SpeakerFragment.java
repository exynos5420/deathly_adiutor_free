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
    private SwitchCardView.DSwitchCard mEQenabled;
    private SeekBarCardView.DSeekBarCard[] mEQgains = new SeekBarCardView.DSeekBarCard[5];

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        if (Sound.hasEarpiece()) EarpieceInit();
        SpeakerGainLeftInit();
        SpeakerGainRightInit();
        addDivider("Speaker Equalizer");
        switchEQonoff();
        SpeakerEQgainsInit();

    }

    private void EarpieceInit(){
        List<String> list_earpiece_volumes = new ArrayList<>();
        for (int i = -8; i < 17; i++){
            list_earpiece_volumes.add(i + "dB");
            if (i!= 16) list_earpiece_volumes.add((i + 0.5) + "dB");
        }
        mEarpieceVolume = new SeekBarCardView.DSeekBarCard(list_earpiece_volumes);
        mEarpieceVolume.setTitle("Earpiece Volume");
        mEarpieceVolume.setDescription("Sets the Earpiece volume.");
        mEarpieceVolume.setProgress(Utils.stringToInt(Sound.getEarpieceVolume()));
        mEarpieceVolume.setOnDSeekBarCardListener(this);

        addView(mEarpieceVolume);
    }

    private void SpeakerGainLeftInit(){
        List<String> list_speaker_volumes = new ArrayList<>();
        for (int i = -8; i < 17; i++){
            list_speaker_volumes.add(i + "dB");
            if (i!= 16) list_speaker_volumes.add((i + 0.5) + "dB");
        }
        mSpeakerGainLeft = new SeekBarCardView.DSeekBarCard(list_speaker_volumes);
        mSpeakerGainLeft.setTitle("Left Speaker Gain");
        mSpeakerGainLeft.setDescription("Sets the left speaker gain.");
        mSpeakerGainLeft.setProgress(Utils.stringToInt(Sound.getSpeakerGainLeft()));
        mSpeakerGainLeft.setOnDSeekBarCardListener(this);

        addView(mSpeakerGainLeft);
    }

    private void SpeakerGainRightInit(){
        List<String> list_speaker_volumes = new ArrayList<>();
        for (int i = -8; i < 17; i++){
            list_speaker_volumes.add(i + "dB");
            if (i!= 16) list_speaker_volumes.add((i + 0.5) + "dB");
        }
        mSpeakerGainRight = new SeekBarCardView.DSeekBarCard(list_speaker_volumes);
        mSpeakerGainRight.setTitle("Right Speaker Gain");
        mSpeakerGainRight.setDescription("Right the left speaker gain.");
        mSpeakerGainRight.setProgress(Utils.stringToInt(Sound.getSpeakerGainRight()));
        mSpeakerGainRight.setOnDSeekBarCardListener(this);

        addView(mSpeakerGainRight);
    }

    private void switchEQonoff(){
        mEQenabled = new SwitchCardView.DSwitchCard();
        mEQenabled.setTitle("Speaker Equalizer");
        mEQenabled.setDescription("Enables or disables Speaker Equalizer.");
        mEQenabled.setChecked(Sound.isSpeakerEqEnabled());
        mEQenabled.setOnDSwitchCardListener(this);

        addView(mEQenabled);
    }

    private void SpeakerEQgainsInit(){
        List<String> list_eq_gains = new ArrayList<>();
        for (int i = -12; i < 13; i++) {
            list_eq_gains.add(i + "dB");
            if (i != 12) list_eq_gains.add((i + 0.5) + "dB");
        }
        for (int i = 0; i < 5; i++){
            mEQgains[i] = new SeekBarCardView.DSeekBarCard(list_eq_gains);
            mEQgains[i].setTitle("Equalizer Band " + (i+1));
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
                return "Cutoff-frequency: 160Hz";
            case 1:
                return "Center-frequency: 500Hz, bandwidth: 1050Hz";
            case 2:
                return "Center-frequency: 2800Hz, bandwidth: 2200Hz";
            case 3:
                return "Center-frequency: 7600Hz, bandwidth: 4500Hz";
            case 4:
                return "Center-frequency: 7600Hz, bandwidth: 4500Hz";
        }
        return "";
    }

}
