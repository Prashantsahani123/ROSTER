package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by USER1 on 23-11-2016.
 */
public class ExternalLinkActivity extends Activity {
    TextView tv_title;
    ImageView iv_backbutton;
    String description;
    String labelText;
    WebView edt_description;
    Button btn;
    String externalLink;
    String modulename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_external_link);

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        edt_description = (WebView) findViewById(R.id.description);
        btn = (Button)findViewById(R.id.btn);
         modulename = PreferenceManager.getPreference(ExternalLinkActivity.this, PreferenceManager.MODUEL_NAME);
        String moduleId = PreferenceManager.getPreference(ExternalLinkActivity.this, PreferenceManager.MODULE_ID);
        String grpId = PreferenceManager.getPreference(ExternalLinkActivity.this, PreferenceManager.GROUP_ID);

        tv_title.setText(modulename);

        if (InternetConnection.checkConnection(this)) {
            GetExternalLinkTask asyncTask =  new GetExternalLinkTask(grpId, moduleId);
            asyncTask.execute();
        }
        else{
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if( externalLink.trim().equals("")) {
                        Toast.makeText(ExternalLinkActivity.this, "Link is empty.", Toast.LENGTH_LONG).show();
                        return;

                    }
                    URL url = new URL(externalLink);

                    if (InternetConnection.checkConnection(ExternalLinkActivity.this)) {
                        finish();
                        Intent i = new Intent(ExternalLinkActivity.this, OpenLinkActivity.class);

                        i.putExtra("link", externalLink);
                        i.putExtra("modulename", modulename);
                        startActivity(i);
                    //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(externalLink));
                    //ExternalLinkActivity.this.startActivity(intent);
                    }else{
                        Toast.makeText(ExternalLinkActivity.this, "Cannot Open page. No internet connection", Toast.LENGTH_LONG).show();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Toast.makeText(ExternalLinkActivity.this, "Invalid url. Please contact admin", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public class GetExternalLinkTask extends AsyncTask<Void, Void, String> {

        String grpId = "0", moduleId = "0";
        ArrayList<NameValuePair> args;
        final ProgressDialog progressDialog = new ProgressDialog(ExternalLinkActivity.this, R.style.TBProgressBar);
        public GetExternalLinkTask(String grpId, String moduleId) {
            this.grpId = grpId;
            this.moduleId = moduleId;
            args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair("grpId", grpId));
            args.add(new BasicNameValuePair("moduleId", moduleId));
            Log.e("TouchBase", "♦♦♦♦Parameters "+ Constant.GET_EXTERNAL_LINK+" :- "+args);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String result = HttpConnection.postData(Constant.GET_EXTERNAL_LINK, args);
                return result;
            } catch (Exception e) {
                Log.e("TouchBase", "♦♦♦♦Error is : " +e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.e("TouchBase", "♦♦♦♦Response : "+s);
                progressDialog.hide();
                JSONObject json = new JSONObject(s).getJSONObject("TBGetLinkResult");
                if ( json.getString("status").equals("0")) {
                    if(json.has("link")) {
                        String link = json.getString("link");
                        JSONObject linkjson = json.getJSONObject("link");
                        description = linkjson.getString("description");
                        labelText =linkjson.getString("lableText");
                        externalLink = linkjson.getString("link");
                        edt_description.getSettings().setJavaScriptEnabled(true);
                        edt_description.loadData(description,"text/html", "UTF-8");

                        //edt_description.setText(description);
                        btn.setText(labelText);

                    } else if (json.has("message")) {
                        Toast.makeText(ExternalLinkActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ExternalLinkActivity.this, "Failed to get information. Please try again", Toast.LENGTH_LONG).show();
                }
            } catch(NullPointerException npe) {
                Log.e("TouchBase", "♦♦♦♦NullPointerException");
                npe.printStackTrace();
                Toast.makeText(ExternalLinkActivity.this, "No response from server. Please try again", Toast.LENGTH_LONG).show();
            } catch(Exception e) {
                Log.e("TouchBase", "♦♦♦♦Error is : "+e.getMessage());
                e.printStackTrace();
                Toast.makeText(ExternalLinkActivity.this, "Unknown error. Please try again", Toast.LENGTH_LONG).show();
            }
        }
    }

}
