package com.exynos5420.deathlyadiutor.ads.fragments.other;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.exynos5420.deathlyadiutor.ads.fragments.RecyclerViewFragment;

/**
 * Created by tinch on 30/11/2016.
 */

public class HateAdsFragment extends RecyclerViewFragment {

    @Override
    public boolean showApplyOnBoot() {
        return false;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.exynos5420.deathlyadiutor.adfree")));
        } catch (ActivityNotFoundException ignored) {
        }
    }

}
