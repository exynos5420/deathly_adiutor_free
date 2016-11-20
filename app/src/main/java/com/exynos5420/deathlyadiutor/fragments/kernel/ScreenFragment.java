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

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.exynos5420.deathlyadiutor.R;
import com.exynos5420.deathlyadiutor.elements.ColorPalette;
import com.exynos5420.deathlyadiutor.elements.cards.SwitchCardView;
import com.exynos5420.deathlyadiutor.fragments.RecyclerViewFragment;

import com.exynos5420.deathlyadiutor.utils.Utils;

import com.exynos5420.deathlyadiutor.utils.kernel.Screen;

/**
 * Created by willi on 26.12.14.
 */
public class ScreenFragment extends RecyclerViewFragment implements SwitchCardView.DSwitchCard.OnDSwitchCardListener {

    private ColorPalette mColorPalette;

    private SwitchCardView.DSwitchCard mGloveModeCard;

    @Override
    public RecyclerView getRecyclerView() {
        mColorPalette = (ColorPalette) getParentView(R.layout.screen_fragment).findViewById(R.id.colorpalette);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mColorPalette.setVisibility(View.INVISIBLE);
        return (RecyclerView) getParentView(R.layout.screen_fragment).findViewById(R.id.recycler_view);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        if (Screen.hasGloveMode()) gloveModeInit();
    }

    @Override
    public void postInit(Bundle savedInstanceState) {
        super.postInit(savedInstanceState);
        Utils.circleAnimate(mColorPalette, 0, mColorPalette.getHeight());
    }


    private void gloveModeInit() {
        mGloveModeCard = new SwitchCardView.DSwitchCard();
        mGloveModeCard.setTitle(getString(R.string.glove_mode));
        mGloveModeCard.setDescription(getString(R.string.glove_mode_summary));
        mGloveModeCard.setChecked(Screen.isGloveModeActive());
        mGloveModeCard.setOnDSwitchCardListener(this);

        addView(mGloveModeCard);
    }

    @Override
    public void onChecked(SwitchCardView.DSwitchCard dSwitchCard, boolean checked) {
        if (dSwitchCard == mGloveModeCard)
            Screen.activateGloveMode(checked, getActivity());
    }

}