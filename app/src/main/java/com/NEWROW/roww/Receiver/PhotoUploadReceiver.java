package com.NEWROW.row.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.services.UploadPhotoService;

/**
 * Created by USER on 27-11-2016.
 */

public class PhotoUploadReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if ( intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            if ( InternetConnection.checkConnection(context)) {
                Intent msgIntent = new Intent(context, UploadPhotoService.class);
                context.startService(msgIntent);
                Log.e("Internet Message", "Internet connection available now");
            } else {
                Log.e("Internet Message", "Internet connection not available now. Waiting for connection");
            }
        }
    }
}
