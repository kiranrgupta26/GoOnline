package com.example.kiran.vs;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kiran on 1/20/2018.
 */

public class CartItems extends AppCompatActivity implements ListViewAdapter.customRemoveListener ,ListViewAdapter.CustomChangeQuantity {

    Toolbar toolbar;
    TextView totalCost;
    ListView listView;
    ListViewAdapter listViewAdapter;
    ArrayList<AddItemToCart> addItemToCartList;
    ArrayList<AddItemToCart> refresheddata;
    Button btnconfirm;
    String user = "username";
    String pass = "password";
    SharedPreferences loginInfo;
    private final String prefName = "myCredentials";
    GetCartItemsFromDataBase getCartItemsFromDataBase = new GetCartItemsFromDataBase(this);
    int displayCost;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cartitems_layout);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        listView = (ListView)findViewById(R.id.lvcartlist);
        btnconfirm = (Button)findViewById(R.id.btnconfirm);
        totalCost = (TextView)findViewById(R.id.tvtotalcost);

        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginInfo = getSharedPreferences(prefName,MODE_PRIVATE);
                String username = loginInfo.getString(user,null);
                String password = loginInfo.getString(pass,null);
                if(username == null && password == null)
                {
                    Intent loginIntent = new Intent(CartItems.this,Login.class);
                    startActivity(loginIntent);
                    finish();
                }
                else {
                   /* Toast.makeText(CartItems.this, username +" "+password, Toast.LENGTH_SHORT).show();
                    //Send Data to Server Along with UserName And Details in JSON Format.
                    getCartItemsFromDataBase.openDataBase();
                    refresheddata = getCartItemsFromDataBase.getDbCartList();
                    getCartItemsFromDataBase.removeAllCartItems();
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
                    new SendPurchaseItem().execute(pusrchaseOrder.toString());*/

                   Intent confirmorder = new Intent(CartItems.this,ConfirmOrder.class);
                    startActivity(confirmorder);
                    finish();
                }
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        callListView();
        listViewAdapter = new ListViewAdapter(this,R.layout.cart_items_display,addItemToCartList);
        listViewAdapter.setButtonListener(CartItems.this);
        listViewAdapter.setSpinnerListener(CartItems.this);
        listView.setAdapter(listViewAdapter);
        totalCost.setText("Total "+Integer.toString(displayCost));

    }

    public void callListView()
    {
        getCartItemsFromDataBase.openDataBase();
        //getCartItemsFromDataBase.removeAllCartItems();
        addItemToCartList = getCartItemsFromDataBase.getDbCartList();
        if(addItemToCartList.size()<1)
        {
            btnconfirm.setEnabled(false);
        }
        else
        {
            btnconfirm.setEnabled(true);
        }
        displayCost = getCartItemsFromDataBase.getTotalCost();
        getCartItemsFromDataBase.closeDataBase();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onButtonClickListener(int position, AddItemToCart item) {
            getCartItemsFromDataBase.openDataBase();
            refresheddata = getCartItemsFromDataBase.removeItemFromCart(item);
             if(refresheddata.size()<1)
             {
                btnconfirm.setEnabled(false);
            }
            else
            {
                btnconfirm.setEnabled(true);
            }
            displayCost = getCartItemsFromDataBase.getTotalCost();
            getCartItemsFromDataBase.closeDataBase();

            listViewAdapter = new ListViewAdapter(this,R.layout.cart_items_display,refresheddata);
            listViewAdapter.setButtonListener(CartItems.this);
            listViewAdapter.setSpinnerListener(CartItems.this);
            listView.setAdapter(listViewAdapter);
            totalCost.setText("Total "+Integer.toString(displayCost));

    }

    @Override
    public void onSpinnerClickListener(int position, AddItemToCart item) {
        getCartItemsFromDataBase.openDataBase();
        getCartItemsFromDataBase.updateItemQuantity(position,item);
        displayCost = getCartItemsFromDataBase.getTotalCost();
        refresheddata = getCartItemsFromDataBase.getDbCartList();
        getCartItemsFromDataBase.closeDataBase();

        listViewAdapter = new ListViewAdapter(this,R.layout.cart_items_display,refresheddata);
        listViewAdapter.setButtonListener(CartItems.this);
        listViewAdapter.setSpinnerListener(CartItems.this);
        listView.setAdapter(listViewAdapter);
        totalCost.setText("Total "+Integer.toString(displayCost));

    }
}
