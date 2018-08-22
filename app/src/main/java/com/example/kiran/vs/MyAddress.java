package com.example.kiran.vs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Kiran on 2/21/2018.
 */

public class MyAddress extends AppCompatActivity {
    EditText etfullname;
    EditText etmobile;
    EditText etaddress;
    EditText etaddress2;
    EditText etlandmark;
    EditText etpincode;
    Button btnsubmit;
    JSONObject jsondetails;
    JSONArray userinfo =new JSONArray();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myaddress_layout);
        etfullname = (EditText)findViewById(R.id.etfullname);
        etmobile =(EditText)findViewById(R.id.etmobile);
        etaddress = (EditText)findViewById(R.id.etaddress);
        etaddress2 =(EditText)findViewById(R.id.etaddress2);
        etlandmark = (EditText)findViewById(R.id.etlandmark);
        etpincode = (EditText)findViewById(R.id.etpincode);
        btnsubmit = (Button)findViewById(R.id.btnsubmit);
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((!etfullname.getText().toString().matches("")) && (!etmobile.getText().toString().matches("")) && (!etaddress.getText().toString().matches("")) && (!etaddress2.getText().toString().matches("")) && (!etlandmark.getText().toString().matches("")) && (!etpincode.getText().toString().matches("")))
                {
                    String te =etfullname.getText().toString();
                    try {
                        jsondetails = new JSONObject();
                        jsondetails.put("username","kiran.rgupta26@live.com");
                        jsondetails.put("fullname",etfullname.getText().toString());
                        jsondetails.put("mobile",etmobile.getText().toString());
                        jsondetails.put("address",etaddress.getText().toString());
                        jsondetails.put("address2",etaddress2.getText().toString());
                        jsondetails.put("landmark",etlandmark.getText().toString());
                        jsondetails.put("pincode",etpincode.getText().toString());
                        userinfo.put(jsondetails);
                        //send to server
                        new SendUserInfo().execute(userinfo.toString());
                    } catch (JSONException jsonException) {
                        Log.w("JSONException", jsonException.getMessage());
                    }
                }
                else
                {
                    Toast.makeText(MyAddress.this,"Please Fill All The Details",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
