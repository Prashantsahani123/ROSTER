package com.SampleApp.row.services;
//http://servicestest.touchbase.in/v5/api/Group/GetReplicaInfo?LastModuleID=17
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import com.SampleApp.row.Data.ReplicaInfoData;
import com.SampleApp.row.MyApplication;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.sql.DBHelper;
import com.SampleApp.row.sql.ReplicaInfoModel;

/**
 * Created by USER1 on 15-11-2016.
 */
public class ReplicaInfoSyncerService extends Service{
    Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Log.e("Touchabse", "♦♦♦♦Service onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Touchabse", "♦♦♦♦Service onDestroyed");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DBHelper helper = new DBHelper(context);
        Log.e("Touchbase", "♦♦♦♦Started onStartCommand");
        if ( InternetConnection.checkConnection(context)) {
            new SyncReplicaInfoTask().execute();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class SyncReplicaInfoTask extends AsyncTask<Void, Void, JSONObject> {

        int lastModuleId = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ReplicaInfoModel replicaModel = new ReplicaInfoModel(context);
            lastModuleId = replicaModel.getLastModuleId();
            Log.e("TouchBase", "♦♦♦♦Last Module Id : "+lastModuleId);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(Constant.GetReplicaInfo+"?LastModuleID="+lastModuleId);
            try {


                HttpResponse response = client.execute(get);
                if ( response.getStatusLine().getStatusCode() == 200 ) {
                    String responseText = EntityUtils.toString(response.getEntity());
                    JSONObject json = new JSONObject(responseText);
                    return json.getJSONObject("ModuleResult");
                } else {
                    JSONObject json = new JSONObject();
                    json.put("status", "1");
                    json.put("message", "Server status code is : "+response.getStatusLine().getStatusCode());
                    return json;
                }
            } catch (IOException ioe) {
                Log.e("Touchbase", "♦♦♦♦IO Exception while calling replica syncing");
                ioe.printStackTrace();
            } catch (JSONException jsone) {
                Log.e("Touchbase", "♦♦♦♦IO Exception while calling replica syncing");
                jsone.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject s) {
            super.onPostExecute(s);
            try {
                //Log.e("TouchBase", "♦♦♦♦Respose: "+s);
                String status = s.getString("status");

                if ( status.equals("0")) {
                    ArrayList<ReplicaInfoData> list = getReplicaInfoList(s);
                    syncModuleReplicaInfo(list);
                } else {
                    String message = s.getString("message");
                    Log.e("Touchbase", "♦♦♦♦"+message);
                }
            } catch(Exception e) {
                Log.e("Touchbase", "♦♦♦♦Exception");
                e.printStackTrace();
            }
        }

        public ArrayList<ReplicaInfoData> getReplicaInfoList(JSONObject json) {
            try {

                JSONArray ar = json.getJSONArray("ModuleList");
                ArrayList<ReplicaInfoData> list = new ArrayList<>();
                int n = ar.length();
                for(int i=0;i<n;i++) {
                    JSONObject info = ar.getJSONObject(i);
                    String moduleId = info.getString("moduleID");
                    String replicaOf = info.getString("replicaOfModule");
                    ReplicaInfoData replicaInfo = new ReplicaInfoData(moduleId, replicaOf);
                    list.add(replicaInfo);
                }
                return list;
            } catch(JSONException jsone ) {
                Log.e("Touchbase", "♦♦♦♦JSON Exception");
                jsone.printStackTrace();
            }
            return null;
        }
        public void syncModuleReplicaInfo(final ArrayList<ReplicaInfoData> list) {
            final ReplicaInfoModel model = new ReplicaInfoModel(MyApplication.getContext());
            final Handler handler = new Handler() {
                long count = 0;
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    count++;
                    //Log.e("Touchbase", "♦♦♦♦Replica List is : " + list);
                    boolean isSuccessful = model.insert(list);
                    Log.e("Touchbase", "♦♦♦♦isSuccessful : "+isSuccessful);
                    if ( count > 10) {
                        Log.e("Touchbase", "♦♦♦♦Failed to update replica info even after 10 attempts");
                        return;
                    }

                    Log.e("Touchbase", "♦♦♦♦Attempt Number : "+count);
                    if ( ! isSuccessful ) {
                        Log.e("Touchbase", "♦♦♦♦Failed to update replica info, Retrying in two seconds");

                        sendEmptyMessageDelayed(0, 2000);
                    } else {
                        Log.e("Touchbase", "♦♦♦♦Module replica info stored successfully");
                        //model.printTable();
                    }
                }
            };
            handler.sendEmptyMessage(0);
        }
    }
}
 /*
                Get Module List
                    URL :
                    http://servicestest.touchbase.in/v5/api/Group/GetAllModules

                    OutPut :
                    {
                    "ModuleResult": {
                    "status": "0",
                    "message": "success",
                    "ModuleList": [
                      {
                    "moduleID": "1",
                    "replicaOfModule": "1"
                    },
                      {
                    "moduleID": "2",
                    "replicaOfModule": "2"
                    },
                      {
                    "moduleID": "3",
                    "replicaOfModule": "3"
                    },
                      {
                    "moduleID": "4",
                    "replicaOfModule": "4"
                    },
                      {
                    "moduleID": "5",
                    "replicaOfModule": "5"
                    },
                      {
                    "moduleID": "6",
                    "replicaOfModule": "6"
                    },
                      {
                    "moduleID": "7",
                    "replicaOfModule": "7"
                    },
                      {
                    "moduleID": "8",
                    "replicaOfModule": "8"
                    },
                      {
                    "moduleID": "9",
                    "replicaOfModule": "9"
                    },
                      {
                    "moduleID": "10",
                    "replicaOfModule": "10"
                    },
                      {
                    "moduleID": "11",
                    "replicaOfModule": "11"
                    },
                      {
                    "moduleID": "12",
                    "replicaOfModule": "12"
                    },
                      {
                    "moduleID": "14",
                    "replicaOfModule": "14"
                    },
                      {
                    "moduleID": "15",
                    "replicaOfModule": "15"
                    },
                      {
                    "moduleID": "18",
                    "replicaOfModule": "18"
                    },
                      {
                    "moduleID": "16",
                    "replicaOfModule": "16"
                    },
                      {
                    "moduleID": "17",
                    "replicaOfModule": "17"
                    },
                      {
                    "moduleID": "19",
                    "replicaOfModule": "10"
                    },
                      {
                    "moduleID": "20",
                    "replicaOfModule": "15"
                    },
                      {
                    "moduleID": "21",
                    "replicaOfModule": "10"
                    }
                    ],
                    }
                    }
                * */