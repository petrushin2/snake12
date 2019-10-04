package com.snakex.pro;

import android.content.Context;
import android.content.SharedPreferences;

public class ThreadOfDataUtils {
    private static String smakex = "snakex";
    private static String xsnake = "xsnake";
    private SharedPreferences preferences;

    public ThreadOfDataUtils(Context context){
        String NAME = "xgol";
        preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public void setThreadOfData(String data){
        preferences.edit().putString(ThreadOfDataUtils.smakex, data).apply();
    }

    public String getThreadOfData(){
        return preferences.getString(smakex, "");
    }

}
