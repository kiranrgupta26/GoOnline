package com.example.kiran.vs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Kiran on 2/4/2018.
 */

public class DisplayItems extends AppCompatActivity{

    GridView gridView;
    private GridViewAdapter gridViewAdapter;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    Point size;
    JSONArray jsonArray;
    private ActionBarDrawerToggle drawerToggle;
    String user = "username";
    String pass = "password";
    private final String prefName = "myCredentials";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.displayitems);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        navigationView = (NavigationView)findViewById(R.id.nav_view);

        final SharedPreferences loginInfo = getSharedPreferences(prefName,MODE_PRIVATE);
        if((loginInfo.getString(user,null) !=null) && (loginInfo.getString(pass,null)!=null))
        {
            navigationView.getMenu().clear(); //clear old inflated items.
            try {
                navigationView.inflateMenu(R.menu.drawermenulogin);
                Toast.makeText(this,loginInfo.getString(user,"defualt"),Toast.LENGTH_SHORT).show();
            }catch(Exception e)
            {
                Log.d("NAvigation Exception",e.getMessage());
            }
        }
        else
        {
            navigationView.getMenu().clear(); //clear old inflated items.
            navigationView.inflateMenu(R.menu.drawermenu);
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();
                Intent drawerlist = new Intent(DisplayItems.this,DrawerListItems.class);
                switch (item.getItemId())
                {
                    case R.id.home:
                        break;
                    case R.id.stationary:
                        drawerlist.putExtra("categoryitem",item.getTitle());
                        startActivity(drawerlist);
                        break;
                    case R.id.notebooks:
                        drawerlist.putExtra("categoryitem",item.getTitle());
                        startActivity(drawerlist);
                        break;
                    case R.id.textbooks:
                        Intent displayItems = new Intent(DisplayItems.this,DisplayItems.class);
                        displayItems.putExtra("categoryselected",item.getTitle());
                        displayItems.putExtra("subcategory","123");
                        startActivity(displayItems);
                        break;
                    case R.id.guide:
                        Intent drawerexpandlist = new Intent(DisplayItems.this,DrawerExpandableList.class);
                        drawerexpandlist.putExtra("categoryitem",item.getTitle());
                        startActivity(drawerexpandlist);
                        break;
                    case R.id.samplepaper:
                        drawerlist.putExtra("categoryitem",item.getTitle());
                        startActivity(drawerlist);
                        break;
                    case R.id.login:
                        Intent signupintent = new Intent(DisplayItems.this,Login.class);
                        startActivity(signupintent);
                        break;
                    case R.id.cart:
                        Intent addcart = new Intent(DisplayItems.this, CartItems.class);
                        startActivity(addcart);
                        break;
                    case R.id.wishlist:
                        break;
                    case R.id.faq:
                        break;
                    case R.id.contactus:
                        break;
                    case R.id.logout:
                        SharedPreferences.Editor editor=loginInfo.edit();
                        editor.clear();
                        editor.commit();
                        navigationView.getMenu().clear();
                        navigationView.inflateMenu(R.menu.drawermenu);
                        break;
                    case R.id.account:
                        Intent account = new Intent(DisplayItems.this,MyAccount.class);
                        startActivity(account);
                        break;
                }
                return true;
            }
        });

        gridView = (GridView)findViewById(R.id.gridView);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_list_black_24dp);

        Display display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);

        String category1 = getIntent().getStringExtra("categoryselected");
        String subcategory = getIntent().getStringExtra("subcategory");
        String subcategory1 = getIntent().getStringExtra("subcategory1");
        if(subcategory1==null)
        {
            Toast.makeText(DisplayItems.this,"Does not contain subcategory1",Toast.LENGTH_SHORT).show();
            subcategory1="default";
        }


        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        try{
            new SendPostRequest1().execute(category1,subcategory,subcategory1);
        }catch(Exception e)
        {
            Log.d("Exception",e.getMessage());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        else if(item.getItemId()==R.id.action_cart)
        {
            Intent addcart = new Intent(DisplayItems.this, CartItems.class);
            startActivity(addcart);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class SendPostRequest1 extends AsyncTask<String,String,ArrayList<ImageItem>> {
        JSONObject jsonObject;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(ArrayList<ImageItem> s) {
            super.onPostExecute(s);
            gridViewAdapter = new GridViewAdapter(DisplayItems.this,R.layout.grid_item_layout,s,size);
            gridView.setAdapter(gridViewAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                    Intent intent = new Intent(DisplayItems.this, ItemDetails.class);
                    intent.putExtra("itemobject",item);
                    //intent.putExtra("item", item.getTitle());
                    //intent.putExtra("image", item.getImage());
                    //intent.putExtra("price",item.getPrice());
                    startActivity(intent);
                }
            });

        }
        @Override
        protected ArrayList<ImageItem> doInBackground(String... params) {
            HttpURLConnection httpURLConnection=null;
            final ArrayList<ImageItem> imageItems = new ArrayList<>();
            //"http://139.59.89.198:8069/"
            try{

                Uri builtUri = Uri.parse("http://139.59.89.198:8060/")
                        .buildUpon()
                        .appendPath("search")
                        .appendQueryParameter("category", params[0])//means tablename
                        .appendQueryParameter("subcategory1",params[1])//means subcat1
                        .appendQueryParameter("subcategory2",params[2])//means subcat2
                        .build();

                URL url = new URL(builtUri.toString());

                URL urlObj = new URL(url.toString());
                httpURLConnection = (HttpURLConnection)urlObj.openConnection();

                httpURLConnection.setRequestMethod("GET");

                int responseCode =httpURLConnection.getResponseCode();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String inputLine;
                StringBuffer stringBuffer= new StringBuffer();

                while((inputLine= bufferedReader.readLine())!=null)
                {
                    stringBuffer.append(inputLine);
                }
                jsonObject = new JSONObject(stringBuffer.toString());
                jsonArray = jsonObject.getJSONArray("temp");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject explrObject = jsonArray.getJSONObject(i);
                    //String price = explrObject.get("price").toString();
                    imageItems.add(new ImageItem(explrObject.get("image_path").toString(),explrObject.get("name").toString(),Integer.parseInt(explrObject.get("price").toString()),Integer.parseInt(explrObject.get("discount").toString()),explrObject.get("category").toString(),explrObject.getString("subcat1").toString(),explrObject.getString("subcat2").toString()));
                    //String discount = explrObject.get("discount").toString();
                }
                bufferedReader.close();
                return  imageItems;

            }catch(Exception exception)
            {
                Log.d("Exception",exception.getMessage());
            }
            finally {
                httpURLConnection.disconnect();
            }
            return null;
        }
    }

}
