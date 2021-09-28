package com.NEWROW.row;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Adapter.PopupCallAdapter;


public class CallPopupActivity extends Activity {

    private PopupCallAdapter popupCallAdapter;
    ListView list;
    TextView fragTitle;
    String communicationType;
    String PopupCategoryPersonal,PopupCategoryBussiness,PopupCategoryFamily;
    public ImageView iv_fragment_send;
    ImageView iv_fragment_close;
    String message;
    String contactNo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        contactNo = "";

        setContentView(R.layout.popup_call_list);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        list = (ListView)findViewById(R.id.list);
        iv_fragment_close = (ImageView)findViewById(R.id.iv_fragment_close);
        iv_fragment_send = (ImageView)findViewById(R.id.iv_fragment_send);

        Intent i = getIntent();
        communicationType = i.getStringExtra("CommunicationType");
        PopupCategoryPersonal = i.getStringExtra("PopupCategoryPersonal");
        PopupCategoryBussiness = i.getStringExtra("PopupCategoryBussiness");
        PopupCategoryFamily = i.getStringExtra("PopupCategoryFamily");
        message = "hi";
        init();


    }


    @Override
    protected void onResume() {
        super.onResume();

        contactNo = "";
    }

    public void init()
    {

        iv_fragment_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupCallAdapter.checkboxArrayList.clear();
                finish();
            }
        });

        if(communicationType.equals("Call")) {
            fragTitle = (TextView) findViewById(R.id.tv_title);
            fragTitle.setText("Call");

            popupCallAdapter = new PopupCallAdapter(CallPopupActivity.this, R.layout.popup_call_list_item, ProfileActivityV4.contactInfoArrayList,"Call");
            list.setAdapter(popupCallAdapter);
        }
        else if(communicationType.equals("Message"))
        {
            fragTitle = (TextView) findViewById(R.id.tv_title);
            fragTitle.setText("Message");


            iv_fragment_send.setVisibility(View.VISIBLE);

            popupCallAdapter = new PopupCallAdapter(CallPopupActivity.this, R.layout.popup_call_list_item, ProfileActivityV4.contactInfoArrayList,"Message");
            list.setAdapter(popupCallAdapter);
        }

        else if(communicationType.equals("Email")) {
            fragTitle = (TextView) findViewById(R.id.tv_title);
            fragTitle.setText("Email");

            popupCallAdapter = new PopupCallAdapter(CallPopupActivity.this, R.layout.popup_call_list_item, ProfileActivityV4.contactInfoArrayList,"Email");
            list.setAdapter(popupCallAdapter);
        }




        iv_fragment_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(PopupCallAdapter.checkboxArrayList.size() == 0)
                {

                    Snackbar.make(findViewById(android.R.id.content), "Please select Contact", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();

                    Toast.makeText(CallPopupActivity.this,"Please select Contact",Toast.LENGTH_SHORT);

                    Log.e("@@@@@@@@@","Please select Contact");
                }
                else {
                    for (int i = 0; i < PopupCallAdapter.checkboxArrayList.size(); i++) {

                        if (i == PopupCallAdapter.checkboxArrayList.size() - 1) {
                            contactNo = contactNo + PopupCallAdapter.checkboxArrayList.get(i).toString();

                        } else {
                            contactNo = contactNo + PopupCallAdapter.checkboxArrayList.get(i).toString() + ";";
                        }
                    }

                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.putExtra("address", contactNo);
                    // here i can send message to emulator 5556,5558,5560
                    // you can change in real device
                    //smsIntent.putExtra("sms_body", "Hello my friends!");
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    startActivity(smsIntent);

                    finish();
                }
            }
        });

        Log.d("--!!!!---","-ProfileActivity.contactInfoArrayList--"+list);
    }

}
