package com.SampleApp.row;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.SampleApp.row.Utils.Constant;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    String add ;

    private String location = "", sportName = "";
    private double latitude=0.0;
    private double longitude=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editText = (EditText)findViewById(R.id.tv_location);

        Button btnSearch = (Button)findViewById(R.id.btn_search);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new GetLatLonFromAddress(editText.getText().toString()).execute();
                //startActivity(new Intent(MainActivity.this, MapsActivity.class));

            }
        });

    }


    class GetLatLonFromAddress extends AsyncTask<Void, Void, String>
    {
        String adddress;
        ProgressDialog pdialog;
        public GetLatLonFromAddress(String address)
        {
            this.adddress=address;
        }
        @Override
        protected void onPreExecute()
        {
            pdialog=new ProgressDialog(MainActivity.this);
            pdialog.setMessage("Loading...");
            pdialog.setCancelable(false);
            pdialog.show();
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Void... params)
        {
            //String tokenID=pref.getString(TAG_TOKEN, "");
            String uri = "http://maps.google.com/maps/api/geocode/json?address=" +
                    adddress + "&sensor=false";
            HttpGet httpGet = new HttpGet(uri);
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            StringBuilder stringBuilder = new StringBuilder();

            try {
                response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                InputStream stream = entity.getContent();
                int b;
                while ((b = stream.read()) != -1) {
                    stringBuilder.append((char) b);
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return stringBuilder.toString();
        }
        @Override
        protected void onPostExecute(String result)
        {
            if(pdialog!=null)
            {
                if (pdialog.isShowing())
                {
                    pdialog.dismiss();


                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject = new JSONObject(result.toString());

                        longitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                                .getJSONObject("geometry").getJSONObject("location")
                                .getDouble("lng");

                        latitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                                .getJSONObject("geometry").getJSONObject("location")
                                .getDouble("lat");

                        Log.d("latitude", ""+latitude);
                        Log.d("longitude", ""+longitude);


                        Intent intent = new Intent(MainActivity.this, SearchAddress.class);
                        Bundle bundle = new Bundle();
                        bundle.putDouble(Constant.LATITUDE,latitude);
                        bundle.putDouble(Constant.LONGITUDE,longitude);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 2251);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
            super.onPostExecute(result);
        }
    }
}
