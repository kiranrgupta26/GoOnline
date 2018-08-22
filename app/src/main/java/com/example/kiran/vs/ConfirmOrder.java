package com.example.kiran.vs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Kiran on 2/11/2018.
 */

public class ConfirmOrder extends AppCompatActivity {
    TableLayout myorders;
    GetCartItemsFromDataBase getCartItemsFromDataBase = new GetCartItemsFromDataBase(this);
    ArrayList<AddItemToCart> addItemToCartList;
    ArrayList<AddItemToCart> refresheddata;
    JSONArray pusrchaseOrder = new JSONArray();
    String user = "username";
    SharedPreferences loginInfo;
    private final String prefName = "myCredentials";

    JSONObject jsonItem;
    int displayCost;
    TextView tvcost1;
    Button btnconfirmorder;
    Button myaddress;
    TextView tvmyaddress;
    int responseCode;
    int flag=0;

    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmorderlayout);//changed the layout file, change according to that

        //myorders = (TableLayout)findViewById(R.id.tlmyorders);
        //tvcost1 = (TextView)findViewById(R.id.tvcost1);
        //btnconfirmorder = (Button)findViewById(R.id.btnconfirmorder);
       // myaddress = (Button)findViewById(R.id.btnaddaddress);
       // tvmyaddress = (TextView)findViewById(R.id.tvmyaddress);

        getCartItemsFromDataBase.openDataBase();
        addItemToCartList = getCartItemsFromDataBase.getDbCartList();
        displayCost = getCartItemsFromDataBase.getTotalCost();
        getCartItemsFromDataBase.closeDataBase();
       // display(addItemToCartList);
       // tvcost1.setText(String.valueOf(displayCost));

        loginInfo = getSharedPreferences(prefName,MODE_PRIVATE);
        username = loginInfo.getString(user,null);

       // tvmyaddress.setText("");
       // new GetAddress().execute(username);

        /*btnconfirmorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==1)
                {
                    getCartItemsFromDataBase.openDataBase();
                    refresheddata = getCartItemsFromDataBase.getDbCartList();
                    getCartItemsFromDataBase.removeAllCartItems();

                    loginInfo = getSharedPreferences(prefName,MODE_PRIVATE);
                    String username = loginInfo.getString(user,null);
                    for (AddItemToCart item : refresheddata) {
                        try {
                            jsonItem = new JSONObject();
                            jsonItem.put("itemname", item.getItemName());
                            jsonItem.put("itemquantity", item.getQuantity());
                            jsonItem.put("username",username);
                            jsonItem.put("sendcategory",item.getCategory());
                            jsonItem.put("sendsubcat1",item.getSubcat1());
                            jsonItem.put("sendsubcat2",item.getSubcat2());
                            pusrchaseOrder.put(jsonItem);
                        } catch (JSONException jsonException) {
                            Log.w("JSONException", jsonException.getMessage());
                        }

                    }
                    getCartItemsFromDataBase.closeDataBase();
                    new SendPurchaseItem().execute(pusrchaseOrder.toString());

                }
                else
                {
                    Toast.makeText(ConfirmOrder.this,"Please Add Address",Toast.LENGTH_LONG).show();
                }

            }
        });

        myaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent address = new Intent(ConfirmOrder.this,MyAccount.class);
                startActivity(address);
            }
        });*/

    }

    public void display(ArrayList<AddItemToCart> list)
    {
        for(AddItemToCart item:list)
        {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            row.setWeightSum(10);
            TableRow.LayoutParams tablerowweight;
            TextView itemname = new TextView(this);
            itemname.setText(item.getItemName());

            tablerowweight = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 7);
            itemname.setLayoutParams(tablerowweight);
            row.addView(itemname);
            TextView itemprice = new TextView(this);
            itemprice.setText(String.valueOf(item.getPrice()));

            tablerowweight = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2);
            itemprice.setLayoutParams(tablerowweight);
            row.addView(itemprice);
            TextView itemquantity = new TextView(this);
            itemquantity.setText(String.valueOf(item.getQuantity()));

            tablerowweight = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1);
            itemquantity.setLayoutParams(tablerowweight);
            row.addView(itemquantity);

            myorders.addView(row,new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

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
            btnconfirmorder.setEnabled(false);
            Toast.makeText(ConfirmOrder.this,"Order Confirmed",Toast.LENGTH_LONG).show();
        }
    }

    public class GetAddress extends AsyncTask<String,String,JSONObject>
    {
        JSONObject jsonObject;
        HttpURLConnection httpURLConnection=null;
        @Override
        protected JSONObject doInBackground(String... params) {
            try{
                Uri builtUri = Uri.parse("http://139.59.89.198:8060/")
                        .buildUpon()
                        .appendPath("address")
                        .appendQueryParameter("username", params[0])
                        .build();

                URL url = new URL(builtUri.toString());

                URL urlObj = new URL(url.toString());
                httpURLConnection = (HttpURLConnection)urlObj.openConnection();

                httpURLConnection.setRequestMethod("GET");

                responseCode = httpURLConnection.getResponseCode();


                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String inputLine;
                StringBuffer stringBuffer= new StringBuffer();

                while((inputLine= bufferedReader.readLine())!=null)
                {
                    stringBuffer.append(inputLine);
                }
                jsonObject = new JSONObject(stringBuffer.toString());
                inputLine = "";
                bufferedReader.close();

            }catch(Exception e)
            {

                Log.w("PurchaseException",e.getMessage());
            }
            finally {
                httpURLConnection.disconnect();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject address) {
            super.onPostExecute(address);

            if(responseCode==200)
            {
                Toast.makeText(ConfirmOrder.this,"Address Available",Toast.LENGTH_LONG).show();
                try{
                    tvmyaddress.setText(address.get("fullname").toString()+"\n"+address.get("address").toString()+"\n"+address.get("address2").toString()+"\n"+address.get("landmark").toString()+"\n"+address.get("pincode").toString()+"\n"+address.get("mobileno").toString());
                    flag=1;
                }catch(JSONException jsonexception)
                {
                    Log.d("Address Exception",jsonexception.getMessage());
                }
            }
            else {
                flag=0;
                Toast.makeText(ConfirmOrder.this,"Please Add Address",Toast.LENGTH_LONG).show();
            }
        }
    }
}
