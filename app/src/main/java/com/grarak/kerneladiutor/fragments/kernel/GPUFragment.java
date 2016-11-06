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

    private PopupCardView.DPopupCard mMinFreqCard;

    private PopupCardView.DPopupCard mGovernorCard;

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        curFreqInit();
        maxFreqInit();
        minFreqInit();
        governorInit();

    }

    private void curFreqInit() {

        if (GPU.hasGpuCurFreq()) {
            mCurFreqCard = new CardViewItem.DCardView();
            mCurFreqCard.setTitle(getString(R.string.gpu_cur_freq));

            addView(mCurFreqCard);
        }
    }

    private void maxFreqInit() {

        if (GPU.hasGpuMaxFreq() && GPU.hasGpuFreqs()) {
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
    }

    private void minFreqInit() {
        if (GPU.hasGpuMinFreq() && GPU.hasGpuFreqs()) {
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
    }

    @Override
    public boolean onRefresh() {

        if (mCurFreqCard != null)
            mCurFreqCard.setDescription((GPU.getGpuCurFreq()) + getString(R.string.mhz));

        return true;
    }
}