package com.grarak.kerneladiutor.fragments.information;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.grarak.kerneladiutor.R;
import com.grarak.kerneladiutor.elements.cards.CardViewItem;
import com.grarak.kerneladiutor.fragments.RecyclerViewFragment;
import com.grarak.kerneladiutor.utils.Constants;
import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.kernel.CPU;
import com.grarak.kerneladiutor.utils.kernel.GPU;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/*
This rewrite is re-using code that Grarak had originally used in his fragment. Credits go to the original source.

 */
public class FrequencyTableFragment extends RecyclerViewFragment implements Constants {

    @Override
    protected boolean pullToRefreshIsEnabled() {
        return true;
    }

    @Override
    public void refreshView() {
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                removeAllViews();
            }

            @Override
            protected Void doInBackground(Void... params) {
                generateSystemTimes();
                generateViewCPU();
                generateViewGPU();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                refreshLayout.setRefreshing(false);
            }
        }.execute();
    }

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
        generateSystemTimes();
        generateViewCPU();
        generateViewGPU();
    }
    private void generateSystemTimes() {
        CardViewItem.DCardView muptimeCard = new CardViewItem.DCardView();
        muptimeCard.setTitle("System Times (since boot):");
        muptimeCard.setDescription(
                        "Uptime: " + getDurationBreakdown(SystemClock.elapsedRealtime()) +
                        "\nTotal Awake Time: " + getDurationBreakdown(SystemClock.uptimeMillis()) +
                        "\nDeep Sleep: " + getDurationBreakdown(SystemClock.elapsedRealtime() - SystemClock.uptimeMillis()) +
                        "\nScreen ON Time: " + getDurationBreakdown(getUPtime(GPU_TIME_IN_STATE)* 10) +
                        "\nDevice was kept awake " + getDurationBreakdown(SystemClock.uptimeMillis() - getUPtime(GPU_TIME_IN_STATE)* 10) + " while screen was OFF by background tasks, " +
                        "repesenting a " + ((SystemClock.uptimeMillis() - getUPtime(GPU_TIME_IN_STATE)* 10) * 100) / SystemClock.elapsedRealtime() + "% of its uptime."
        );
        addView(muptimeCard);
    }

    private void generateViewCPU() {
            String value = Utils.readFile(Utils.getsysfspath(CPU_TIME_IN_STATE_ARRAY, 0));
            String[] lines = value.split("\n");
            int total_time = getUPtime(Utils.getsysfspath(CPU_TIME_IN_STATE_ARRAY, 0));
            String unusedStates = "";

            LinearLayout uiStatesView = new LinearLayout(getActivity());
            uiStatesView.setOrientation(LinearLayout.VERTICAL);
            CardViewItem.DCardView frequencyCard = new CardViewItem.DCardView();
            frequencyCard.setTitle("CPU - Time in States (Online: " + getDurationBreakdown(total_time * 10) + ")");
            frequencyCard.setView(uiStatesView);
            frequencyCard.setFullSpan(true);

            for (int x = 0; x < lines.length; x++) {
                String[] temp = lines[x].split(" ");
                int freq = Utils.stringToInt(temp[0]);
                int freq_time = Utils.stringToInt(temp[1]);
                int pct = total_time > 0 ? (freq_time * 100) / total_time : 0;
                //Limit the freqs shown to only anything with at least 1% use
                if (pct >= 1) {
                    FrameLayout layout = (FrameLayout) LayoutInflater.from(getActivity())
                            .inflate(R.layout.state_row, uiStatesView, false);

                    // map UI elements to objects
                    TextView freqText = (TextView) layout.findViewById(R.id.ui_freq_text);
                    TextView durText = (TextView) layout.findViewById(R.id.ui_duration_text);
                    TextView perText = (TextView) layout.findViewById(R.id.ui_percentage_text);
                    ProgressBar bar = (ProgressBar) layout.findViewById(R.id.ui_bar);

                    // modify the row
                    freqText.setText(freq / 1000 + "Mhz");
                    perText.setText(pct + "%");
                    // Multiple the time_in_state time value by 10 as it is stored in UserTime Units (10ms)
                    durText.setText(getDurationBreakdown(freq_time * 10));
                    bar.setProgress(pct);

                    uiStatesView.addView(layout);
                } else {
                    if(unusedStates.length() > 0){
                        unusedStates += ", ";
                    }
                    unusedStates += freq / 1000 + "MHZ";
                }
            }
            addView(frequencyCard);
            if (unusedStates.length() > 0) {
                CardViewItem.DCardView mUnUsedStatesCard = new CardViewItem.DCardView();
                mUnUsedStatesCard.setTitle("CPU - Unused States: (<1%)");
                mUnUsedStatesCard.setDescription(unusedStates);
                addView(mUnUsedStatesCard);
            }

    }

    private void generateViewGPU() {

        int total_time = getUPtime(GPU_TIME_IN_STATE);
        String value = Utils.readFile(GPU_TIME_IN_STATE);
        String[] lines = value.split("\n"); // get lines

        LinearLayout uiStatesView = new LinearLayout(getActivity());
        uiStatesView.setOrientation(LinearLayout.VERTICAL);
        CardViewItem.DCardView frequencyCard = new CardViewItem.DCardView();
        frequencyCard.setTitle("GPU - Time in States (Online: " + getDurationBreakdown(total_time * 10) + ")");
        frequencyCard.setView(uiStatesView);
        frequencyCard.setFullSpan(true);

        for (int x = lines.length - 1; x >= 0; x--) {
            String[] temp = lines[x].split(" ");
            int freq_time = Utils.stringToInt(temp[1]);
            int pct = total_time > 0 ? (freq_time * 100) / total_time : 0;
                FrameLayout layout = (FrameLayout) LayoutInflater.from(getActivity())
                        .inflate(R.layout.state_row, uiStatesView, false);

                // map UI elements to objects
                TextView freqText = (TextView) layout.findViewById(R.id.ui_freq_text);
                TextView durText = (TextView) layout.findViewById(R.id.ui_duration_text);
                TextView perText = (TextView) layout.findViewById(R.id.ui_percentage_text);
                ProgressBar bar = (ProgressBar) layout.findViewById(R.id.ui_bar);

                // modify the row
                freqText.setText(temp[0] + "Mhz");
                perText.setText(pct + "%");
                // Multiple the time_in_state time value by 10 as it is stored in UserTime Units (10ms)
                durText.setText(getDurationBreakdown(freq_time * 10));
                bar.setProgress(pct);

                uiStatesView.addView(layout);
            }
        addView(frequencyCard);
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
        StringBuilder sb = new StringBuilder(64);
        if(millis <= 0)
        {
            sb.append("00m00s");
            return sb.toString();
        }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        if (days > 0) {
            sb.append(days);
            sb.append("d");
        }
        if (hours > 0) {
            sb.append(hours);
            sb.append("h");
        }
        sb.append(String.format("%02d", minutes));
        sb.append("m");
        sb.append(String.format("%02d", seconds));
        sb.append("s");
        return sb.toString();
    }

    private static int getUPtime(String path){
        int total_time = 0;
        String value = Utils.readFile(path);
        String[] lines = value.split("\n"); // get lines
        String[] times = new String[lines.length];
        for (int a = 0; a < lines.length; a++) { //separate freqs from times
            String[] temp = lines[a].split(" ");
            times[a] = temp[1];
            total_time = total_time + Utils.stringToInt(times[a]);
        }
        return total_time;
    }
}
