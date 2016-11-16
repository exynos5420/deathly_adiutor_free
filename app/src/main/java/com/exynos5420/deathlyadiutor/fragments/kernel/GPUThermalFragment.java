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

package com.exynos5420.deathlyadiutor.fragments.kernel;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Toast;

import com.exynos5420.deathlyadiutor.R;
import com.exynos5420.deathlyadiutor.elements.DDivider;
import com.exynos5420.deathlyadiutor.elements.cards.EditTextCardView;
import com.exynos5420.deathlyadiutor.elements.cards.PopupCardView;
import com.exynos5420.deathlyadiutor.fragments.RecyclerViewFragment;
import com.exynos5420.deathlyadiutor.utils.Constants;
import com.exynos5420.deathlyadiutor.utils.Utils;
import com.exynos5420.deathlyadiutor.utils.kernel.GPU;
import com.exynos5420.deathlyadiutor.utils.kernel.GPUThermal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin Bouchet on 15.11.16.
 */
public class GPUThermalFragment extends RecyclerViewFragment implements PopupCardView.DPopupCard.OnDPopupCardListener {

    PopupCardView.DPopupCard[] mFreqCard = new PopupCardView.DPopupCard[Constants.GPU_THRORRLING_FREQS_ARRAY.length];


    @Override
    public int getSpan() {
        int orientation = Utils.getScreenOrientation(getActivity());
        if (Utils.isTablet(getActivity()))
            return orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 2;
        return orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 2;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        GPUThermalInit();
    }

    private void GPUThermalInit(){
        final EditTextCardView.DEditTextCard[] mTempCard = new EditTextCardView.DEditTextCard[Constants.GPU_THRORRLING_TEMPS_ARRAY.length];
        final List<String> availibleGPUfreqs = new ArrayList<>();
        for (int i = 0; i < GPU.getGpuFreqs().size(); i++) {
            availibleGPUfreqs.add(GPU.getGpuFreqs().get(i) + getString(R.string.mhz));
        }

        for (int i = 0; i < Constants.GPU_THRORRLING_TEMPS_ARRAY.length; i++){
            DDivider mTripPoint = new DDivider();
            mTripPoint.setText(getString(R.string.throttling_point) + " " + (i+1));

            addView(mTripPoint);

            mTempCard[i] = new EditTextCardView.DEditTextCard();
            mTempCard[i].setTitle(getString(R.string.temperature));
            mTempCard[i].setDescription(Integer.toString(GPUThermal.getTemp(i)) + getString(R.string.degrees_celcius));
            mTempCard[i].setValue(Integer.toString(GPUThermal.getTemp(i)));
            mTempCard[i].setInputType(InputType.TYPE_CLASS_NUMBER);
            mTempCard[i].setOnDEditTextCardListener(new EditTextCardView.DEditTextCard.OnDEditTextCardListener() {
                @Override
                public void onApply(EditTextCardView.DEditTextCard dEditTextCard, String value) {
                    for (int i = 0; i < mTempCard.length; i++) {
                        if (dEditTextCard == mTempCard[i]) {
                            if (i != mTempCard.length - 1 && Utils.stringToInt(value) > GPUThermal.getTemp(i + 1)) {
                                Utils.toast(getString(R.string.temp_higher_than_next), getActivity(), Toast.LENGTH_LONG);
                                dEditTextCard.setDescription(GPUThermal.getTemp(i) + getString(R.string.degrees_celcius));
                                mTempCard[i].setValue(Integer.toString(GPUThermal.getTemp(i)));
                            } else if (i != 0 && Utils.stringToInt(value) < GPUThermal.getTemp(i - 1)) {
                                Utils.toast(getString(R.string.temp_lower_than_previous), getActivity(), Toast.LENGTH_LONG);
                                dEditTextCard.setDescription(GPUThermal.getTemp(i) + getString(R.string.degrees_celcius));
                                mTempCard[i].setValue(Integer.toString(GPUThermal.getTemp(i)));
                            } else {
                                GPUThermal.setGPUTripPointTemp(Utils.stringToInt(value), i, getActivity());
                                dEditTextCard.setDescription(value + getString(R.string.degrees_celcius));
                            }
                        }
                    }
                }
            });

            addView(mTempCard[i]);

            mFreqCard[i] = new PopupCardView.DPopupCard(availibleGPUfreqs);
            mFreqCard[i].setTitle(getString(R.string.frequency));
            mFreqCard[i].setItem(GPUThermal.getFreq(i) + getString(R.string.mhz));
            mFreqCard[i].setOnDPopupCardListener(this);

            addView(mFreqCard[i]);
        }
    }

    @Override
    public void onItemSelected(PopupCardView.DPopupCard dPopupCard, int position) {
        for (int i = 0; i < mFreqCard.length; i++) {
            if (dPopupCard == mFreqCard[i]){
                if (i != mFreqCard.length - 1 && GPU.getGpuFreqs().get(position) < GPUThermal.getFreq(i + 1)) {
                    Utils.toast(getString(R.string.freq_lower_than_next), getActivity(), Toast.LENGTH_LONG);
                    mFreqCard[i].setItem(GPUThermal.getFreq(i) + getString(R.string.mhz));
                }else if (i != 0 && GPU.getGpuFreqs().get(position) > GPUThermal.getFreq(i - 1)) {
                    Utils.toast(getString(R.string.freq_higher_than_previous), getActivity(), Toast.LENGTH_LONG);
                    mFreqCard[i].setItem(GPUThermal.getFreq(i) + getString(R.string.mhz));
                } else {
                    GPUThermal.setGPUTripPointFreq(GPU.getGpuFreqs().get(position), i, getActivity());
                    mFreqCard[i].setItem(GPU.getGpuFreqs().get(position) + getString(R.string.mhz));
                }
            }
        }
    }

}