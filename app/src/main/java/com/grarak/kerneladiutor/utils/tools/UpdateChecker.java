package com.grarak.kerneladiutor.utils.tools;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.grarak.kerneladiutor.BuildConfig;
import com.grarak.kerneladiutor.R;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by shmuelr on 3/10/16.
 */
public class UpdateChecker {

    private static final String TAG = UpdateChecker.class.getSimpleName();
    private static WeakReference<Context> contextWeakReference;
    private static WeakReference<Callback> callbackWeakReference;
    private static WeakReference<DownloadCallback> downloadWeakReference;

    public static void checkForUpdate(Callback callback, final Context context){

        contextWeakReference = new WeakReference<>(context);
        callbackWeakReference = new WeakReference<>(callback);

        new AsyncTask<Void, Void, AppUpdateData>(){

            @Override
            protected AppUpdateData doInBackground(Void... params) {

                AppUpdateData appUpdateData = null;
                String updateURL;
                if(contextWeakReference.get() == null){
                    Log.w(TAG, "Context is empty");
                    return null;
                } else {
                    updateURL = context.getString(R.string.APP_UPDATE_URL);
                }

                if(TextUtils.isEmpty(updateURL)){
                    Log.w(TAG, "App update Url is empty");
                    return null;
                }

                OkHttpClient client = new OkHttpClient.Builder().build();
                Request request = new Request.Builder().url(updateURL).build();

                try {
                    Response response =  client.newCall(request).execute();
                    if(response.isSuccessful()){
                        String responseBody = response.body().string();
                        Log.d(TAG, "Response String is "+responseBody);
                        appUpdateData = new Gson().fromJson(responseBody, AppUpdateData.class);
                    } else {
                        Log.e(TAG, "Response was not successful. | "+response.code());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return appUpdateData;
            }

            @Override
            protected void onPostExecute(AppUpdateData appUpdateData) {
                Callback localCallback = callbackWeakReference.get();
                if(localCallback != null){
                    if(appUpdateData == null){
                        localCallback.onError();
                    } else {
                        localCallback.onSuccess(appUpdateData);
                    }
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static boolean isOldVersion(AppUpdateData appUpdateData){
        Log.d(TAG, "Comparing app version "+ BuildConfig.VERSION_CODE+" to public version "+appUpdateData.currentBuildNumber);
        return BuildConfig.VERSION_CODE < appUpdateData.currentBuildNumber;
    }

    public static void downloadNewVersion(AppUpdateData appUpdateData,final File cacheDir, DownloadCallback callback){
        downloadWeakReference = new WeakReference<>(callback);

        new AsyncTask<String, Void, File>(){

            @Override
            protected File doInBackground(String... params) {
                File file = null;

                try {
                    OkHttpClient client = new OkHttpClient.Builder().build();
                    Request request = new Request.Builder().url(params[0]).build();

                    Response response = client.newCall(request).execute();

                    file = new File(cacheDir.getPath()+"/app-debug.apk");
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(response.body().bytes());
                    fos.close();

                }catch (Exception e){
                    e.printStackTrace();
                }

                return file;
            }

            @Override
            protected void onPostExecute(File file) {

                DownloadCallback localCallback = downloadWeakReference.get();
                if(localCallback != null) {
                    if (file != null) {
                        localCallback.onSuccess(file);
                    } else {
                        localCallback.onError();
                    }
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, appUpdateData.fileUrl);

    }

    public interface Callback{
        void onSuccess(final AppUpdateData appUpdateData);
        void onError();
    }

    public interface DownloadCallback{
        void onSuccess(final File file);
        void onError();
    }

    public class AppUpdateData {

        @SerializedName("current_build")
        public String currentBuild;
        @SerializedName("current_build_number")
        public Integer currentBuildNumber;
        @SerializedName("download_url")
        public String downloadUrl;
        @SerializedName("file_url")
        public String fileUrl;

    }
}



