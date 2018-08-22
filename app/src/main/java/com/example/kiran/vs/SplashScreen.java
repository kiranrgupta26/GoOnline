package com.example.kiran.vs;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Kiran on 1/26/2018.
 */

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        final Intent intent = new Intent(this,MainActivity.class);
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo !=null && networkInfo.isConnectedOrConnecting();
        if(isConnected)
        {
            Thread timer = new Thread(){
                public void run()
                {
                    try{
                        sleep(3000);
                    }catch(InterruptedException e)
                    {
                        e.printStackTrace();
                    }finally {
                        startActivity(intent);
                        finish();
                    }
                }
            };
            timer.start();
            Toast.makeText(this,"Internet Connected",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }

    }
}
