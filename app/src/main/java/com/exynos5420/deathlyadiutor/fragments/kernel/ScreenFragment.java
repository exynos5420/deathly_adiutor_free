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

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.os.Handler;

import com.exynos5420.deathlyadiutor.R;
import com.exynos5420.deathlyadiutor.elements.ColorPalette;
import com.exynos5420.deathlyadiutor.elements.cards.PopupCardView;
import com.exynos5420.deathlyadiutor.elements.cards.SwitchCardView;
import com.exynos5420.deathlyadiutor.fragments.RecyclerViewFragment;
import com.exynos5420.deathlyadiutor.utils.Constants;

import com.exynos5420.deathlyadiutor.utils.Utils;

import com.exynos5420.deathlyadiutor.utils.kernel.Screen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willi on 26.12.14.
 */
public class ScreenFragment extends RecyclerViewFragment implements SwitchCardView.DSwitchCard.OnDSwitchCardListener,
        PopupCardView.DPopupCard.OnDPopupCardListener{

    private ColorPalette mColorPalette;

    private SwitchCardView.DSwitchCard mPowerReduceCard, mGloveModeCard, mepenSavingMode;
    private PopupCardView.DPopupCard mMdnieMode;
    List<String> modes = new ArrayList<>();

    @Override
    public RecyclerView getRecyclerView() {
        mColorPalette = (ColorPalette) getParentView(R.layout.screen_fragment).findViewById(R.id.colorpalette);
        mColorPalette.setVisibility(View.INVISIBLE);
        return (RecyclerView) getParentView(R.layout.screen_fragment).findViewById(R.id.recycler_view);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        mdnieModeInit();
        if (!Utils.hasCMSDK()) PowerReduceInit();
        gloveModeInit();
        if (Utils.existFile(Constants.EPEN_SAVING_MODE)) epenSavingModeInit();
    }

    @Override
    public void postInit(Bundle savedInstanceState) {
        super.postInit(savedInstanceState);
        Utils.circleAnimate(mColorPalette, 0, mColorPalette.getHeight());
    }
    
    private void mdnieModeInit(){
        modes.clear();
        for (int i = 0; i<Screen.mdnie_modes.length; i++)
            modes.add(Screen.mdnie_modes[i]);

        mMdnieMode = new PopupCardView.DPopupCard(modes);
        mMdnieMode.setTitle(getString(R.string.mdnie_mode));
        mMdnieMode.setDescription(getmdnieDescription());
        mMdnieMode.setItem(Screen.getmdnieMode());
        mMdnieMode.setOnDPopupCardListener(this);

        addView(mMdnieMode);
    }
    
    private void PowerReduceInit() {
        mPowerReduceCard = new SwitchCardView.DSwitchCard();
        mPowerReduceCard.setTitle(getString(R.string.power_reduce));
        mPowerReduceCard.setDescription(getString(R.string.power_reduce_summary));
        mPowerReduceCard.setChecked(Screen.isPowerReduceActive());
        mPowerReduceCard.setOnDSwitchCardListener(this);

        addView(mPowerReduceCard);
    }

    private void gloveModeInit() {
        mGloveModeCard = new SwitchCardView.DSwitchCard();
        mGloveModeCard.setTitle(getString(R.string.glove_mode));
        mGloveModeCard.setDescription(getString(R.string.glove_mode_summary));
        mGloveModeCard.setChecked(Screen.isGloveModeActive());
        mGloveModeCard.setOnDSwitchCardListener(this);

        addView(mGloveModeCard);
    }

    private void epenSavingModeInit() {
        mepenSavingMode = new SwitchCardView.DSwitchCard();
        mepenSavingMode.setTitle(getString(R.string.epen_saving_mode));
        mepenSavingMode.setDescription(getString(R.string.epen_saving_mode_summary));
        mepenSavingMode.setChecked(Screen.isepenSavingModeActive());
        mepenSavingMode.setOnDSwitchCardListener(this);

        addView(mepenSavingMode);
    }

    @Override
    public void onChecked(SwitchCardView.DSwitchCard dSwitchCard, boolean checked) {
        if (dSwitchCard == mGloveModeCard)
            Screen.activateGloveMode(checked, getActivity());
        else if (dSwitchCard == mPowerReduceCard)
            Screen.activatePowerReduce(checked, getActivity());
        else if (dSwitchCard == mepenSavingMode)
            Screen.activatepenSavingMode(checked, getActivity());
    }

    @Override
    public void onItemSelected(PopupCardView.DPopupCard dPopupCard, int position) {
        Handler handler = new Handler();
        if (dPopupCard == mMdnieMode) {
            Screen.setmdnieMode(modes.get(position), getActivity());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mMdnieMode.setDescription(getmdnieDescription());
                }
            }, 500);
        }
    }

    private String getmdnieDescription(){
        switch (Screen.getmdnieMode()) {
            case "Dynamic":
                return getString(R.string.mdnie_mode_Dynamic);
            case "Standard":
                return getString(R.string.mdnie_mode_Standard);
            case "Natural":
                return getString(R.string.mdnie_mode_Natural);
            case "Cinema":
                return getString(R.string.mdnie_mode_Cinema);
            case "Adaptative":
                return getString(R.string.mdnie_mode_Adaptative);
        }
        return "";
    }

}