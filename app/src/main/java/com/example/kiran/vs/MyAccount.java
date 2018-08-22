package com.example.kiran.vs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Kiran on 2/11/2018.
 */

public class MyAccount extends AppCompatActivity implements AdapterView.OnItemClickListener{
    ListView lvmyaccount;
    private String[] accountlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myaccount);
        lvmyaccount = (ListView)findViewById(R.id.lvmyaccount);
        accountlist = getResources().getStringArray(R.array.accountlist);
        lvmyaccount.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,accountlist));
        lvmyaccount.setOnItemClickListener(MyAccount.this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String itemSelected = (String)parent.getItemAtPosition(position);
        Toast.makeText(MyAccount.this,itemSelected,Toast.LENGTH_SHORT).show();
        if (position==0)
        {
            Intent myaddress = new Intent(MyAccount.this,MyAddress.class);
            startActivity(myaddress);
        }
        else
        {
            Intent myorder = new Intent(MyAccount.this,MyOrders.class);
            startActivity(myorder);
        }
    }
}
