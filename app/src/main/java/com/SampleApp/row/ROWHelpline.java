package com.SampleApp.row;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ROWHelpline extends AppCompatActivity {

    TextView tv_mobile,tv_phone,tv_email,tv_website, tv_title;
    ImageView iv_backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rowhelpline);

        actionbarfunction();
        init();
    }
    private void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_title.setText("ROW Helpline");
    }
    private void init() {
        tv_mobile = (TextView) findViewById(R.id.tv_mobile);
        tv_phone = (TextView) findViewById(R.id.tv_Phone);
        tv_email = (TextView) findViewById(R.id.tv_Email);
        tv_website = (TextView) findViewById(R.id.tv_Website);

        tv_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", tv_mobile.getText().toString(), null));
                startActivity(intent);
            }
        });

        tv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", tv_phone.getText().toString(), null));
                startActivity(intent);
            }
        });


        tv_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setType("plain/text");
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{tv_email.getText().toString()});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                ROWHelpline.this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        });

        tv_website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ROWHelpline.this,OpenLinkActivity.class);
                i.putExtra("modulename","ROW Website");
                i.putExtra("link",tv_website.getText().toString());
                startActivity(i);
            }
        });

    }
}
