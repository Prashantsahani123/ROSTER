package com.SampleApp.row.Utils;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Hashtable;

/**
 * Created by user on 31-08-2016.
 */
public class JSONHttpConnection {

    public static String postRequest(String url, String jsonParam) {
        String response="";
        HttpURLConnection connection = null;

        URL mUrl = null;
        try {
            mUrl = new URL(url);
            connection = (HttpURLConnection) mUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoInput(true);
            connection.setDoOutput(false);

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            byte[] data = jsonParam.getBytes("UTF-8");
            wr.write(data);
            wr.flush();
            wr.close();
            int responseCode = connection.getResponseCode();
           if(responseCode==200){
               BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

               String line = "";

               while ( (line = reader.readLine()) != null ) {
                   response = response + line;
               }

           }
            Log.e("Response meaasage >", "" + response);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if ( response == null ) {
            Hashtable<String, String> result = new Hashtable<String, String>();
            result.put("status", "1");
            result.put("message", "Failed to fetch data.");
            Gson gson = new Gson();
            response = gson.toJson(result);
        }
        return response;
    }

    public static String postRequest(String url, JSONObject jsonParam) {
        return postRequest(url, jsonParam.toString());
    }
}
