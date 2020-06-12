package com.androidaura.fafscallapp;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;


public class ViewReport extends AppCompatActivity {


    TextView total;
    ArrayList<HashMap<String, String>> arraylist;
    String currentDate;
    String empId;
    ListView listview;
    CustomViewReportList adapter;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private RequestQueue rQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewreport);

        pref = getApplicationContext().getSharedPreferences("loginPref", 0);
        editor = pref.edit();
        empId = pref.getString("empId", "0");

        total = findViewById(R.id.total);

        // SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        // SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        //  Date date = new Date();

        currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        listview = (ListView) findViewById(R.id.lv);
        arraylist = new ArrayList<HashMap<String, String>>();
        adapter = new CustomViewReportList(this, arraylist);
        listview.setAdapter(adapter);

        arraylist.clear();
        new Task().execute();

    }

    private class Task extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @SuppressLint("CommitTransaction")
        protected Void doInBackground(Void... params) {

            String registerURL = "http://103.51.20.9/EmployeeMangerApi/getCallHistoryDetails";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, registerURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {

                                JSONObject jsonObject = new JSONObject(response);

                                JSONArray arr = (JSONArray) jsonObject.get("data");

                                total.setText(jsonObject.getString("total"));

                                for (int i = 0; i < arr.length(); i++) {

                                    HashMap<String, String> map = new HashMap<String, String>();
                                    JSONObject obj = arr.getJSONObject(i);

                                    map.put("clv_call_status", obj.getString("call_status"));
                                    map.put("clv_number", obj.getString("contact_no"));

                                    arraylist.add(map);

                                }

                                adapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("vt", "error " + error.getMessage());
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
                    params.put("employeeid", empId);
                    params.put("fromDate", currentDate);
                    params.put("toDate", currentDate);

                    return params;
                }

            };

            rQueue = Volley.newRequestQueue(ViewReport.this);
            rQueue.add(stringRequest);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            adapter.notifyDataSetChanged();
        }
    }
}