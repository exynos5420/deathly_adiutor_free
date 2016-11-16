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
import com.exynos5420.deathlyadiutor.utils.Utils;
import com.exynos5420.deathlyadiutor.utils.kernel.CPUThermal;
import com.exynos5420.deathlyadiutor.utils.kernel.CPUVoltage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin Bouchet on 15.11.16.
 */
public class CPUThermalFragment extends RecyclerViewFragment implements PopupCardView.DPopupCard.OnDPopupCardListener {

    PopupCardView.DPopupCard[] mFreqCard = new PopupCardView.DPopupCard[CPUThermal.getFreqs().size()];


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

        CPUThermalInit();
    }

    private void CPUThermalInit(){
        final EditTextCardView.DEditTextCard[] mTempCard = new EditTextCardView.DEditTextCard[CPUThermal.getTemps().size()];
        final List<String> availibleCPUfreqs = new ArrayList<>();
        for (int i = 0; i < CPUVoltage.getFreqs().size(); i++) {
            availibleCPUfreqs.add(CPUVoltage.getFreqs().get(i) + getString(R.string.mhz));
        }

        for (int i = 0; i < CPUThermal.getTemps().size(); i++){
            DDivider mTripPoint = new DDivider();
            mTripPoint.setText(getString(R.string.throttling_point) + " " + (i+1));

            addView(mTripPoint);

            mTempCard[i] = new EditTextCardView.DEditTextCard();
            mTempCard[i].setTitle(getString(R.string.temperature));
            mTempCard[i].setDescription(Integer.toString(CPUThermal.getTemps().get(i)) + getString(R.string.degrees_celcius));
            mTempCard[i].setValue(Integer.toString(CPUThermal.getTemps().get(i)));
            mTempCard[i].setInputType(InputType.TYPE_CLASS_NUMBER);
            mTempCard[i].setOnDEditTextCardListener(new EditTextCardView.DEditTextCard.OnDEditTextCardListener() {
                @Override
                public void onApply(EditTextCardView.DEditTextCard dEditTextCard, String value) {
                    for (int i = 0; i < mTempCard.length; i++) {
                        if (dEditTextCard == mTempCard[i]) {
                            if (i != mTempCard.length - 1 && Utils.stringToInt(value) > CPUThermal.getTemps().get(i + 1)) {
                                Utils.toast(getString(R.string.temp_higher_than_next), getActivity(), Toast.LENGTH_LONG);
                                dEditTextCard.setDescription(CPUThermal.getTemps().get(i) + getString(R.string.degrees_celcius));
                                mTempCard[i].setValue(Integer.toString(CPUThermal.getTemps().get(i)));
                            } else if (i != 0 && Utils.stringToInt(value) < CPUThermal.getTemps().get(i - 1)) {
                                Utils.toast(getString(R.string.temp_lower_than_previous), getActivity(), Toast.LENGTH_LONG);
                                dEditTextCard.setDescription(CPUThermal.getTemps().get(i) + getString(R.string.degrees_celcius));
                                mTempCard[i].setValue(Integer.toString(CPUThermal.getTemps().get(i)));
                            } else {
                                CPUThermal.setCPUTripPointTemp(Utils.stringToInt(value), i, getActivity());
                                dEditTextCard.setDescription(value + getString(R.string.degrees_celcius));
                            }
                        }
                    }
                }
            });

            addView(mTempCard[i]);

            mFreqCard[i] = new PopupCardView.DPopupCard(availibleCPUfreqs);
            mFreqCard[i].setTitle(getString(R.string.frequency));
            mFreqCard[i].setDescription(CPUThermal.getFreqs().get(i) + getString(R.string.mhz));
            mFreqCard[i].setOnDPopupCardListener(this);

            addView(mFreqCard[i]);
        }
    }

    @Override
    public void onItemSelected(PopupCardView.DPopupCard dPopupCard, int position) {
        for (int i = 0; i < mFreqCard.length; i++) {
            if (dPopupCard == mFreqCard[i]){
                if (i != mFreqCard.length - 1 && Utils.stringToInt(CPUVoltage.getFreqs().get(position)) < CPUThermal.getFreqs().get(i + 1)) {
                    Utils.toast(getString(R.string.freq_lower_than_next), getActivity(), Toast.LENGTH_LONG);
                    mFreqCard[i].setDescription(CPUThermal.getFreqs().get(i) + getString(R.string.mhz));
                    mFreqCard[i].setItem("");
                }else if (i != 0 && Utils.stringToInt(CPUVoltage.getFreqs().get(position)) > CPUThermal.getFreqs().get(i - 1)) {
                    Utils.toast(getString(R.string.freq_higher_than_previous), getActivity(), Toast.LENGTH_LONG);
                    mFreqCard[i].setDescription(CPUThermal.getFreqs().get(i) + getString(R.string.mhz));
                    mFreqCard[i].setItem("");
                } else {
                    CPUThermal.setCPUTripPointFreq(Utils.stringToInt(CPUVoltage.getFreqs().get(position)), i, getActivity());
                    mFreqCard[i].setDescription(CPUVoltage.getFreqs().get(position) + getString(R.string.mhz));
                    mFreqCard[i].setItem("");
                }
            }
        }
    }

}
