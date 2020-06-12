package com.androidaura.fafscallapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class PhoneCall extends AppCompatActivity implements View.OnClickListener {


    TelephonyManager telephonyManager;
    PhoneStateListener callStateListener;
    ImageView viewReport;
    EditText phoneno;
    Button dialBackspace, dialCall, dialClear;
    Button dial0, dial1, dial2, dial3, dial4, dial5, dial6, dial7, dial8, dial9;
    String number, empId, callStatus;
    LinearLayout ll_submitreport;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    StringBuilder joinnumbers;
    private RequestQueue rQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phonecall);


        telephonyManager =
                (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        viewReport = findViewById(R.id.viewReport);

        phoneno = findViewById(R.id.phoneno);
        joinnumbers = new StringBuilder();
        joinnumbers.append("+91");
        phoneno.setText(joinnumbers.toString());

        ll_submitreport = findViewById(R.id.ll_submitreport);

        dialBackspace = findViewById(R.id.dialBackspace);
        dialBackspace.setOnClickListener(this);

        dialCall = findViewById(R.id.dialCall);
        dialCall.setOnClickListener(this);

        dialClear = findViewById(R.id.dialClear);
        dialClear.setOnClickListener(this);

        dial0 = findViewById(R.id.dial0);
        dial0.setOnClickListener(this);

        dial1 = findViewById(R.id.dial1);
        dial1.setOnClickListener(this);

        dial2 = findViewById(R.id.dial2);
        dial2.setOnClickListener(this);

        dial3 = findViewById(R.id.dial3);
        dial3.setOnClickListener(this);

        dial4 = findViewById(R.id.dial4);
        dial4.setOnClickListener(this);

        dial5 = findViewById(R.id.dial5);
        dial5.setOnClickListener(this);

        dial6 = findViewById(R.id.dial6);
        dial6.setOnClickListener(this);

        dial7 = findViewById(R.id.dial7);
        dial7.setOnClickListener(this);

        dial8 = findViewById(R.id.dial8);
        dial8.setOnClickListener(this);

        dial9 = findViewById(R.id.dial9);
        dial9.setOnClickListener(this);

        pref = getApplicationContext().getSharedPreferences("loginPref", 0);
        editor = pref.edit();

        empId = pref.getString("empId", "androidaura");

        if (empId.equals("androidaura")) {
            Intent i = new Intent(PhoneCall.this, SignIn.class);
            startActivity(i);
            finish();
        }

        dialCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                number = phoneno.getText().toString();

                if (number.length() >= 10) {
                    callPhoneNumber();
                } else {
                    Toast.makeText(PhoneCall.this, "Invalid Number", Toast.LENGTH_SHORT).show();

                }
            }
        });


        viewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PhoneCall.this, ViewReport.class);
                startActivity(i);

            }
        });


        callStateListener = new PhoneStateListener() {
            public void onCallStateChanged(int state, String incomingNumber) {

                if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    //   Toast.makeText(getApplicationContext(),"Phone is Currently in A call",Toast.LENGTH_LONG).show();
                    showAlertDialog();
                }

            }
        };

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.dial0:
                joinnumbers.append("0");
                break;

            case R.id.dial1:
                joinnumbers.append("1");
                break;

            case R.id.dial2:
                joinnumbers.append("2");
                break;

            case R.id.dial3:
                joinnumbers.append("3");
                break;

            case R.id.dial4:
                joinnumbers.append("4");
                break;

            case R.id.dial5:
                joinnumbers.append("5");
                break;

            case R.id.dial6:
                joinnumbers.append("6");
                break;

            case R.id.dial7:
                joinnumbers.append("7");
                break;

            case R.id.dial8:
                joinnumbers.append("8");
                break;

            case R.id.dial9:
                joinnumbers.append("9");
                break;

            case R.id.dialClear:
                joinnumbers.delete(0, joinnumbers.length());
                joinnumbers.append("+91");
                break;

            case R.id.dialBackspace:

                if (joinnumbers.length() > 0) {
                    joinnumbers.setLength(joinnumbers.length() - 1);
                }
        }
        phoneno.setText(joinnumbers.toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.viewReport:
                Intent i = new Intent(PhoneCall.this, ViewReport.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void callPhoneNumber() {


        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    ActivityCompat.requestPermissions(PhoneCall.this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));
                startActivity(callIntent);

            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));
                startActivity(callIntent);

            }
            telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);
            // showAlertDialog();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showAlertDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(PhoneCall.this);
        final View customLayout = getLayoutInflater().inflate(R.layout.custom_alert, null);
        alertDialog.setView(customLayout);

        final AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();

        Button ph_submit = customLayout.findViewById(R.id.ph_submit);

        ph_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final RadioGroup radioCallStatus = customLayout.findViewById(R.id.radioCallStatus);
                int getId = radioCallStatus.getCheckedRadioButtonId();
                RadioButton statusButton = customLayout.findViewById(getId);

                callStatus = statusButton.getText().toString();

                callPost();
                alert.dismiss();

            }
        });

    }

    private void callPost() {

        String registerURL = "http://103.51.20.9/EmployeeMangerApi/addCallHistory";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, registerURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                      //  Toast.makeText(PhoneCall.this, "Failed", Toast.LENGTH_SHORT).show();
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

                params.put("contactno", number);
                params.put("employeeId", empId);
                params.put("commment", "");
                params.put("callback", "NO");
                params.put("contactedPersonName", "Unknown");
                params.put("callDuration", "0");
                params.put("callStatus", callStatus);

                return params;
            }

        };

        rQueue = Volley.newRequestQueue(PhoneCall.this);
        rQueue.add(stringRequest);
    }

}