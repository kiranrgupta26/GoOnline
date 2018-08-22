package com.example.kiran.vs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by Kiran on 2/4/2018.
 */

public class DrawerExpandableList extends AppCompatActivity{

    Spinner select_class;
    Spinner select_publisher;
    Button btnsearch;
    String categoryselected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawerexpandablelist);

        btnsearch = (Button)findViewById(R.id.btnsearch);
        select_class = (Spinner)findViewById(R.id.spinclass);
        select_publisher = (Spinner)findViewById(R.id.spinpublish);
        ArrayAdapter<CharSequence> classA = ArrayAdapter.createFromResource(this,R.array.select_class,android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> publishA = ArrayAdapter.createFromResource(this,R.array.select_publisher,android.R.layout.simple_spinner_dropdown_item);

        select_class.setAdapter(classA);
        select_publisher.setAdapter(publishA);
        categoryselected = getIntent().getStringExtra("categoryitem");
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(DrawerExpandableList.this,select_class.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                //Toast.makeText(DrawerExpandableList.this,select_publisher.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                Intent displayItem = new Intent(DrawerExpandableList.this,DisplayItems.class);
                displayItem.putExtra("categoryselected",categoryselected);
                displayItem.putExtra("subcategory",select_class.getSelectedItem().toString());
                displayItem.putExtra("subcategory1",select_publisher.getSelectedItem().toString());
                startActivity(displayItem);
                finish();
            }
        });
    }

}
