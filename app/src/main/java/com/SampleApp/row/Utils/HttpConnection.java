package com.SampleApp.row.Utils;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by user on 08-12-2015.
 */
public class HttpConnection {
    public static String flg ;
    public static String f;
    public static String  responseBody;
    public static String prev="";
    public static String postData(String url, List<NameValuePair> arrayList) {
        responseBody = null;
        try {
            HttpResponse response = null;
            HttpClient httpclient = new DefaultHttpClient();
            try
            {

                HttpPost httppost = new HttpPost(url);
                httppost.setHeader("Access-Control-Allow-Origin", "*");
                httppost.setEntity(new UrlEncodedFormEntity(arrayList));
                long startTime = System.currentTimeMillis();         /// Extra Can remove

                response = httpclient.execute(httppost);
                Header[] headers = response.getAllHeaders();


                //   Log.d("fav", "url..in "+url+"..."+arrayList.toString());
                //Log.d("fav", EntityUtils.toString(response.getEntity()));
                responseBody = EntityUtils.toString(response.getEntity());
                long elapsedTime = System.currentTimeMillis() - startTime;  /// Extra Can remove
                System.out.println("Total elapsed http request/response time in milliseconds: " + elapsedTime);/// Extra Can remove
                //    Log.d("vote", "rb"+responseBody);
                arrayList.clear();
            }
            catch (Exception e) {
                Log.d("exc", "EXCEPTION -- " + e);
                e.printStackTrace();
            }

        } catch (Exception e) {
            Log.e("TouchBase", "♦♦♦♦Error is : "+e.getMessage());
            e.printStackTrace();
        }

        if ( responseBody == null ) {
            Hashtable<String, String> result = new Hashtable<String, String>();
            result.put("status", "1");
            result.put("message", "Failed to fetch data.");
            Gson gson = new Gson();
            responseBody = gson.toJson(result);
        }
        return responseBody;
    }
    /*	public static String getflg()
	{
		flg = f;
		return flg;
	}*/
/*	public static String postData(String url) {
		responseBody = null;
		try {
			HttpResponse response = null;
			HttpClient httpclient = new DefaultHttpClient();
			try
			{
				HttpPost httppost = new HttpPost(url);

				response = httpclient.execute(httppost);
				//Log.d("app", EntityUtils.toString(response.getEntity()));
				responseBody = EntityUtils.toString(response.getEntity());
				Log.d("vote", "rb"+responseBody);
			}
			catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
		}
		return responseBody;
	}*/
}

