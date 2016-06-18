//-----------------------------------------------------------------------------
//
// (C) Brandon Valosek, 2011 <bvalosek@gmail.com>
//
//-----------------------------------------------------------------------------
// Modified by Willi Ye to work as Fragment and with with big.LITTLE

package com.grarak.kerneladiutor.fragments.information;

import android.content.res.Configuration;
import android.os.Bundle;

import com.grarak.kerneladiutor.elements.DDivider;
import com.grarak.kerneladiutor.elements.cards.UsageCardView;
import com.grarak.kerneladiutor.fragments.RecyclerViewFragment;
import com.grarak.kerneladiutor.utils.Constants;
import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.kernel.CPU;
import com.bvalosek.cpuspy.CpuSpyApp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * main activity class
 */
public class FrequencyTableFragment extends RecyclerViewFragment implements Constants {



    private UsageCardView.DUsageCard[][] mUsageCard;

    @Override
    public int getSpan() {
        return Utils.getScreenOrientation(getActivity()) == Configuration.ORIENTATION_PORTRAIT ? 1 : 2;
    }

    @Override
    public boolean showApplyOnBoot() {
        return false;
    }

    @Override
    public void preInit(Bundle savedInstanceState) {
        super.preInit(savedInstanceState);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        generateview();
    }

    @Override
    public void postInit(Bundle savedInstanceState) {
        super.postInit(savedInstanceState);
    }

    private void generateview() {
        mUsageCard = new UsageCardView.DUsageCard[CPU.getCoreCount()][CPU.getFreqs().size()] ;
        final String time_in_state_path = Utils.getsysfspath(CPU_TIME_IN_STATE_ARRAY);
        int total_time = 0;

        DDivider[] freqUtilization = new DDivider[CPU.getCoreCount()];
        for (int i = 0; i < CPU.getCoreCount(); i++) {
            Map<String, String> freq_use_list = new HashMap<>();
            // Fill a hashmap with the info
            try {
            // Code to readlines borrowed from StackOverflow: http://stackoverflow.com/questions/7175161/how-to-get-file-read-line-by-line
                InputStream instream = new FileInputStream(String.format(time_in_state_path, i));

                if (instream != null) {
                    freqUtilization[i].setText("Core: " + i);
                    addView(freqUtilization[i]);
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    do {
                        line = buffreader.readLine();
                        total_time = total_time + Integer.parseInt(line.split(" ")[1]);
                        freq_use_list.put(line.split(" ")[0], String.valueOf(line.split(" ")[1]));
                    } while (line != null);
                }
            } catch (Exception ex) {
                // I don't care about this exception...
            }
            // All States/Times should be loaded for this core at this point
            String[] freq_use_array = freq_use_list.values().toArray(new String[freq_use_list.size()]);

            for (int x = 0; x < freq_use_array.length; x++) {
                mUsageCard[i][x].setText("" + Utils.stringToInt(freq_use_array[x])/1000);
                mUsageCard[i][x].setProgress(total_time / Utils.stringToInt(freq_use_array[x]));
                addView(mUsageCard[i][x]);
            }
        }

    }

    /**
     * @return A nicely formatted String representing tSec seconds
     */
    private static String sToString(long tSec) {
        long h = (long) Math.floor(tSec / (60 * 60));
        long m = (long) Math.floor((tSec - h * 60 * 60) / 60);
        long s = tSec % 60;
        String sDur;
        sDur = h + ":";
        if (m < 10) sDur += "0";
        sDur += m + ":";
        if (s < 10) sDur += "0";
        sDur += s;

        return sDur;
    }

}
