package com.androidaura.fafscallapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class SignIn extends AppCompatActivity {

    String username, password;
    EditText uname, pwd;
    Button btnLogin;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private RequestQueue rQueue;
    LinearLayout ll_signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        uname = findViewById(R.id.uname);
        pwd = findViewById(R.id.pwd);
        btnLogin = findViewById(R.id.btnLogin);
        ll_signin = findViewById(R.id.ll_signin);

        pref = getApplicationContext().getSharedPreferences("loginPref", 0);
        editor = pref.edit();

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    username = uname.getText().toString();
                    password = pwd.getText().toString();

                    if(username.length() != 0 && password.length() != 0){
                        AuthCheck(username, password);
                    }else{
              Toast.makeText(SignIn.this, "Invalid Input", Toast.LENGTH_SHORT).show();

                    }

                }
            });

    }


    private void AuthCheck(final String uname, final String pwd) {

        final String registerURL = "http://103.51.20.9/EmployeeMangerApi/authenticate";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, registerURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getBoolean("success")){
                                parseData(response);
                            }else{
                                Toast.makeText(SignIn.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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

        rQueue = Volley.newRequestQueue(SignIn.this);
        rQueue.add(stringRequest);
    }


    private void parseData(String response) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);
        JSONObject employeeObject = (JSONObject) jsonObject.get("data");


        if (jsonObject.getBoolean("success")) {

            editor.putString("empName", (String) employeeObject.get("Name"));
            editor.putString("empId", username);
            editor.putString("empPwd", password);
            editor.commit();

        }
            if (employeeObject.get("Status").equals("Active")) {

                Intent i = new Intent(SignIn.this, PhoneCall.class);
                startActivity(i);
                finish();

            } else {
                Snackbar snackbar = Snackbar
                        .make(ll_signin, "Account Inactive", Snackbar.LENGTH_LONG);
                snackbar.setDuration(150000);
                snackbar.show();
            }

        }
    }

