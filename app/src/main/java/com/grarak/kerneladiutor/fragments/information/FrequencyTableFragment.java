package com.grarak.kerneladiutor.fragments.information;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.grarak.kerneladiutor.elements.DDivider;
import com.grarak.kerneladiutor.elements.cards.UsageCardView;
import com.grarak.kerneladiutor.fragments.RecyclerViewFragment;
import com.grarak.kerneladiutor.utils.Constants;
import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.kernel.CPU;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        generateview();
    }

    private void generateview() {
        mUsageCard = new UsageCardView.DUsageCard[CPU.getCoreCount()][CPU.getFreqs().size()] ;
        double total_time = 0;

        DDivider muptimeCard = new DDivider();
        muptimeCard.setText("Uptime: " + getDurationBreakdown(SystemClock.elapsedRealtime()));
        addView(muptimeCard);

        DDivider mAwakeTime = new DDivider();
        mAwakeTime.setText("Awake Time: " + getDurationBreakdown(SystemClock.uptimeMillis()));
        addView(mAwakeTime);

        DDivider mDeepSleepCard = new DDivider();
        mDeepSleepCard.setText("Deep Sleep: " + getDurationBreakdown(SystemClock.elapsedRealtime() - SystemClock.uptimeMillis()));
        addView(mDeepSleepCard);

        DDivider[] freqUtilization = new DDivider[CPU.getCoreCount()];
        for (int i = 0; i < CPU.getCoreCount(); i++) {
            // <Freq, time>
            Map<String, String> freq_use_list = new HashMap<>();
            try {
                BufferedReader buffreader = new BufferedReader(new InputStreamReader(new FileInputStream(Utils.getsysfspath(CPU_TIME_IN_STATE_ARRAY, i))));
                if (buffreader != null) {

                    String line;
                    do {
                        line = buffreader.readLine();
                        total_time = total_time + Integer.parseInt(line.split(" ")[1]);
                        freq_use_list.put(line.split(" ")[0], line.split(" ")[1]);
                    } while (line != null);
                    buffreader.close();
                }
            } catch (Exception ex) {
                // I don't really care about this exception...
            }
            List<Integer> allfreqs = CPU.getFreqs();

            freqUtilization[i] = new DDivider();
            freqUtilization[i].setText("Core: " + i);
            addView(freqUtilization[i]);
            for (int x = 0; x < freq_use_list.size(); x++) {
                double freq_time = (double)Utils.stringToInt(freq_use_list.get(Integer.toString(allfreqs.get(x))));
                mUsageCard[i][x] = new UsageCardView.DUsageCard();
                mUsageCard[i][x].setText(allfreqs.get(x) / 1000 + "Mhz");
                double pct = Math.round(freq_time / total_time * 100);
                Log.i(TAG, "Core: " + i + " Freq: " + allfreqs.get(x) + " Freq_Time: " + freq_time + " Total Time: " + total_time + " PCT: " + pct);
                if (freq_time != 0) {
                    mUsageCard[i][x].setProgress((int)pct);
                }
                addView(mUsageCard[i][x]);

            }
        }

    }

    /**
     * Convert a millisecond duration to a string format
     *
     * @param millis A duration to convert to a string form
     * @return A string of the form "X Days Y Hours Z Minutes A Seconds".
     *
     * Function modified from answer here: http://stackoverflow.com/questions/625433/how-to-convert-milliseconds-to-x-mins-x-seconds-in-java
     */
    public static String getDurationBreakdown(long millis)
    {
        if(millis < 0)
        {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(64);
        if (days > 0 ) {
            sb.append(days);
            sb.append("D ");
        }
        if (hours > 0) {
            sb.append(hours);
            sb.append("H ");
        }
        if (minutes > 0 ) {
            sb.append(minutes);
            sb.append("M ");
        }
        if (seconds > 0) {
            sb.append(seconds);
            sb.append("S ");
        }

        return(sb.toString());
    }

}
