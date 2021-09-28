package com.NEWROW.row;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by USER on 29-12-2015.
 */
public class EditContact extends Activity
{

    ListView listview;
    ArrayAdapter<String> adapter;
    TextView tv_title;
    ImageView iv_backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_contact);

        listview = (ListView) findViewById(R.id.listView);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        // iv_backbutton.setVisibility(View.GONE);
        tv_title.setText("Edit Contacts");


    }

}