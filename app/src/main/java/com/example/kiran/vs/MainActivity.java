package com.example.kiran.vs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    TextView tvmaincontent;
    DrawerLayout drawerLayout;
    ListView listView;
    private String[] planets;
    ImageView ivitem;
    JSONArray jsonArray;
    GridView gridView;
    private GridViewAdapter gridViewAdapter;
    private ActionBarDrawerToggle drawerToggle;
    Point size;
    Toolbar toolbar;
    NavigationView navigationView;
    ExpandableListView expandableListView;

    String user = "username";
    String pass = "password";
    private final String prefName = "myCredentials";
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ExpandableAdapter expandableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tvmaincontent = (TextView)findViewById(R.id.tvmaincontent);
        //planets = getResources().getStringArray(R.array.planets);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);

        //expandableListView = (ExpandableListView)findViewById(R.id.expListView);
       // prepareListData();
       // expandableAdapter = new ExpandableAdapter(this, listDataHeader, listDataChild);
       // expandableListView.setAdapter(expandableAdapter);

        /*expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
              *//*  Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();*//*
            }
        });

        // Listview Group collasped listener
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                *//*Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();*//*

            }
        });

        // Listview on child click listener
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
               *//* Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();*//*
                return false;
            }
        });
*/

        navigationView = (NavigationView)findViewById(R.id.nav_view);

       final SharedPreferences loginInfo = getSharedPreferences(prefName,MODE_PRIVATE);
        if((loginInfo.getString(user,null) !=null) && (loginInfo.getString(pass,null)!=null))
        {
            navigationView.getMenu().clear(); //clear old inflated items.
            try {
                navigationView.inflateMenu(R.menu.drawermenulogin);
                View header = navigationView.getHeaderView(0);
                TextView userN = (TextView)header.findViewById(R.id.username);
                TextView userE = (TextView)header.findViewById(R.id.email);
                userE.setText(loginInfo.getString(user,"defualt"));
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
                Intent drawerlist = new Intent(MainActivity.this,DrawerListItems.class);
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
                        Intent displayItems = new Intent(MainActivity.this,DisplayItems.class);
                        displayItems.putExtra("categoryselected",item.getTitle());
                        displayItems.putExtra("subcategory","123");
                        startActivity(displayItems);
                        break;
                    case R.id.guide:
                        Intent drawerexpandlist = new Intent(MainActivity.this,DrawerExpandableList.class);
                        drawerexpandlist.putExtra("categoryitem",item.getTitle());
                        startActivity(drawerexpandlist);
                        break;
                    case R.id.samplepaper:
                        drawerlist.putExtra("categoryitem",item.getTitle());
                        startActivity(drawerlist);
                        break;
                    case R.id.login:
                        Intent signupintent = new Intent(MainActivity.this,Login.class);
                        startActivity(signupintent);
                        break;
                    case R.id.cart:
                        Intent addcart = new Intent(MainActivity.this, CartItems.class);
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
                        View header = navigationView.getHeaderView(0);
                        TextView userE = (TextView)header.findViewById(R.id.email);
                        userE.setText("username@sample.com");
                        navigationView.getMenu().clear();
                        navigationView.inflateMenu(R.menu.drawermenu);
                        break;
                    case R.id.account:
                        Intent account = new Intent(MainActivity.this,MyAccount.class);
                        startActivity(account);
                        break;
                }
                return true;
            }
        });

        //look into this
        //listView = (ListView)findViewById(R.id.drawerList);
        ivitem = (ImageView)findViewById(R.id.ivitem);
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

       // listView.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,planets));
        //listView.setOnItemClickListener(this);

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
            Log.d("SendData","Data Sending");
            new SendPostRequest().execute();


        }
        catch(Exception exception)
        {
            Log.d("ExceptionOccured","ThisTest");
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

    //toolbar selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        else if(item.getItemId()==R.id.action_cart)
        {
            Intent addcart = new Intent(MainActivity.this, CartItems.class);
            startActivity(addcart);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        drawerToggle.syncState();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        selectItem(position);
       String itemSelected = (String)parent.getItemAtPosition(position);
        if(itemSelected.equals("Login"))
        {
            Intent signupintent = new Intent(MainActivity.this,Login.class);
            startActivity(signupintent);
        }
    }

    public void selectItem(int position)
    {
       // listView.setItemChecked(position,true);
       // setTitile(planets[position]);
    }

    public void setTitile(String title)
    {

        getSupportActionBar().setTitle(title);
    }

    class SendPostRequest extends AsyncTask<String,String,ArrayList<ImageItem>> {
        JSONObject jsonObject;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(ArrayList<ImageItem> s) {
            super.onPostExecute(s);
            gridViewAdapter = new GridViewAdapter(MainActivity.this,R.layout.grid_item_layout,s,size);
            gridView.setAdapter(gridViewAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                    //String trr = item.getSubcat2();
                    Intent intent = new Intent(MainActivity.this, ItemDetails.class);
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

            //"http://139.59.89.198:8068/"
            try{

                Uri builtUri = Uri.parse("http://139.59.89.198:8060/")
                        .buildUpon()
                        .appendPath("search")
                        .appendQueryParameter("category", "Text Books")
                        .appendQueryParameter("subcategory1","default")//means subcat1
                        .appendQueryParameter("subcategory2","default")
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


    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Stationary");
        listDataHeader.add("NoteBooks");
        listDataHeader.add("TextBooks");
        listDataHeader.add("Guide");
        listDataHeader.add("Sample Papers");


        // Adding child data
        List<String> stationary = new ArrayList<String>();
        stationary.add("Pencil");
        stationary.add("Reynolds Pen");
        stationary.add("Parker Pen");
        stationary.add("Rubber");
        stationary.add("Scale");
        stationary.add("Crayons");
        stationary.add("Cover Rolls");
        stationary.add("Geometry Box");
        stationary.add("Others");

        List<String> notebooks = new ArrayList<String>();
        notebooks.add("Sundaram");
        notebooks.add("Navneet");
        notebooks.add("Classmate");

        List<String> textbooks = new ArrayList<String>();
        textbooks.add("C.B.S.E");
        textbooks.add("Govt.");

        List<String> guide = new ArrayList<String>();
        guide.add("Navneet");
        guide.add("Golden");
        guide.add("Full Marks");

        List<String> samplepaper = new ArrayList<String>();
        samplepaper.add("Oswal");
        samplepaper.add("Navneet");

        listDataChild.put(listDataHeader.get(0), stationary); // Header, Child data
        listDataChild.put(listDataHeader.get(1), notebooks);
        listDataChild.put(listDataHeader.get(2), textbooks);
        listDataChild.put(listDataHeader.get(3), guide);
        listDataChild.put(listDataHeader.get(4), samplepaper);
    }
}