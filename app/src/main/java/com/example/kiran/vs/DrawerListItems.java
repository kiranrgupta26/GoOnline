package com.example.kiran.vs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Kiran on 2/4/2018.
 */

public class DrawerListItems extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ListView listView;
    private String[] drawerlistitems;
    String category;
    String itemcategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawerlistitems);
        listView = (ListView)findViewById(R.id.lvdrawerlistitem);

        itemcategory = getIntent().getStringExtra("categoryitem");

        setDrawerList(itemcategory);
        listView.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,drawerlistitems));
        listView.setOnItemClickListener(DrawerListItems.this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       // selectItem(position);
        String itemSelected = (String)parent.getItemAtPosition(position);
        Toast.makeText(DrawerListItems.this,itemSelected,Toast.LENGTH_SHORT).show();
        Intent displayItems = new Intent(DrawerListItems.this,DisplayItems.class);
        displayItems.putExtra("categoryselected",itemcategory);
        displayItems.putExtra("subcategory",itemSelected);
        startActivity(displayItems);
        finish();
    }

    public void setDrawerList(String categoryItem)
    {
        if(categoryItem.equals("Stationary")) {
            category = "stationary";
            drawerlistitems = getResources().getStringArray(R.array.stationary);
        }
        else if(categoryItem.equals("Note Books")){
            category="notebooks";
            drawerlistitems = getResources().getStringArray(R.array.notebooks);
        }
        else if(categoryItem.equals("Sample Papers"))
        {
            category = "samplepapers";
            drawerlistitems = getResources().getStringArray(R.array.samplepapers);
        }
    }
}
