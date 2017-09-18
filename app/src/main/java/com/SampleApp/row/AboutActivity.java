package com.SampleApp.row;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;

import android.widget.TextView;

import com.SampleApp.row.Utils.Constant;

/**
 * Created by USER on 17-12-2015.
 */
public class AboutActivity extends Activity {

    TextView tv_title, tvContact, tvEmail, tvWebsite,tvVersionNo;
           // tvRecommendFriend, ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tvEmail = (TextView) findViewById(R.id.etKaizeEmail);
        tvWebsite = (TextView) findViewById(R.id.etKaizenWebsite);
        //tvRecommendFriend = (TextView) findViewById(R.id.tvKaizenRecommend);
        tvContact = (TextView) findViewById(R.id.etKaizenContact);
        tvVersionNo = (TextView) findViewById(R.id.tvVersionNo);
        init();
    }

    public void init() {

        tv_title.setText("About");

        tvContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = tvContact.getTag().toString();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data)));
            }

        });

        tvWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = tvWebsite.getTag().toString();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data)));
            }
        });


        tvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = tvEmail.getTag().toString();


                final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setType("plain/text");
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});

                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(emailIntent);
            }

        });

//        tvRecommendFriend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email = tvEmail.getTag().toString();
//
//
//                final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//                emailIntent.setType("plain/text");
//                emailIntent.setData(Uri.parse("mailto:"));
//
//                emailIntent.putExtra(Intent.EXTRA_CC,new String[]{email});
//                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "I recommend this for you.");
//                emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("<html><body>I have been using TouchBase app and found it to be very useful people engagement tool. It is a focused, simplified collaborative app which any organization must have for structured communication.\n" +
//                        "\n<br /><br />" +
//                        "I recommend you to try out TouchBase by clicking the link below.<br /><br />" +
//                        "<center><a href=\"http://goo.gl/uK2UQQ\">Download TouchBase Now</a></center></body></html>"));
//                startActivity(emailIntent);
//            }
//
//        });
        tvVersionNo.setText(getVersionNumber());
    }

    public String getVersionNumber() {
        String version = "Version "+Constant.versionNo;
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            //pi.versionCode;
            version = "Version "+pi.versionName;

            Log.e("TouchBase", "♦♦♦♦Value of version name : "+pi.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }
}
