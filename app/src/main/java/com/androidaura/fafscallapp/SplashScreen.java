package com.androidaura.fafscallapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    final int SPLASH_TIME_OUT = 500;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String prefId, prefPwd;
    private RequestQueue rQueue;
    LinearLayout splashParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        splashParent = findViewById(R.id.splashParent);

        pref = getApplicationContext().getSharedPreferences("loginPref", 0);
        editor = pref.edit();

        prefId = pref.getString("empId", "androidauraID");
        prefPwd = pref.getString("empPwd", "androidauraPWD");

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

                if (!prefId.equals("androidauraID") && (!prefPwd.equals("androidauraPWD"))) {
                    AuthCheck(prefId, prefPwd);
                } else {

                    Intent i = new Intent(SplashScreen.this, SignIn.class);
                    startActivity(i);
                    finish();

                }
                overridePendingTransition(0, 0);

            }
        }, SPLASH_TIME_OUT);
    }

    private void AuthCheck(final String uname, final String pwd) {

        String registerURL = "http://103.51.20.9/EmployeeMangerApi/authenticate";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, registerURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            parseData(response);
                        } catch (JSONException e) {

                            Intent i = new Intent(SplashScreen.this, SignIn.class);
                            startActivity(i);
                            finish();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override

                    public void onErrorResponse(VolleyError error) {

                        Intent i = new Intent(SplashScreen.this, SignIn.class);
                        startActivity(i);
                        finish();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", "c7ad3e44a02d132392ec845cc125a6a1");
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", uname);
                params.put("passwd", pwd);
                return params;
            }

        };

        rQueue = Volley.newRequestQueue(SplashScreen.this);
        rQueue.add(stringRequest);
    }


    private void parseData(String response) throws JSONException {

        try {

            JSONObject jsonObject = new JSONObject(response);
            JSONObject employeeObject = (JSONObject) jsonObject.get("data");


            if (employeeObject.get("Status").equals("Active")) {

                Intent i = new Intent(SplashScreen.this, PhoneCall.class);
                startActivity(i);
                finish();

            } else {

                Snackbar snackbar = Snackbar
                        .make(splashParent, "Account Inactive", Snackbar.LENGTH_LONG);
                snackbar.setDuration(150000);
                snackbar.show();

            }
        } catch (Exception e) {
        }


    }
}