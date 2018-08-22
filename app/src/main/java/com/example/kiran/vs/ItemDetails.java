package com.example.kiran.vs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Kiran on 1/19/2018.
 */

public class ItemDetails extends AppCompatActivity {

    ImageView ivImageDetails;
    TextView tvItemDetails;
    TextView tvitemprice;
    Toolbar toolbar;
    Button addCart;
    String bitmap;
    Bitmap bitmapImage;
    String title;
    int price;
    ImageItem getIntentItem;
    GetCartItemsFromDataBase getCartItemsFromDataBase = new GetCartItemsFromDataBase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details_layout);

        ivImageDetails  =(ImageView)findViewById(R.id.ivimagedetails);
        tvItemDetails = (TextView)findViewById(R.id.tvitemdetails);
        tvitemprice = (TextView)findViewById(R.id.tvitemprice);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        addCart = (Button)findViewById(R.id.addtocart);

        getIntentItem = (ImageItem) getIntent().getSerializableExtra("itemobject");
        //title = getIntent().getStringExtra("item");
        //bitmap = getIntent().getStringExtra("image");
        //price = getIntent().getIntExtra("price",0);
        /*try{
            Picasso.with(this)
                    .load(getIntentItem.getImage())
                    .into(ivImageDetails);
        }
        catch(Exception e)
        {
            Log.d("Picasso Exception",e.getMessage());
        }*/
        new ImageDownloader1(ivImageDetails).execute(getIntentItem.getImage());


        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check whether it is available in cart or not
                getCartItemsFromDataBase.openDataBase();
                Boolean check = getCartItemsFromDataBase.searchItemInCart(getIntentItem.getTitle());
                if(check)
                {
                    Toast.makeText(ItemDetails.this,"Product Already In Cart",Toast.LENGTH_SHORT).show();
                }
                else {
                    AddItemToCart itemToCart = new AddItemToCart();
                    itemToCart.setItemName(getIntentItem.getTitle());
                    itemToCart.setImage_path(bitmapImage);
                    itemToCart.setPrice(getIntentItem.getPrice());
                    itemToCart.setCategory(getIntentItem.getCategory());
                    itemToCart.setSubcat1(getIntentItem.getSubcat1());
                    itemToCart.setSubcat2(getIntentItem.getSubcat2());

                    getCartItemsFromDataBase.openDataBase();
                    getCartItemsFromDataBase.addItemToCart(itemToCart);
                    Toast.makeText(ItemDetails.this,"Product Added To Cart",Toast.LENGTH_SHORT).show();
                }
                getCartItemsFromDataBase.closeDataBase();
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        //bitmap = getIntent().getParcelableExtra("image");
        tvItemDetails.setText(getIntentItem.getTitle());
        tvitemprice.setText("Rs "+getIntentItem.getPrice());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId()==R.id.action_cart)
        {
            Intent addcart = new Intent(ItemDetails.this, CartItems.class);
            startActivity(addcart);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class ImageDownloader1 extends AsyncTask<String,Void,Bitmap> {
        private final WeakReference<ImageView> imageViewWeakReference;

        public ImageDownloader1(ImageView imageView)
        {
            imageViewWeakReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            //HttpURLConnection httpURLConnection=null;
            try{
                URL url = new URL(params[0]);
                Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return bitmap;
            }catch(Exception e)
            {
                //httpURLConnection.disconnect();
                Log.w("ImageDownloader","Error Downloading Image "+params[0]);
            }
            finally {
               // httpURLConnection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(imageViewWeakReference!=null)
            {
                ImageView imageView = imageViewWeakReference.get();
                imageView.getLayoutParams().height = 600;
                imageView.getLayoutParams().width = 500;
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                if(imageView!=null)
                {
                    if(bitmap!=null)
                    {
                        imageView.setImageBitmap(bitmap);
                        bitmapImage = bitmap;
                    }
                }
            }
        }
    }

}
