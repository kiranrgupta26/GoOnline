package com.example.kiran.vs;

import android.content.Intent;
import android.content.SharedPreferences;
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
 * Created by Kiran on 1/31/2018.
 */

public class Login extends AppCompatActivity {
    EditText etloginuser;
    EditText etloginpassword;
    Button btnlogin;
    Button btnloginsignup;
    String user = "username";
    String pass = "password";
    SharedPreferences loginInfo;
    SharedPreferences.Editor editor;
    private final String prefName = "myCredentials";
    int responseCode;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        etloginuser = (EditText)findViewById(R.id.etloginuser);
        etloginpassword = (EditText)findViewById(R.id.loginpassword);
        btnloginsignup = (Button)findViewById(R.id.btnloginsignup);
        btnloginsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(Login.this,SignUp.class);
                startActivity(signup);
            }
        });
        btnlogin = (Button)findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new createLogin1().execute(etloginuser.getText().toString(),etloginpassword.getText().toString());
            }
        });
    }

    class createLogin1 extends AsyncTask<String,String,Integer>
    {
        HttpURLConnection httpURLConnection=null;
        @Override
        protected Integer doInBackground(String... params) {
            try{

                Uri builtUri = Uri.parse("http://139.59.89.198:8060/")
                        .buildUpon()
                        .appendPath("checkCredential")
                        .appendQueryParameter("username",params[0])
                        .appendQueryParameter("password",params[1])
                        .build();
                URL url = new URL(builtUri.toString());
                URL urlObj = new URL(url.toString());
                httpURLConnection = (HttpURLConnection)urlObj.openConnection();

                httpURLConnection.setRequestMethod("GET");

                responseCode = httpURLConnection.getResponseCode();
                return responseCode;
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
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
            if(s.intValue()==200)
            {
                Toast.makeText(Login.this,"Valid Credentials",Toast.LENGTH_LONG).show();
                loginInfo = getSharedPreferences(prefName,MODE_PRIVATE);
                editor = loginInfo.edit();
                editor.putString(user,etloginuser.getText().toString());
                editor.putString(pass,etloginpassword.getText().toString());
                editor.commit();
                Intent homeIntent = new Intent(Login.this,MainActivity.class);
                startActivity(homeIntent);
                finish();
            }
            else
            {
                Toast.makeText(Login.this,"INValid Credentials",Toast.LENGTH_LONG).show();
            }
        }
    }
}
