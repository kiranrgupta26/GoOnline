package com.example.kiran.vs;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Kiran on 1/30/2018.
 */

/*
public class SendPurchaseItem extends AsyncTask<String,String,String> {
    HttpURLConnection httpURLConnection=null;
    @Override
    protected String doInBackground(String... params) {
        try{
            Uri builtUri = Uri.parse("http://139.59.89.198:8060/")
                    .buildUpon()
                    .appendPath("purchase")
                    .appendQueryParameter("purchaselist", params[0])
                    .build();

            URL url = new URL(builtUri.toString());

            URL urlObj = new URL(url.toString());
            httpURLConnection = (HttpURLConnection)urlObj.openConnection();
            httpURLConnection.connect();
            httpURLConnection.setRequestMethod("GET");

            int responseCode = httpURLConnection.getResponseCode();
        }catch(Exception e)
        {

            Log.w("PurchaseException",e.getMessage());
        }
        finally {
            httpURLConnection.disconnect();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
*/
