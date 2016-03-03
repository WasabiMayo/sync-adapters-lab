package com.example.wasabi.syncadapterlab;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Wasabi on 3/2/2016.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private ContentResolver mContentResolver;

    public SyncAdapter(Context context, boolean autoInitialize){
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs){
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        String wholeData ="";
        String[] symbols = new String[]{"ABX","BRK.A","BRK.B","SLW","SLV"};

        for(int i=0; i<symbols.length; i++) {
            try {
                URL url = new URL("http://dev.markitondemand.com/MODApis/Api/v2/Quote/json?symbol="+symbols[i]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inStream = connection.getInputStream();
                String data = getInputData(inStream);
                wholeData = wholeData + data + ",";
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        wholeData = wholeData.substring(0,wholeData.length()-1);
        wholeData = "{\"stocks\":[" + wholeData + "]}";

        Gson gson = new Gson();
        StocksResult result = gson.fromJson(wholeData,StocksResult.class);

        for(int i=0; i<result.getStocks().size(); i++) {
            Log.d("SyncAdapter", "Company name: " + result.getStocks().get(i).getName() + " Last price: " + result.getStocks().get(i).getLastPrice());
        }
    }

    private String getInputData(InputStream inStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));

        String data = null;

        while ((data = reader.readLine()) != null) {
            builder.append(data);
        }

        reader.close();

        return builder.toString();
    }
}
