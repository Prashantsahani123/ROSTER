package com.NEWROW.row;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by USER on 15-12-2015.
 */
public class PreferredLanguage extends Activity {


    ListView listview;
    ArrayAdapter<String> adapter;
    final String[] announcements = new String[]{"English ", "हिंदी", "中国", "français"};
    RadioButton radio;
    TextView tv_continue,tv_title;
    ImageView iv_backbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferred_lang);

        listview = (ListView) findViewById(R.id.listView);
        tv_continue = (TextView) findViewById(R.id.tv_continue);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);

        iv_backbutton.setVisibility(View.GONE);
        tv_title.setText("Preferred Language");




        adapter = new ArrayAdapter<String>(this, R.layout.preferred_lang_list, R.id.tv_lang, announcements);
        listview.setAdapter(adapter);


        init();

    }

    private void init() {

        tv_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PreferredLanguage.this, TermsAndConditionWelcome.class);
                startActivity(i);
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }


}
