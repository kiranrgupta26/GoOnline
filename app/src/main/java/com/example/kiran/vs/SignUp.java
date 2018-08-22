package com.example.kiran.vs;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Kiran on 1/25/2018.
 */

public class SignUp extends AppCompatActivity {

    EditText name;
    EditText username;
    EditText password;
    Button signup;
    String getUser;
    String getPass;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);

        name = (EditText)findViewById(R.id.etname);
        username = (EditText)findViewById(R.id.etusername);
        password = (EditText)findViewById(R.id.etpassword);
        signup = (Button)findViewById(R.id.btnsignup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUser = username.getText().toString();
                getPass = password.getText().toString();
                new createLogin().execute();
            }
        });
    }

    class createLogin extends AsyncTask<String,String,String>
    {
        HttpURLConnection httpURLConnection=null;
        @Override
        protected String doInBackground(String... params) {
            try{

                Uri builtUri = Uri.parse("http://139.59.89.198:8060/")
                        .buildUpon()
                        .appendPath("send")
                        .appendQueryParameter("to", getUser)
                        .appendQueryParameter("password",getPass)
                        .build();
                URL url = new URL(builtUri.toString());
                URL urlObj = new URL(url.toString());
                httpURLConnection = (HttpURLConnection)urlObj.openConnection();

                httpURLConnection.setRequestMethod("GET");

                int responseCode = httpURLConnection.getResponseCode();


               if(responseCode==200)
               {
               }
               else
               {
               }

            }catch (Exception e)
            {
                Log.d("SignUP Exception",e.getMessage());
            }
            finally {
                httpURLConnection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
