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

package com.grarak.kerneladiutor.fragments.kernel;

import android.os.Bundle;

import com.grarak.kerneladiutor.R;
import com.grarak.kerneladiutor.elements.DDivider;
import com.grarak.kerneladiutor.elements.cards.CardViewItem;
import com.grarak.kerneladiutor.elements.cards.PopupCardView;
import com.grarak.kerneladiutor.elements.cards.SeekBarCardView;
import com.grarak.kerneladiutor.elements.cards.SwitchCardView;
import com.grarak.kerneladiutor.fragments.RecyclerViewFragment;
import com.grarak.kerneladiutor.utils.kernel.GPU;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willi on 26.12.14.
 */
public class GPUFragment extends RecyclerViewFragment implements PopupCardView.DPopupCard.OnDPopupCardListener
{

    private CardViewItem.DCardView mCurFreqCard;

    private PopupCardView.DPopupCard mMaxFreqCard;

    private PopupCardView.DPopupCard mThrottling5card, mThrottling4card, mThrottling3card, mThrottling2card, mThrottling1card;

    private PopupCardView.DPopupCard mMinFreqCard;

    private PopupCardView.DPopupCard mGovernorCard;

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        curFreqInit();
        maxFreqInit();
        minFreqInit();
        governorInit();
        throttlingInit();

    }

    private void curFreqInit() {

            mCurFreqCard = new CardViewItem.DCardView();
            mCurFreqCard.setTitle(getString(R.string.gpu_cur_freq));

            addView(mCurFreqCard);
    }

    private void throttlingInit() {

            List<String> freqs = new ArrayList<>();
              for (int i = 0; i< GPU.getGpuFreqs().size() - 1; i++ ) {
                freqs.add(GPU.getGpuFreqs().get(i) + getString(R.string.mhz));
            }
            mThrottling1card = new PopupCardView.DPopupCard(freqs);
            mThrottling1card.setTitle(getString(R.string.gpu_thermal_tittle) + " 1");
            mThrottling1card.setDescription(getString(R.string.gpu_thermal_description) + "85° C");
            mThrottling1card.setItem(GPU.getGpuThrottling(4) + getString(R.string.mhz));
            mThrottling1card.setOnDPopupCardListener(this);

            addView(mThrottling1card);

            mThrottling2card = new PopupCardView.DPopupCard(freqs);
            mThrottling2card.setTitle(getString(R.string.gpu_thermal_tittle) + " 2");
            mThrottling2card.setDescription(getString(R.string.gpu_thermal_description) + "90° C");
            mThrottling2card.setItem(GPU.getGpuThrottling(3) + getString(R.string.mhz));
            mThrottling2card.setOnDPopupCardListener(this);

            addView(mThrottling2card);

            mThrottling3card = new PopupCardView.DPopupCard(freqs);
            mThrottling3card.setTitle(getString(R.string.gpu_thermal_tittle) + " 3");
            mThrottling3card.setDescription(getString(R.string.gpu_thermal_description) + "95° C");
            mThrottling3card.setItem(GPU.getGpuThrottling(2) + getString(R.string.mhz));
            mThrottling3card.setOnDPopupCardListener(this);

            addView(mThrottling3card);

            mThrottling4card = new PopupCardView.DPopupCard(freqs);
            mThrottling4card.setTitle(getString(R.string.gpu_thermal_tittle) + " 4");
            mThrottling4card.setDescription(getString(R.string.gpu_thermal_description) + "100° C");
            mThrottling4card.setItem(GPU.getGpuThrottling(1) + getString(R.string.mhz));
            mThrottling4card.setOnDPopupCardListener(this);

            addView(mThrottling4card);

            mThrottling5card = new PopupCardView.DPopupCard(freqs);
            mThrottling5card.setTitle(getString(R.string.gpu_thermal_tittle) + " 5 (Tripping)");
            mThrottling5card.setDescription(getString(R.string.gpu_thermal_description) + "105° C");
            mThrottling5card.setItem(GPU.getGpuThrottling(0) + getString(R.string.mhz));
            mThrottling5card.setOnDPopupCardListener(this);

            addView(mThrottling5card);

    }

    private void maxFreqInit() {

            List<String> freqs = new ArrayList<>();
            for (int freq : GPU.getGpuFreqs()) {
                freqs.add(freq + getString(R.string.mhz));
            }

            mMaxFreqCard = new PopupCardView.DPopupCard(freqs);
            mMaxFreqCard.setTitle(getString(R.string.gpu_max_freq));
            mMaxFreqCard.setDescription(getString(R.string.gpu_max_freq_summary));
            mMaxFreqCard.setItem(GPU.getGpuMaxFreq() + getString(R.string.mhz));
            mMaxFreqCard.setOnDPopupCardListener(this);

            addView(mMaxFreqCard);
    }

    private void minFreqInit() {
            List<String> freqs = new ArrayList<>();
            for (int freq : GPU.getGpuFreqs()) {
                freqs.add(freq + getString(R.string.mhz));
            }

            mMinFreqCard = new PopupCardView.DPopupCard(freqs);
            mMinFreqCard.setTitle(getString(R.string.gpu_min_freq));
            mMinFreqCard.setDescription(getString(R.string.gpu_min_freq_summary));
            mMinFreqCard.setItem(GPU.getGpuMinFreq() + getString(R.string.mhz));
            mMinFreqCard.setOnDPopupCardListener(this);

            addView(mMinFreqCard);
    }

    private void governorInit() {

        if (GPU.hasGpuGovernor()) {
            mGovernorCard = new PopupCardView.DPopupCard(GPU.getGpuGovernors());
            mGovernorCard.setTitle(getString(R.string.gpu_governor));
            mGovernorCard.setDescription(getString(R.string.gpu_governor_summary));
            mGovernorCard.setItem(GPU.getGpuGovernor());
            mGovernorCard.setOnDPopupCardListener(this);

            addView(mGovernorCard);
        }
    }


    @Override
    public void onItemSelected(PopupCardView.DPopupCard dPopupCard, int position) {
        if (dPopupCard == mMaxFreqCard)
            GPU.setGpuMaxFreq(GPU.getGpuFreqs().get(position), getActivity());
        else if (dPopupCard == mMinFreqCard)
            GPU.setGpuMinFreq(GPU.getGpuFreqs().get(position), getActivity());
        else if (dPopupCard == mGovernorCard)
            GPU.setGpuGovernor(GPU.getGpuGovernors().get(position), getActivity());
        else if (dPopupCard ==  mThrottling1card)
            GPU.setGpuThrottling(GPU.getGpuFreqs().get(position), 4, getActivity());
        else if (dPopupCard ==  mThrottling2card)
            GPU.setGpuThrottling(GPU.getGpuFreqs().get(position), 3, getActivity());
        else if (dPopupCard ==  mThrottling3card)
            GPU.setGpuThrottling(GPU.getGpuFreqs().get(position), 2, getActivity());
        else if (dPopupCard ==  mThrottling4card)
            GPU.setGpuThrottling(GPU.getGpuFreqs().get(position), 1, getActivity());
        else if (dPopupCard ==  mThrottling5card)
            GPU.setGpuThrottling(GPU.getGpuFreqs().get(position), 0, getActivity());
    }

    @Override
    public boolean onRefresh() {

        if (mCurFreqCard != null)
            mCurFreqCard.setDescription((GPU.getGpuCurFreq()) + getString(R.string.mhz));

        return true;
    }
}