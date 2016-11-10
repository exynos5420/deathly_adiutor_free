package com.exynos5420.deathlyadiutor.fragments.kernel;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.exynos5420.deathlyadiutor.R;
import com.exynos5420.deathlyadiutor.elements.cards.EditTextCardView;
import com.exynos5420.deathlyadiutor.fragments.RecyclerViewFragment;
import com.exynos5420.deathlyadiutor.utils.Constants;
import com.exynos5420.deathlyadiutor.utils.Utils;
import com.exynos5420.deathlyadiutor.utils.kernel.GPUVoltage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tinch on 9/11/2016.
 */

public class GPUVoltageFragment extends RecyclerViewFragment {

    private EditTextCardView.DEditTextCard[] mVoltageCard;
    Map<String, String> voltagetable = new HashMap<String, String>();

    @Override
    public int getSpan() {
        int orientation = Utils.getScreenOrientation(getActivity());
        if (Utils.isTablet(getActivity()))
            return orientation == Configuration.ORIENTATION_LANDSCAPE ? 6 : 5;
        return orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 2;
    }

    @Override
    public void preInit(Bundle savedInstanceState) {
        super.preInit(savedInstanceState);
        SharedPreferences storedvoltagetable = getContext().getSharedPreferences("gpu_voltage_table", 0);
        // Save the current Voltage table if it doesn't exist. This will prevent issues in the table if they open it before a reboot.
        // On reboot, the default table will overwrite this as it will have any adjustments done since boot as the reference. This is imperfect, but no better way to do it.
        String toasttext = "";
        List<String> frequencies = GPUVoltage.getFreqs();
        if (frequencies.isEmpty()) return;

        if (storedvoltagetable.getString(frequencies.get(0), "-1").equals("-1")) {
            toasttext = getString(R.string.non_default_reference) + " -- ";
            GPUVoltage.storeVoltageTable(getContext());
        }
        Utils.toast(toasttext + getString(R.string.voltages_toast_notification), getActivity(), Toast.LENGTH_LONG);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        SharedPreferences storedvoltagetable = getContext().getSharedPreferences("gpu_voltage_table", 0);
        for( Map.Entry entry : storedvoltagetable.getAll().entrySet() )
            voltagetable.put( entry.getKey().toString(), entry.getValue().toString() );

        Log.i(Constants.TAG, "Volt Table: " + voltagetable);

        mVoltageCard = new EditTextCardView.DEditTextCard[GPUVoltage.getFreqs().size()];
        List<String> voltages = GPUVoltage.getVoltages();
        List<String> frequencies = GPUVoltage.getFreqs();

        if (voltages.isEmpty()) return;

        for (int i = 0; i < frequencies.size(); i++) {
            mVoltageCard[i] = new EditTextCardView.DEditTextCard();
            String freq = String.valueOf(Utils.stringToInt(frequencies.get(i)));
            mVoltageCard[i].setTitle(freq + getString(R.string.mhz));

            if (voltagetable.get(frequencies.get(i)) != null) {
                double stock = Double.parseDouble(voltagetable.get(frequencies.get(i)));
                double current = Double.parseDouble(voltages.get(i));
                String diff;
                if (stock > current) {
                    diff =  "(-" + Double.toString((stock-current) / 1000) +")";
                } else if (stock < current) {
                    diff = "(+" + Double.toString((current-stock) / 1000) + ")";
                }
                else {
                    diff = "";
                }
                mVoltageCard[i].setDescription((Utils.stringtodouble(voltages.get(i)) / 1000) + getString(R.string.mv) + diff);
            } else {
                mVoltageCard[i].setDescription((Utils.stringtodouble(voltages.get(i)) / 1000) + getString(R.string.mv));
            }
            mVoltageCard[i].setValue(Double.toString(Utils.stringtodouble(voltages.get(i))/ 1000));
            mVoltageCard[i].setInputType(InputType.TYPE_CLASS_NUMBER);
            mVoltageCard[i].setOnDEditTextCardListener(new EditTextCardView.DEditTextCard.OnDEditTextCardListener() {
                @Override
                public void onApply(EditTextCardView.DEditTextCard dEditTextCard, String value) {
                    List<String> freqs = GPUVoltage.getFreqs();

                    for (int i = 0; i < mVoltageCard.length; i++)
                        if (dEditTextCard == mVoltageCard[i])
                            GPUVoltage.setVoltage(freqs.get(i), Double.toString(Utils.stringtodouble(value) * 1000), getActivity());

                    dEditTextCard.setDescription(value + getString(R.string.mv));
                    refresh();
                }
            });

            addView(mVoltageCard[i]);
        }

    }

    private void refresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            List<String> voltages = GPUVoltage.getVoltages();
                            if (voltages != null)
                                for (int i = 0; i < mVoltageCard.length; i++) {
                                    try {
                                        if (voltagetable.get(GPUVoltage.getFreqs().get(i)) != null) {
                                            double stock = Double.parseDouble(voltagetable.get(GPUVoltage.getFreqs().get(i)));
                                            double current = Double.parseDouble(voltages.get(i));
                                            String diff;
                                            if (stock > current) {
                                                diff =  "(-" + Double.toString((stock-current) / 1000) +")";
                                            } else if (stock < current) {
                                                diff = "(+" + Double.toString((current-stock) / 1000) + ")";
                                            }
                                            else {
                                                diff = "";
                                            }
                                            mVoltageCard[i].setDescription((Utils.stringtodouble(voltages.get(i)) / 1000) + getString(R.string.mv) + diff);
                                        } else {
                                            mVoltageCard[i].setDescription((Utils.stringtodouble(voltages.get(i)) / 1000) + getString(R.string.mv));
                                        }
                                        mVoltageCard[i].setValue(Double.toString(Utils.stringtodouble(voltages.get(i))/ 1000));
                                    } catch (IndexOutOfBoundsException e) {
                                        e.printStackTrace();
                                    }
                                }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.cpu_voltage_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.global_offset:

                View view = inflater.inflate(R.layout.global_offset_view, container, false);

                final TextView textView = (TextView) view.findViewById(R.id.offset_text);
                if (Utils.DARKTHEME)
                    textView.setTextColor(getResources().getColor(R.color.textcolor_dark));
                textView.setText("0");

                AppCompatButton minus = (AppCompatButton) view.findViewById(R.id.button_minus);
                if (Utils.DARKTHEME)
                    minus.setTextColor(getResources().getColor(R.color.textcolor_dark));
                minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            textView.setText(String.valueOf(Utils.stringtodouble(textView.getText().toString()) - 6.25));
                        } catch (NumberFormatException e) {
                            textView.setText("0");
                        }
                    }
                });

                AppCompatButton plus = (AppCompatButton) view.findViewById(R.id.button_plus);
                if (Utils.DARKTHEME)
                    plus.setTextColor(getResources().getColor(R.color.textcolor_dark));
                plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            textView.setText(String.valueOf(Utils.stringtodouble(textView.getText().toString()) + 6.25));
                        } catch (NumberFormatException e) {
                            textView.setText("0");
                        }
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(view)
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GPUVoltage.setGlobalOffset(textView.getText().toString(), getActivity());

                        refresh();
                    }
                }).show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
