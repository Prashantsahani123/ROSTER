package com.SampleApp.row;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by USER on 29-12-2015.
 */
public class GroupCreated extends Activity {

    TextView tv_title, ib_next;
    ImageView iv_backbutton;
    ImageView iv_grpimg;
    private String groupid, groupname, groupimg;
    TextView tv_grpname;
    private ProgressBar progressbar;
    String trialMsg ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.group_created);

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_backbutton.setVisibility(View.GONE);
        tv_title.setText("Entity Created");

        tv_grpname = (TextView) findViewById(R.id.tv_grpname);
        iv_grpimg = (ImageView) findViewById(R.id.iv_grpimg);
        ib_next = (TextView) findViewById(R.id.ib_next);


        progressbar = (ProgressBar) findViewById(R.id.progressbar);

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            groupid = intent.getString("groupid"); // Created Group ID
            groupname = intent.getString("groupname"); // Created Group Name
            groupimg = intent.getString("groupimg"); // Created Group Image
            trialMsg = intent.getString("trialMsg"); // Created Group Image

            tv_grpname.setText(groupname);
// Add condition of blank
            popupTrialVersion(trialMsg);


            if (groupimg.equals("") || groupimg.toString() == null) {
                // linear_image.setVisibility(View.GONE);
            } else {
                progressbar.setVisibility(View.VISIBLE);
                Picasso.with(GroupCreated.this).load(groupimg)
                        .placeholder(R.drawable.imageplaceholder)
                        .into(iv_grpimg, new Callback() {
                            @Override
                            public void onSuccess() {
                                progressbar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {

                            }
                        });
            }
        }


        setvalues();

        init();

    }

    private void setvalues() {

    }

    private void init() {


        ib_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent i = new Intent(GroupCreated.this,Contact_Import.class);
                Intent i = new Intent(GroupCreated.this, DashboardActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
    }

    private void popupTrialVersion(String trialVersionMsg) {
        final Dialog dialog = new Dialog(GroupCreated.this, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_trial_version);

        TextView tv_subscribe = (TextView) dialog.findViewById(R.id.tv_subscribe);
        TextView tv_tryfree = (TextView) dialog.findViewById(R.id.tv_tryfree);
        TextView tv_line1 = (TextView) dialog.findViewById(R.id.tv_line1);
        tv_line1.setText(trialVersionMsg);

        tv_tryfree.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_subscribe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                dialog.dismiss();
                Uri uri = Uri.parse("http://touchbase.in/mobile/subscribes.html"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        dialog.show();
    }
    @Override
    public void onBackPressed() {
    }
}
