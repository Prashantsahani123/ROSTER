package com.NEWROW.row.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by USER1 on 03-01-2017.
 */
public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "BootReceiver", Toast.LENGTH_LONG).show();
        if ( intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.e("TouchBase", "♦♦♦♦Boot Completed");
            Toast.makeText(context, "Finally we come to know that boot is completed", Toast.LENGTH_LONG).show();
        }
    }
}
