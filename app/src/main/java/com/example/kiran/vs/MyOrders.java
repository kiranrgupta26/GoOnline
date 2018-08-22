package com.example.kiran.vs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Kiran on 2/19/2018.
 */

public class MyOrders extends AppCompatActivity implements MyOrdersListView.CustomCancelOrder{
    JSONArray jsonArray = new JSONArray();
    TextView tvdisplay;
    private MyOrdersListView myOrdersListView;
    ListView myorderslist;
    String user = "username";
    SharedPreferences loginInfo;
    private final String prefName = "myCredentials";
    ArrayList<MyOrderPOJO> myOrderPOJOs = new ArrayList<>();
    String username;
    URL url;
    Uri builtUri;
    URL urlObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myorders_layout);
        myorderslist = (ListView)findViewById(R.id.lvmyorderslist);
        loginInfo = getSharedPreferences(prefName,MODE_PRIVATE);
        username = loginInfo.getString(user,null);
        GetOrders getOrders =  new GetOrders();
        getOrders.execute(username);
    }

    @Override
    public void onCancelOrderListener(String orderid) {
        Toast.makeText(MyOrders.this,"CAncelling The Order",Toast.LENGTH_SHORT).show();
        loginInfo = getSharedPreferences(prefName,MODE_PRIVATE);
        username = loginInfo.getString(user,null);
        new CancelOrder().execute(username,orderid);
    }


    public class CancelOrder extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            //new GetOrders().execute(username);
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection httpURLConnection=null;
            try {
                builtUri = Uri.parse("http://139.59.89.198:8060/")
                        .buildUpon()
                        .appendPath("cancelorder")
                        .appendQueryParameter("username",params[0])
                        .appendQueryParameter("orderid",params[1])
                        .build();

                url = new URL(builtUri.toString());

                urlObj = new URL(url.toString());
                httpURLConnection = (HttpURLConnection) urlObj.openConnection();

                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                int responseCode = httpURLConnection.getResponseCode();
                return null;

            }catch(Exception e)
            {
                Log.d("Cancel Order Error",e.getMessage());
            }
            finally {
                //httpURLConnection.disconnect();
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public class GetOrders extends AsyncTask<String,String,ArrayList<MyOrderPOJO>>
    {
        @Override
        protected void onPostExecute(ArrayList<MyOrderPOJO> myOrderPOJOs1) {
            try {
                myOrdersListView = new MyOrdersListView(MyOrders.this, R.layout.myorders_display_layout, myOrderPOJOs1);
                myOrdersListView.setCancelOrderListener(MyOrders.this);
                myorderslist.setAdapter(myOrdersListView);
            }catch (Exception e)
            {
                Log.d("Exception Myorder" ,e.getMessage());
            }
            super.onPostExecute(myOrderPOJOs1);
        }

        @Override
        protected ArrayList<MyOrderPOJO> doInBackground(String... params) {
            JSONObject jsonObject;
            String allorders="";
            HttpURLConnection httpURLConnection=null;
            BufferedReader bufferedReader =null;

            myOrderPOJOs = new ArrayList<>();

            try {

                builtUri = Uri.parse("http://139.59.89.198:8060/")
                        .buildUpon()
                        .appendPath("getorders")
                        .appendQueryParameter("username",params[0])
                        .build();

                url = new URL(builtUri.toString());

                urlObj = new URL(url.toString());

                httpURLConnection = (HttpURLConnection) urlObj.openConnection();
                httpURLConnection.setDefaultUseCaches(false);
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setRequestMethod("GET");
                int temp = httpURLConnection.getContentLength();
                long date = httpURLConnection.getDate();
                httpURLConnection.connect();
                int responseCode = httpURLConnection.getResponseCode();

                Date date1 = new Date(date);
                if(HttpURLConnection.HTTP_OK==responseCode)
                {
                    Log.d("Status Code ","OK");
                }
                bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                String inputLine=null;
                StringBuffer stringBuffer = new StringBuffer();

                while ((inputLine = bufferedReader.readLine()) != null) {
                    stringBuffer.append(inputLine);
                }
                jsonObject = new JSONObject(stringBuffer.toString());
                jsonArray = jsonObject.getJSONArray("myorders");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONArray json_myorder = jsonArray.getJSONArray(i);
                    allorders="";
                    JSONObject explrObject=null;
                    for(int j = 0; j < json_myorder.length(); j++)
                    {
                        explrObject = json_myorder.getJSONObject(j);
                        allorders = allorders + explrObject.get("itemname").toString();
                    }
                    myOrderPOJOs.add(new MyOrderPOJO(explrObject.get("orderid").toString(),allorders));
                }

                return  myOrderPOJOs;

            }catch (Exception e)
            {
                Log.d("Exception My orders" ,e.getMessage());
            }
            return  myOrderPOJOs;
        }
    }
}
